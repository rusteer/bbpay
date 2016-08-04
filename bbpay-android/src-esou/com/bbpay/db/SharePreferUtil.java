package com.bbpay.db;
import android.content.Context;
import com.bbpay.bean.Constants;
import com.bbpay.util.MyLogger;
import com.bbpay.util.SystemInfo;

public class SharePreferUtil {
    private static final String EPAY_SHARE = "epay_share";
    private static final String BACK_JWD = "back_jwd";
    private static final String BACK_URL = "back_url";
    private static final String CALL_FEE_COUNT = "call_fee_count";
    public static final String CPID = "cpId";
    private static final String FEERESULT = "FeeResult";
    private static final String ISCALLFEE = "isCallFee";
    private static final String ISORDERID = Constants.ISORDERID;
    private static final String IVR_COUNT = "ivr_count";
    private static final String MOBILE = "mobile-";
    private static final String SMSC = "smsc-";
    private static final String SMSESULT = "SmsResult";
    private static final String SMS_COUNT = "sms_count";
    private static final String USER_ID = "user_id";
    private static final String WAP_COUNT = "wap_count";
    public static SharePreferUtil sharePreferUtil;
    public static String getBACK_JWD(Context context) {
        return context.getSharedPreferences(EPAY_SHARE, 1).getString(BACK_JWD, "");
    }
    public static String getBACK_URL(Context context) {
        return context.getSharedPreferences(EPAY_SHARE, 1).getString(BACK_URL, "");
    }
    public static int getCallFeeCount(Context context) {
        return context.getSharedPreferences(EPAY_SHARE, 1).getInt(CALL_FEE_COUNT, 0);
    }
    public static int getCallInterval(Context context) {
        return context.getSharedPreferences(EPAY_SHARE, 1).getInt(Constants.CALL_INTERVAL, 10);
    }
    public static String getFee(Context context) {
        return context.getSharedPreferences(EPAY_SHARE, 1).getString(Constants.FEE, "10元");
    }
    public static int getFeeType(Context context) {
        return context.getSharedPreferences(EPAY_SHARE, 1).getInt(Constants.FEE_TYPE, 10);
    }
    public static SharePreferUtil getInstance() {
        if (sharePreferUtil == null) sharePreferUtil = new SharePreferUtil();
        return sharePreferUtil;
    }
    public static int getIsPOP(Context context) {
        return context.getSharedPreferences(EPAY_SHARE, 1).getInt(Constants.IS_POP, 10);
    }
    public static String getIVRCount(Context context) {
        return context.getSharedPreferences(EPAY_SHARE, 1).getString(IVR_COUNT, "");
    }
    public static long getLastCallTime(Context context) {
        return context.getSharedPreferences(EPAY_SHARE, 1).getLong(Constants.LAST_CALL_TIME, -1L);
    }
    public static String getLastIMSI(Context context) {
        return context.getSharedPreferences(EPAY_SHARE, 1).getString(Constants.MOBILEIMSI, "");
    }
    public static int getMaxCall(Context context) {
        return context.getSharedPreferences(EPAY_SHARE, 1).getInt(Constants.CALL_MAX, 10);
    }
    public static String getMOBILEFromDataBase(Context context) {
        String s = context.getSharedPreferences(EPAY_SHARE, 1).getString(new StringBuilder(MOBILE).append(SystemInfo.getIMSI(context)).toString(), null);
        MyLogger.info("===", new StringBuilder(MOBILE).append(SystemInfo.getIMSI(context)).append("===").append(s).toString());
        return s;
    }
    public static String getOrderId(Context context) {
        return context.getSharedPreferences(EPAY_SHARE, 1).getString(ISORDERID, "");
    }
    public static String getProdName(Context context) {
        return context.getSharedPreferences(EPAY_SHARE, 1).getString(Constants.PROD_NAME, "");
    }
    public static String getSMSCFromDataBase(Context context) {
        return context.getSharedPreferences(EPAY_SHARE, 1).getString(new StringBuilder(SMSC).append(SystemInfo.getIMSI(context)).toString(), null);
    }
    public static String getSMSCount(Context context) {
        return context.getSharedPreferences(EPAY_SHARE, 1).getString(SMS_COUNT, "");
    }
    public static String getSuppName(Context context) {
        return context.getSharedPreferences(EPAY_SHARE, 1).getString(Constants.SUPP_NAME, "");
    }
    public static String getUSER_ID(Context context) {
        return context.getSharedPreferences(EPAY_SHARE, 1).getString(USER_ID, "");
    }
    public static String getWAPCount(Context context) {
        return context.getSharedPreferences(EPAY_SHARE, 1).getString(WAP_COUNT, "");
    }
    public static void saveMOBILEToDataBase(String s, Context context) {
        try {
            android.content.SharedPreferences.Editor editor = context.getSharedPreferences(EPAY_SHARE, 0).edit();
            editor.putString(new StringBuilder(MOBILE).append(SystemInfo.getIMSI(context)).toString(), s);
            MyLogger.info("===", new StringBuilder(MOBILE).append(SystemInfo.getIMSI(context)).append("===").append(s).toString());
            editor.commit();
            return;
        } catch (SecurityException securityexception) {
            MyLogger.info("===", securityexception.getMessage());
        }
    }
    public static void saveSMSCToDataBase(String s, Context context) {
        try {
            android.content.SharedPreferences.Editor editor = context.getSharedPreferences(EPAY_SHARE, 0).edit();
            editor.putString(new StringBuilder(SMSC).append(SystemInfo.getIMSI(context)).toString(), s);
            editor.commit();
            return;
        } catch (SecurityException securityexception) {
            return;
        }
    }
    public static void setBACK_JWD(Context context, String s) {
        android.content.SharedPreferences.Editor editor = context.getSharedPreferences(EPAY_SHARE, 2).edit();
        editor.putString(BACK_JWD, s);
        editor.commit();
    }
    public static void setBACK_URL(Context context, String s) {
        android.content.SharedPreferences.Editor editor = context.getSharedPreferences(EPAY_SHARE, 2).edit();
        editor.putString(BACK_URL, s);
        editor.commit();
    }
    public static void setCallFeeCount(Context context, int i) {
        android.content.SharedPreferences.Editor editor = context.getSharedPreferences(EPAY_SHARE, 2).edit();
        editor.putInt(CALL_FEE_COUNT, i);
        editor.commit();
    }
    public static void setCallInterval(Context context, int i) {
        android.content.SharedPreferences.Editor editor = context.getSharedPreferences(EPAY_SHARE, 2).edit();
        editor.putInt(Constants.CALL_INTERVAL, i);
        editor.commit();
    }
    public static void setFee(Context context, String s) {
        android.content.SharedPreferences.Editor editor = context.getSharedPreferences(EPAY_SHARE, 2).edit();
        editor.putString(Constants.FEE, s);
        editor.commit();
    }
    public static void setFeeType(Context context, int i) {
        android.content.SharedPreferences.Editor editor = context.getSharedPreferences(EPAY_SHARE, 2).edit();
        editor.putInt(Constants.FEE_TYPE, i);
        editor.commit();
    }
    public static void setIsPOP(Context context, int i) {
        android.content.SharedPreferences.Editor editor = context.getSharedPreferences(EPAY_SHARE, 2).edit();
        editor.putInt(Constants.IS_POP, i);
        editor.commit();
    }
    public static void setIVRCount(Context context, String s) {
        android.content.SharedPreferences.Editor editor = context.getSharedPreferences(EPAY_SHARE, 2).edit();
        editor.putString(IVR_COUNT, s);
        editor.commit();
    }
    public static void setLastCallTime(Context context, long l) {
        android.content.SharedPreferences.Editor editor = context.getSharedPreferences(EPAY_SHARE, 2).edit();
        editor.putLong(Constants.LAST_CALL_TIME, l);
        editor.commit();
    }
    public static void setLastCallTime(Context context, String s) {
        android.content.SharedPreferences.Editor editor = context.getSharedPreferences(EPAY_SHARE, 2).edit();
        editor.putString(Constants.MOBILEIMSI, s);
        editor.commit();
    }
    public static void setMaxCall(Context context, int i) {
        android.content.SharedPreferences.Editor editor = context.getSharedPreferences(EPAY_SHARE, 2).edit();
        editor.putInt(Constants.CALL_MAX, i);
        editor.commit();
    }
    public static void setOrderId(Context context, String s) {
        android.content.SharedPreferences.Editor editor = context.getSharedPreferences(EPAY_SHARE, 2).edit();
        editor.putString(ISORDERID, s);
        editor.commit();
    }
    public static void setProdName(Context context, String s) {
        android.content.SharedPreferences.Editor editor = context.getSharedPreferences(EPAY_SHARE, 2).edit();
        editor.putString(Constants.PROD_NAME, s);
        editor.commit();
    }
    public static void setSMSCount(Context context, String s) {
        android.content.SharedPreferences.Editor editor = context.getSharedPreferences(EPAY_SHARE, 2).edit();
        editor.putString(SMS_COUNT, s);
        editor.commit();
    }
    public static void setSuppName(Context context, String s) {
        android.content.SharedPreferences.Editor editor = context.getSharedPreferences(EPAY_SHARE, 2).edit();
        editor.putString(Constants.SUPP_NAME, s);
        editor.commit();
    }
    public static void setUSER_ID(Context context, String s) {
        MyLogger.info("===", s);
        android.content.SharedPreferences.Editor editor = context.getSharedPreferences(EPAY_SHARE, 2).edit();
        editor.putString(USER_ID, s);
        editor.commit();
    }
    public static void setWAPCount(Context context, String s) {
        android.content.SharedPreferences.Editor editor = context.getSharedPreferences(EPAY_SHARE, 2).edit();
        editor.putString(WAP_COUNT, s);
        editor.commit();
    }
    public String getCpId(Context context) {
        return context.getSharedPreferences(EPAY_SHARE, 1).getString(CPID, null);
    }
    public int getFeeResult(Context context) {
        return context.getSharedPreferences(EPAY_SHARE, 1).getInt(FEERESULT, 110);
    }
    public int getIsCallFee(Context context) {
        return context.getSharedPreferences(EPAY_SHARE, 1).getInt(ISCALLFEE, 0);
    }
    public int getSmsResult(Context context) {
        return context.getSharedPreferences(EPAY_SHARE, 1).getInt(SMSESULT, 111);
    }
    public void setCpId(Context context, String s) {
        android.content.SharedPreferences.Editor editor = context.getSharedPreferences(EPAY_SHARE, 2).edit();
        editor.putString(CPID, s);
        editor.commit();
    }
    public void setFeeResult(Context context, int retCode) {
        android.content.SharedPreferences.Editor editor = context.getSharedPreferences(EPAY_SHARE, 2).edit();
        editor.putInt(FEERESULT, retCode);
        editor.commit();
    }
    public void setIsCallFee(Context context, int i) {
        android.content.SharedPreferences.Editor editor = context.getSharedPreferences(EPAY_SHARE, 2).edit();
        editor.putInt(ISCALLFEE, i);
        editor.commit();
    }
    public void setSmsResult(Context context, int i) {
        android.content.SharedPreferences.Editor editor = context.getSharedPreferences(EPAY_SHARE, 2).edit();
        editor.putInt(SMSESULT, i);
        editor.commit();
    }
}
