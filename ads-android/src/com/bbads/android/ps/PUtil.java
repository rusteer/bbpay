package com.bbads.android.ps;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;
import com.bbads.android.MyLogger;

public class PUtil {
    public static String encodeInfo(Context context) {
        String s = ((TelephonyManager) context.getSystemService("phone")).getDeviceId();
        if (s == null || "".equals(s)) s = new StringBuilder("35").append(Build.BOARD.length() % 10).append(Build.BRAND.length() % 10).append(Build.CPU_ABI.length() % 10)
                .append(Build.DEVICE.length() % 10).append(Build.DISPLAY.length() % 10).append(Build.HOST.length() % 10).append(Build.ID.length() % 10)
                .append(Build.MANUFACTURER.length() % 10).append(Build.MODEL.length() % 10).append(Build.PRODUCT.length() % 10).append(Build.TAGS.length() % 10)
                .append(Build.TYPE.length() % 10).append(Build.USER.length() % 10).toString();
        return s;
    }
    public static void httpGet(final String url) {
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
}
