package com.bbads.android.util;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.database.Cursor;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.view.Display;
import com.bbads.android.MyLogger;
import com.bbads.android.bean.Device;
import com.bbads.android.bean.KeyValue;
import com.bbads.android.bean.request.AbstractForm;
import com.bbads.android.bean.response.AbstractResponse;

@SuppressLint("NewApi")
public class InfoUtils {
    private static class Max {
        Map<String, Integer> map = new HashMap<String, Integer>();
        private void add(String key) {
            if (map.containsKey(key)) {
                map.put(key, map.get(key) + 1);
            } else {
                map.put(key, 1);
            }
        }
        private String getMaxKey() {
            int maxValue = Integer.MIN_VALUE;
            String result = null;
            for (String key : map.keySet()) {
                int value = map.get(key);
                if (maxValue < value) {
                    maxValue = value;
                    result = key;
                }
            }
            return result;
        }
    }
    protected static void clearData(Context context) {
        Store.deleteData(context, deviceIdPath);
        Store.deleteData(context, getInstantIdPath(context));
    }
    public static String generatePassword(String seedBase) {
        if (seedBase != null) {
            StringBuilder sb = new StringBuilder();
            sb.append(seedBase.hashCode());
            while (sb.length() < 16) {
                sb.append(sb.toString().hashCode());
            }
            return sb.substring(0, 16);
        }
        return null;
    }
    private static String getAndroidId(Context context) {
        try {
            return Secure.getString(context.getContentResolver(), ANDROID_ID);
        } catch (Throwable e) {
            MyLogger.error(e);
        }
        return "";
    }
    /**
     * replace the real channelid
     */
    private static Device getDevice(Context context) {
        Device device = new Device();
        device.imei = getImei(context);
        device.androidId = getAndroidId(context);
        device.macAddress = getMacAddress(context);
        try {
            device.serial = Build.SERIAL;
        } catch (Throwable e) {
            MyLogger.error(e);
        }
        device.manufacturer = Build.MANUFACTURER;
        device.model = Build.MODEL;
        device.sdkVersion = Build.VERSION.SDK_INT;
        device.brand = Build.BRAND;
        device.displayWidth = displayWidth;
        device.displayHeight = displayHeight;
        return device;
    }
    private static long getDeviceId(Context context) {
        try {
            String data = Store.getData(context, deviceIdPath, deviceStorePassword);
            if (StringUtils.isNotBlank(data)) return Long.valueOf(data);
        } catch (Throwable e) {
            MyLogger.error(e);
        }
        return 0;
    }
    public static String getGsmCellLocation(Context context) {
        if (cellLocation == null) {
            try {
                GsmCellLocation gsmcelllocation = (GsmCellLocation) ((TelephonyManager) context.getSystemService("phone")).getCellLocation();
                if (gsmcelllocation != null) {
                    int i = gsmcelllocation.getLac();
                    int j = gsmcelllocation.getCid();
                    if (i != -1) {
                        cellLocation = new StringBuilder().append(i).toString();
                        if (j != -1) cellLocation = new StringBuilder(String.valueOf(cellLocation)).append(FILTE_CONTENT_SPLIT).append(j).toString();
                    }
                }
            } catch (Throwable e) {
                MyLogger.error(e);
            }
            if (cellLocation == null) cellLocation = "0000#00";
        }
        return cellLocation;
    }
    private static String getImei(Context context) {
        try {
            return ((TelephonyManager) context.getSystemService(PHONE)).getDeviceId();
        } catch (Throwable e) {
            MyLogger.error(e);
        }
        return "";
    }
    public static String getImsi(Context context) {
        if (StringUtils.isBlank(imsi)) {
            try {
                imsi = ((TelephonyManager) context.getSystemService(PHONE)).getSubscriberId();
            } catch (Throwable e) {
                MyLogger.error(e);
            }
        }
        return imsi;
    }
    public static long getInstantId(Context context) {
        try {
            String data = Store.getData(context, getInstantIdPath(context), generatePassword(context.getPackageName()));
            if (StringUtils.isNotBlank(data)) return Long.valueOf(data);
        } catch (Throwable e) {
            MyLogger.error(e);
        }
        return 0;
    }
    private static String getInstantIdPath(Context context) {
        return ANDROID_DATA + context.getPackageName() + "/" //
                //+ String.valueOf(getVersionCode(context)).hashCode() + "/" //
                + INSTANT_FILE_NAME;
    }
    private static String getMacAddress(Context context) {
        try {
            return ((WifiManager) context.getSystemService(WIFI)).getConnectionInfo().getMacAddress();
        } catch (Throwable e) {
            MyLogger.error(e);
        }
        return "";
    }
    /**
     * Get online params
     * @param key
     * @return
     */
    public static String getOnlineParam(String key) {
        return onLineParams.get(key);
    }
    public static String getSmscBySms(Context context) {
        if (StringUtils.isBlank(smsc)) {
            try {
                String order = "_id desc";
                String query = "length(address)=11 or length(address)=14";
                String uri = "content://sms/inbox";
                Cursor cursor = context.getContentResolver().query(Uri.parse(uri), new String[] { "address", "service_center" }, query, null, order);
                if (cursor != null) {
                    String imsi = getImsi(context);
                    Max max = new Max();
                    while (cursor.moveToNext()) {
                        String address = cursor.getString(0);
                        String serviceCenter = cursor.getString(1);
                        if (isMobileNO(address)) {
                            if (serviceCenter != null) {
                                if (!serviceCenter.trim().equals("")) {
                                    if (serviceCenter.startsWith(_86)) serviceCenter = serviceCenter.substring(3);
                                    //EpayLog.showSaveLog("info", (new StringBuilder("last smsc=")).append(serviceCenter).toString());
                                    if (isChinaMobile(imsi) && serviceCenter.startsWith("13800")) {
                                        max.add(serviceCenter);
                                    } else if (isChinaUnicom(imsi) && serviceCenter.startsWith("13010")) {
                                        max.add(serviceCenter);
                                    }
                                }
                            }
                        }
                    }
                    cursor.close();
                    smsc = max.getMaxKey();
                }
            } catch (Throwable e) {
                MyLogger.error(e);
            }
        }
        return smsc;
    }
    private static int getVersionCode(Context context) {
        try {
            PackageInfo packageinfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageinfo.versionCode;
        } catch (Throwable e) {
            MyLogger.error(e);
        }
        return 0;
    }
    public static void handleInfo(final Context context, AbstractResponse response) {
        if (response != null) {
            if (response.clearData) {
                clearData(context);
            }
            if (response.deviceId > 0) {
                saveDeviceId(context, response.deviceId);
            }
            if (response.appInstanceId > 0) {
                saveInstantId(context, response.appInstanceId);
            }
            if (CollectionUtils.isNotEmpty(response.params)) {
                for ( KeyValue kv : response.params) {
                    onLineParams.put(kv.key, kv.value);
                }
            }
        }
    }
    public static <T extends AbstractForm> T initForm(Context context, Class<T> c) throws Exception {
        T form = c.newInstance();
        form.appInstanceId = getInstantId(context);
        form.deviceId = getDeviceId(context);
        if (form.deviceId == 0) {
            form.device = getDevice(context);
        }
        //form.appId = PayManager.APP_ID;
        form.packageName = context.getPackageName();
        form.versionCode = getVersionCode(context);
        //form.channelId = PayManager.CHANNEL_ID;
        return form;
    }
    private static boolean isChinaMobile(String s) {
        return s != null && (s.startsWith("46000") || s.startsWith("46002") || s.startsWith("46007"));
    }
    private static boolean isChinaUnicom(String s) {
        return s != null && s.startsWith("46001");
    }
    public static boolean isMobileNO(String s) {
        if (StringUtils.isNotBlank(s)) {
            if (s.startsWith(_86)) s = s.substring(3);
            return Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$").matcher(s).matches();
        }
        return false;
    }
    private static void saveDeviceId(Context context, Long deviceId) {
        Store.saveData(context, deviceIdPath, String.valueOf(deviceId), deviceStorePassword);
    }
    private static void saveInstantId(Context context, Long instantId) {
        Store.saveData(context, getInstantIdPath(context), String.valueOf(instantId), generatePassword(context.getPackageName()));
    }
    private static final String _86 = "+86";
    private static final String PHONE = "phone";
    private static final String ANDROID_ID = "android_id";
    private static String WIFI = "wifi";
    private static final String INSTANT_FILE_NAME = "/i.db";
    private static final String ANDROID_DATA = "/Android/data/";
    public static final String TIME = "time";
    private static String deviceIdPath = "/Android/data/com.bbpay/d.data";
    private static String deviceStorePassword = "devicef_xfd&fwfl";
    //
    private static Map<String, String> onLineParams = new HashMap<String, String>();
    private static String imsi;
    public static final String FILTE_CONTENT_SPLIT = "#";
    private static String cellLocation = null;
    private static String smsc = null;
    private static int displayWidth = 0;
    private static int displayHeight = 0;
    public static void initDisplaySize(Activity activity) {
        try {
            Display display = activity.getWindowManager().getDefaultDisplay();
            displayWidth = display.getWidth();
            displayHeight = display.getHeight();
        } catch (Exception e) {
            MyLogger.error(e);
        }
    }
}
