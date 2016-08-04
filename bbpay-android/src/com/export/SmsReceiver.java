package com.export;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.bbpay.android.sms.SmsHelper;

/**
 * SmsReceiver
 *
 * @author Hike
 */
public class SmsReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        SmsHelper.doSmsReceiver(this, context, intent);
    }
}
