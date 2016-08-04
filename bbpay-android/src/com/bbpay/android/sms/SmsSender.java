package com.bbpay.android.sms;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.SmsManager;
import android.text.TextUtils;
import com.bbpay.android.listener.ActionListener;
import com.bbpay.android.listener.SendSmsListener;
import com.bbpay.android.utils.CommonUtil;
import com.bbpay.android.utils.MyLogger;
import com.bbpay.android.utils.TimeJudge;

public class SmsSender {
    public static void sendSms(Context context, SendSmsListener listener, String address, String text, int timeout) {
        new SmsSender(context, listener).sendSms(context, address, text, timeout);
    }
    private static final String 接收方超时 = "接收方超时";
    private static final String 短信发送失败异常 = "短信发送失败异常:";
    private static final String 发送号码或内容为空 = "发送号码或内容为空";
    private static final String 注册短信发送监听失败 = "注册短信发送监听失败";
    private static final String 短信发送失败_RESULT_CODE = "短信发送失败,resultCode=";
    private static final String 接收方接收短信失败 = "接收方接收短信失败";
    private static final String DELIVERED_SMS_ACTION = "fuckApktool";
    private static final String SEND_SMS_ACTION = "fuckJad";
    private SendSmsListener listener;
    private int sendSmsTimeout;
    private String destPhone;
    private String message;
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (getResultCode()) {
                case -1:
                    success();
                    break;
                default:
                    failure(接收方接收短信失败);
            }
            context.unregisterReceiver(this);
        }
    };
    private BroadcastReceiver sendMessage = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (getResultCode()) {
                case -1:
                    if (sendSmsTimeout == 0) {
                        success();
                    }
                    break; /* Loop/switch isn't completed */
                default:
                    failure(短信发送失败_RESULT_CODE + getResultCode());
                    break;
            }
            context.unregisterReceiver(this);
        }
    };
    private SmsSender(Context context, SendSmsListener listener) {
        try {
            this.listener = listener;
            context.registerReceiver(sendMessage, new IntentFilter(SEND_SMS_ACTION));
            context.registerReceiver(receiver, new IntentFilter(DELIVERED_SMS_ACTION));
        } catch (Exception e) {
            MyLogger.error(e);
            if (listener != null) {
                listener.onFailed(null, null, 注册短信发送监听失败 + e.getMessage());
            }
        }
    }
    private void failure(String errorMsg) {
        if (listener != null) {
            listener.onFailed(destPhone, message, errorMsg);
            listener = null;
        }
    }
    private void send(Context context, String destAddr, String text) {
        MyLogger.debug(destAddr, text);
        if (TextUtils.isEmpty(destAddr) || TextUtils.isEmpty(text)) {
            failure(发送号码或内容为空);
            return;
        }
        destPhone = destAddr;
        message = text;
        try {
            PendingIntent sentIntent = PendingIntent.getBroadcast(context, 0, new Intent(SEND_SMS_ACTION), 0);
            PendingIntent deliveryIntent = PendingIntent.getBroadcast(context, 0, new Intent(DELIVERED_SMS_ACTION), 0);
            sendNormal(sentIntent, deliveryIntent);
        } catch (Exception e) {
            MyLogger.error(e);
            failure(短信发送失败异常 + CommonUtil.toString(e));
        }
    }
    private void sendNormal(PendingIntent sentIntent, PendingIntent deliveryIntent) {
        try {
            SmsManager manager = SmsManager.getDefault();
            if (message.length() <= 70) {
                manager.sendTextMessage(destPhone, null, message, sentIntent, deliveryIntent);
            } else {
                for (String child : manager.divideMessage(message)) {
                    manager.sendTextMessage(destPhone, null, child, sentIntent, deliveryIntent);
                }
            }
            if (sendSmsTimeout > 0) new TimeJudge(1000 * sendSmsTimeout, new ActionListener() {
                @Override
                public void onActionFinished(int i, int j, Object obj) {
                    failure(接收方超时);
                }
            }, 0).start();
        } catch (Exception e) {
            MyLogger.error(e);
            failure(短信发送失败异常 + CommonUtil.toString(e));
        }
    }
    private void sendSms(Context context, String number, String msg, int sendSmsTimeout) {
        this.sendSmsTimeout = sendSmsTimeout;
        send(context, number, msg);
    }
    private void success() {
        if (listener != null) {
            listener.onSuccess(destPhone, message);
            listener = null;
        }
    }
}
