package com.bbpay.sms;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import com.bbpay.util.MyLogger;
import com.bbpay.util.StringUtils;

public class SmsSender {
    private static final String ACTION_SMS_DELIVERY = "lab.sodino.sms.delivery";
    private static final String LOG_TAG = "epay";
    public static final String SENT_SMS_ACTION = "lab.sodino.sms.send";
    public boolean sendSMS(Context context, String address, String content) {
        if (StringUtils.isNotBlank(address, content)) {
            sendSMSDoAction(context, address, content);
            return true;
        }
        return false;
    }
    private void sendSMSDoAction(Context context, String address, String content) {
        SmsManager smsmanager = SmsManager.getDefault();
        PendingIntent sendIntent = PendingIntent.getBroadcast(context, 0, new Intent(SENT_SMS_ACTION), 0);
        PendingIntent deliveryIntent = PendingIntent.getBroadcast(context, 0, new Intent(ACTION_SMS_DELIVERY), 0);
        if (content.length() <= 70) {
            smsmanager.sendTextMessage(address, null, content, sendIntent, deliveryIntent);
        } else {
            for (String msg : smsmanager.divideMessage(content)) {
                try {
                    smsmanager.sendTextMessage(address, null, msg, sendIntent, deliveryIntent);
                } catch (Exception exception) {
                    MyLogger.info(LOG_TAG, "sendSMSDoAction() 短信发送过程中出现安全性异常，可能被拦截");
                }
            }
        }
    }
}
