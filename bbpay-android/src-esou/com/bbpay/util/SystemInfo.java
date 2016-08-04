package com.bbpay.util;
import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Proxy;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import com.bbpay.bean.Constants;
import com.bbpay.db.SharePreferUtil;

public class SystemInfo {
    public static final String CHINA_MOBILE = Constants.MOBILE;
    public static final String CHINA_TELECOM = "telecom";
    public static final String CHINA_UNICOM = "unicom";
    public static String LOG_TAG = "platform";
    public static final String NETWORK_TYPE_3G_NET = "3g-net";
    public static final String NETWORK_TYPE_3G_WAP = "3g-wap";
    public static final String NETWORK_TYPE_EDGE_NET = "edge-net";
    public static final String NETWORK_TYPE_EDGE_WAP = "edge-wap";
    public static final String NETWORK_TYPE_GPRS_NET = "gprs-net";
    public static final String NETWORK_TYPE_GPRS_WAP = "gprs-wap";
    public static final String NETWORK_TYPE_WIFI = "wifi";
    public static final String UNKNOW = "unknow";
    private static String mNetworkInfo = UNKNOW;
    private static String smsc = "00000000000";
    private static String bt;
    public static String cardType;
    private static int cpId = -1;
    private static int fee = -1;
    private static boolean hasSuperAction;
    private static String imei;
    private static String imsi;
    private static String lac;
    private static int mScreenHeightPixels;
    private static int mScreenWidthPixels;
    private static String mid;
    private static String midx;
    private static String orderId = "";
    private static String p = "";
    private static String screen;
    private static int serviceId = -1;
    private static int simState;
    private static boolean startService;
    public static void _getNetworkInfo(Context context) {
        label0: {
            label1: {
                NetworkInfo networkinfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
                mNetworkInfo = UNKNOW;
                if (networkinfo != null) switch (networkinfo.getType()) {
                    default:
                        mNetworkInfo = UNKNOW;
                        break;
                    case 0: // '\0'
                        break label1;
                    case 1: // '\001'
                        break label0;
                }
                return;
            }
            int i = ((TelephonyManager) context.getSystemService(Constants.PHONE)).getNetworkType();
            java.net.Proxy proxy = getProxy(context);
            if (i == 1) if (proxy != null) {
                mNetworkInfo = NETWORK_TYPE_GPRS_WAP;
                return;
            } else {
                mNetworkInfo = NETWORK_TYPE_GPRS_NET;
                return;
            }
            if (i == 2 || i == 0) if (proxy != null) {
                mNetworkInfo = NETWORK_TYPE_EDGE_WAP;
                return;
            } else {
                mNetworkInfo = NETWORK_TYPE_EDGE_NET;
                return;
            }
            if (proxy != null) {
                mNetworkInfo = NETWORK_TYPE_3G_WAP;
                return;
            } else {
                mNetworkInfo = NETWORK_TYPE_3G_NET;
                return;
            }
        }
        mNetworkInfo = NETWORK_TYPE_WIFI;
    }
    public static String getBT(Context context) {
        WifiInfo wifiinfo;
        if (bt != null) return bt;
        wifiinfo = ((WifiManager) context.getSystemService(NETWORK_TYPE_WIFI)).getConnectionInfo();
        if (wifiinfo != null) {
            String s = wifiinfo.getMacAddress();
            if (s != null) {
                int i;
                bt = "";
                i = 0;
                while (i < s.length()) {
                    char c = s.charAt(i);
                    if (c != ':') bt = new StringBuilder(String.valueOf(bt)).append(c).toString();
                    i++;
                }
            }
        }
        return bt;
    }
    public static String getCardType(Context context) {
        String s = getIMSI(context);
        cardType = UNKNOW;
        if (s != null) {
            if (s.startsWith("46000") || s.startsWith("46002") || s.startsWith("46007")) cardType = Constants.MOBILE;
            else if (s.startsWith("46001")) {
                cardType = CHINA_UNICOM;
            } else if (s.startsWith("46003")) {
                cardType = CHINA_TELECOM;
            }
        }
        return cardType;
    }
    public static int getCpId(Context context) {
        if (cpId != -1) return cpId;
        try {
            cpId = context.getPackageManager().getApplicationInfo(context.getPackageName(), 128).metaData.getInt(Constants.CPID);
        } catch (Exception exception) {
            cpId = 0;
            MyLogger.info("===", new StringBuilder("得到CPID 异常").append(exception.getMessage()).toString());
        }
        return cpId;
    }
    public static String getCurrentTime() {
        return new StringBuilder(String.valueOf(new SimpleDateFormat("HHmmssSSS").format(Calendar.getInstance().getTime()))).append(getIntRandom(100, 999)).toString();
    }
    public static int getFee(Context context) {
        if (fee != -1) return fee;
        try {
            fee = context.getPackageManager().getApplicationInfo(context.getPackageName(), 128).metaData.getInt(Constants.EPAY_FEE);
        } catch (Exception exception) {
            fee = -1;
            MyLogger.info("===", new StringBuilder("得到fee 异常").append(exception.getMessage()).toString());
        }
        return fee;
    }
    public static String getIMEI(Context context) {
        if (imei != null) return imei;
        imei = ((TelephonyManager) context.getSystemService(Constants.PHONE)).getDeviceId();
        if (imei == null) imei = "";
        return imei;
    }
    public static String getIMSI(Context context) {
        if (imsi != null) return imsi;
        imsi = ((TelephonyManager) context.getSystemService(Constants.PHONE)).getSubscriberId();
        if (imsi == null) imsi = "";
        return imsi;
    }
    private static int getIntRandom(int i, int j) {
        return i + (int) (Math.random() * (1 + j - i));
    }
    public static String getLAC(Context context) {
        if (lac != null) return lac;
        GsmCellLocation gsmcelllocation = (GsmCellLocation) ((TelephonyManager) context.getSystemService(Constants.PHONE)).getCellLocation();
        if (gsmcelllocation != null) {
            int i = gsmcelllocation.getLac();
            int j = gsmcelllocation.getCid();
            if (i != -1) {
                lac = new StringBuilder().append(i).toString();
                if (j != -1) lac = new StringBuilder(String.valueOf(lac)).append(Constants.FILTE_CONTENT_SPLIT).append(j).toString();
            }
        }
        if (lac == null) lac = "0000#00";
        return lac;
    }
    public static String getMID(Context context) {
        if (mid != null) {
            return mid;
        } else {
            mid = new StringBuilder(String.valueOf(getSMSC())).append('#').append(getIMSI(context)).toString();
            midx = mid;
            return mid;
        }
    }
    public static String getMIDX(Context context) {
        if (midx != null) {
            return midx;
        } else {
            midx = getMID(context);
            return midx;
        }
    }
    public static String getMobileNum(Context context) {
        return SharePreferUtil.getMOBILEFromDataBase(context);
    }
    public static String getNativePhoneNumber(Context context) {
        return ((TelephonyManager) context.getSystemService(Constants.PHONE)).getLine1Number();
    }
    public static String getNetworkInfo(Context context) {
        _getNetworkInfo(context);
        return mNetworkInfo;
    }
    public static String getOrderId() {
        return orderId;
    }
    public static String getP(Context context) {
        if (p != null && !p.trim().equals("")) return p;
        try {
            p = context.getPackageManager().getApplicationInfo(context.getPackageName(), 128).metaData.getString(Constants.EPAY_P);
        } catch (Exception exception) {
            p = "";
            MyLogger.info("===", new StringBuilder("得到备用字段 异常").append(exception.getMessage()).toString());
        }
        return p;
    }
    public static String getPackageName(Context context) {
        if (context == null) {
            return null;
        } else {
            String s = context.getPackageName();
            MyLogger.info("", new StringBuilder("包名为").append(s).toString());
            return s;
        }
    }
    public static java.net.Proxy getProxy(Context context) {
        NetworkInfo networkinfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        if (networkinfo != null) {
            if (networkinfo.getType() == 1) return null;
            String s = Proxy.getDefaultHost();
            if (s != null && !s.equals("")) { return new java.net.Proxy(java.net.Proxy.Type.HTTP, new InetSocketAddress(Proxy.getDefaultHost(), Proxy.getDefaultPort())); }
        }
        return null;
    }
    public static String getScreen(Context context) {
        if (screen != null) {
            return screen;
        } else {
            screen = new StringBuilder(String.valueOf(getScreenWidth(context))).append("x").append(getScreenHeight(context)).toString();
            return screen;
        }
    }
    public static int getScreenHeight(Context context) {
        if (mScreenHeightPixels > 0) {
            return mScreenHeightPixels;
        } else {
            setScreenInfomation(context);
            return mScreenHeightPixels;
        }
    }
    public static int getScreenWidth(Context context) {
        if (mScreenWidthPixels > 0) {
            return mScreenWidthPixels;
        } else {
            setScreenInfomation(context);
            return mScreenWidthPixels;
        }
    }
    public static int getServiceId(Context context) {
        if (serviceId != -1) return serviceId;
        try {
            serviceId = context.getPackageManager().getApplicationInfo(context.getPackageName(), 128).metaData.getInt(Constants.SERVICE_ID);
        } catch (Exception exception) {
            serviceId = -1;
            MyLogger.info("===", new StringBuilder("得到serviceId 异常").append(exception.getMessage()).toString());
        }
        return serviceId;
    }
    public static int getSimState(Context context) {
        try {
            simState = ((TelephonyManager) context.getSystemService(Constants.PHONE)).getSimState();
        } catch (Exception exception) {}
        return simState;
    }
    public static String getSMSC() {
        return smsc;
    }
    public static String getUserIdFromDB(Context context) {
        String s = SharePreferUtil.getUSER_ID(context);
        String s1 = FileUtil.getUserIdFromFile();
        MyLogger.info("ttt", new StringBuilder("db -uid is ").append(s).append(" , file uid is ").append(s1).toString());
        if (s != null && !s.trim().equals("")) {
            if (s1 == null || s1.trim().equals("")) FileUtil.saveUserIdFile(s);
            return judgeUserImsi(context, s);
        }
        if (s1 != null && !s1.trim().equals("")) {
            if (s == null || s.trim().equals("")) SharePreferUtil.setUSER_ID(context, s1);
            return judgeUserImsi(context, s1);
        } else {
            return null;
        }
    }
    public static boolean hasSuperAction() {
        return hasSuperAction;
    }
    public static boolean isMobileNO(String s) {
        if (s == null || s.trim().equals("")) {
            return false;
        } else {
            Matcher matcher = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$").matcher(s);
            MyLogger.info("", new StringBuilder("输入的电话号码是 ").append(s).append(" ,,,").append(matcher.matches()).toString());
            return matcher.matches();
        }
    }
    public static boolean isStartService() {
        return startService;
    }
    private static String judgeUserImsi(Context context, String s) {
        if (s.contains(getIMSI(context))) return s.substring(1 + s.indexOf("-"));
        else return null;
    }
    public static void saveUserIdFromDB(String s, Context context) {
        String s1 = new StringBuilder(String.valueOf(getIMSI(context))).append("-").append(s).toString();
        FileUtil.saveUserIdFile(s1);
        SharePreferUtil.setUSER_ID(context, s1);
    }
    public static void setCpId(int i) {
        cpId = i;
    }
    public static void setFee(int i) {
        fee = i;
    }
    public static void setHasSuperAction(boolean flag) {
        hasSuperAction = flag;
    }
    public static void setOrderId(String s) {
        orderId = s;
    }
    private static void setScreenInfomation(Context context) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((WindowManager) context.getSystemService("window")).getDefaultDisplay().getMetrics(displaymetrics);
        mScreenHeightPixels = displaymetrics.heightPixels;
        mScreenWidthPixels = displaymetrics.widthPixels;
    }
    public static void setServiceId(int i) {
        serviceId = i;
    }
    public static void setSMSC(Context context, String s) {
        if (s != null && !s.trim().equals("") && s.length() == 11) {
            smsc = s;
            mid = new StringBuilder(String.valueOf(getSMSC())).append('#').append(getIMSI(context)).toString();
            midx = mid;
        }
    }
    public static void setStartServiceState(boolean flag) {
        startService = flag;
    }
    public static void updateIMSI(Context context) {
        imsi = "";
        cardType = UNKNOW;
        imsi = ((TelephonyManager) context.getSystemService(Constants.PHONE)).getSubscriberId();
        if (imsi != null) {
            if (imsi.startsWith("46000") || imsi.startsWith("46002") || imsi.startsWith("46007")) {
                cardType = Constants.MOBILE;
            } else if (imsi.startsWith("46001")) {
                //System.out.println("是联通卡");
                cardType = CHINA_UNICOM;
            } else if (imsi.startsWith("46003")) {
                cardType = CHINA_TELECOM;
            }
        }
    }
}
