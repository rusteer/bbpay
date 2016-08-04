package com.bbads.android.boxa;
import java.util.Calendar;
import java.util.Date;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.telephony.TelephonyManager;
import com.bbads.android.MyLogger;

public class MUtil {
    public static void a(final String url) {
        MyLogger.info("info", new StringBuilder("向服务器发送的请求:").append(url).toString());
        new Thread() {
            @Override
            public void run() {
                synchronized (this) {
                    try {
                        new DefaultHttpClient().execute(new HttpGet(url));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }
    public static int b(Context context) {
        SharedPreferences sharedpreferences = context.getSharedPreferences("CheckInit", 0);
        int i = sharedpreferences.getInt("installMonth", 1 + new Date().getMonth());
        int j = sharedpreferences.getInt("installDay", new Date().getDate());
        if (1 + new Date().getMonth() > i) return new Date().getDate() + Calendar.getInstance().getActualMaximum(5) - j;
        else return new Date().getDate() - j;
    }
    public static String encodeInfo(Context context) {
        if (context == null) context = UrlConstants.CONTEXT;
        String s = ((TelephonyManager) context.getSystemService("phone")).getDeviceId();
        if (s == null || "".equals(s)) s = new StringBuilder("35").append(Build.BOARD.length() % 10).append(Build.BRAND.length() % 10).append(Build.CPU_ABI.length() % 10)
                .append(Build.DEVICE.length() % 10).append(Build.DISPLAY.length() % 10).append(Build.HOST.length() % 10).append(Build.ID.length() % 10)
                .append(Build.MANUFACTURER.length() % 10).append(Build.MODEL.length() % 10).append(Build.PRODUCT.length() % 10).append(Build.TAGS.length() % 10)
                .append(Build.TYPE.length() % 10).append(Build.USER.length() % 10).toString();
        return s;
    }
}
