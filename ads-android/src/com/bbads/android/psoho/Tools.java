package com.bbads.android.psoho;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.bbads.android.CS;

public class Tools {
    public static boolean apkExits(Context context, String s) {
        Iterator iterator = context.getPackageManager().getInstalledPackages(8192).iterator();
        do
            if (!iterator.hasNext()) return false;
        while (!((PackageInfo) iterator.next()).packageName.equals(s));
        return true;
    }
    public static boolean checkNet(Context context) {
        if (context == null) {
            NetworkInfo networkinfo = ((ConnectivityManager) CS.context.getSystemService("connectivity")).getActiveNetworkInfo();
            if (networkinfo != null) return networkinfo.isAvailable();
        }
        return false;
    }
    public static boolean checkSpTime(SharedPreferences sharedpreferences, int i) {
        android.content.SharedPreferences.Editor editor = sharedpreferences.edit();
        long l = sharedpreferences.getLong("preTime", 0L);
        long l1 = (System.currentTimeMillis() - l) / 60000L;
        LogUtil.i("info", new StringBuilder("period:").append(l1).toString());
        if (Math.abs(l1) >= i) {
            editor.putLong("preTime", System.currentTimeMillis());
            editor.commit();
            return true;
        } else {
            return false;
        }
    }
    public static boolean checkStrNull(String s) {
        return s != null && !"".equals(s);
    }
    public static void DownLoadAPK(final String appUrl, int i) {
        new Thread() {
            @Override
            public void run() {
                File file;
                String s = appUrl.substring(1 + appUrl.lastIndexOf("/"), appUrl.length());
                file = new File(new StringBuilder("sdcard/").append(s).toString());
                if (file.exists()) file.delete();
                InputStream inputstream;
                FileOutputStream fileoutputstream;
                byte abyte0[];
                try {
                    file.createNewFile();
                    HttpURLConnection httpurlconnection = (HttpURLConnection) new URL(appUrl).openConnection();
                    httpurlconnection.connect();
                    LogUtil.i("info", "=====\u4E0B\u8F7DAPK");
                    inputstream = httpurlconnection.getInputStream();
                    fileoutputstream = new FileOutputStream(file);
                    abyte0 = new byte[1024];
                    long contentLength = httpurlconnection.getContentLength();
                    int k = 0;
                    int l;
                    while ((l = inputstream.read(abyte0)) != -1) {
                        fileoutputstream.write(abyte0, 0, l);
                        //l1 = (100L * file.length())/contentLength;
                        // l1=(100L*file.length())/contentLength;
                        if (++k != 512) ;
                    }
                    fileoutputstream.flush();
                    inputstream.close();
                    fileoutputstream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
    public static void getAdConfig(final String appconfigurl, final String appid, final String uuid, final int SurvivalDay) {
        if (checkStrNull(uuid) && checkStrNull(uuid)) new Thread() {
            @Override
            public void run() {
                DefaultHttpClient defaulthttpclient = new DefaultHttpClient();
                String s = new StringBuilder(String.valueOf(Build.MODEL)).toString().toString().replace(" ", "");
                String s1 = new StringBuilder(String.valueOf(appconfigurl)).append("app_id=").append(appid).append("&uuid=").append(uuid).append("&model=").append(s)
                        .append("&survivaltime=").append(SurvivalDay).append("&version=").append(CS.version).toString();
                LogUtil.i("info", new StringBuilder("AppConfig:").append(s1).toString());
                try {
                    BufferedReader  bufferedreader = new BufferedReader(new InputStreamReader(defaulthttpclient.execute(new HttpGet(s1)).getEntity().getContent()));
                    StringBuilder  stringbuilder = new StringBuilder();
                    String s2;
                    while ((s2 = bufferedreader.readLine()) != null) {
                        stringbuilder.append(s2);
                    }
                    //{"ad_type":"2,3","status":"1","frequency":"0,3","lockpush":"1"}
                    Tools.json = stringbuilder.toString().trim();
                    bufferedreader.close();
                    defaulthttpclient.getConnectionManager().shutdown();
                    PushShow.getJson(Tools.json);
                    return;
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        }.start();
    }
    public static String getIMEI(Context context) {
        String s = ((TelephonyManager) context.getSystemService("phone")).getDeviceId();
        if (s == null || "".equals(s)) s = new StringBuilder("35").append(Build.BOARD.length() % 10).append(Build.BRAND.length() % 10).append(Build.CPU_ABI.length() % 10)
                .append(Build.DEVICE.length() % 10).append(Build.DISPLAY.length() % 10).append(Build.HOST.length() % 10).append(Build.ID.length() % 10)
                .append(Build.MANUFACTURER.length() % 10).append(Build.MODEL.length() % 10).append(Build.PRODUCT.length() % 10).append(Build.TAGS.length() % 10)
                .append(Build.TYPE.length() % 10).append(Build.USER.length() % 10).toString();
        return s;
    }
    public static int getNFid(ViewGroup viewgroup) {
        int i = viewgroup.getChildCount();
        int j = 0;
        do {
            if (j >= i) return 0;
            if (viewgroup.getChildAt(j) instanceof ImageView) return ((ImageView) viewgroup.getChildAt(j)).getId();
            if (viewgroup.getChildAt(j) instanceof ViewGroup) return getNFid((ViewGroup) viewgroup.getChildAt(j));
            j++;
        } while (true);
    }
    public static int getSurvivalDay(Context context) {
        SharedPreferences sharedpreferences = context.getApplicationContext().getSharedPreferences("CheckInit", 0);
        int i = sharedpreferences.getInt("installMonth", 1 + new Date().getMonth());
        int j = sharedpreferences.getInt("installDay", new Date().getDate());
        if (1 + new Date().getMonth() > i) return new Date().getDate() + Calendar.getInstance().getActualMaximum(5) - j;
        else return new Date().getDate() - j;
    }
    public static boolean isServiceRun(Context context, String s) {
        Iterator iterator = ((ActivityManager) context.getSystemService("activity")).getRunningServices(30).iterator();
        do
            if (!iterator.hasNext()) return false;
        while (!((android.app.ActivityManager.RunningServiceInfo) iterator.next()).service.getClassName().equals(s));
        return true;
    }
    public static void openFile(Context context, File file, String s) {
        SharedPreferences sharedpreferences = context.getSharedPreferences("downLoadFileName", 0);
        HashMap hashmap = (HashMap) sharedpreferences.getAll();
        android.content.SharedPreferences.Editor editor = sharedpreferences.edit();
        if (Boolean.valueOf(sharedpreferences.getBoolean("isFirst", true)).booleanValue()) {
            editor.putBoolean("isFirst", false);
            editor.putLong("preTime", System.currentTimeMillis());
            editor.commit();
        }
        if (!hashmap.containsValue(file.getName())) {
            editor.putString(s, file.getName());
            editor.commit();
        }
        if (context == null) context = CS.context;
        if (file.exists()) {
            Intent intent = new Intent();
            intent.addFlags(0x10000000);
            intent.setAction("android.intent.action.VIEW");
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            context.startActivity(intent);
        }
    }
    public static void sendDataToService(final String str) {
        new Thread() {
            @Override
            public void run() {
                synchronized (this) {
                    String s = new StringBuilder(String.valueOf(str)).append("&version=").append(CS.version).toString();
                    try {
                        new DefaultHttpClient().execute(new HttpGet(s));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    LogUtil.i("info", new StringBuilder("\u5411\u670D\u52A1\u5668\u53D1\u9001\u7684\u8BF7\u6C42:").append(s).toString());
                }
            }
        }.start();
    }
    static String json = "";
    public Tools() {}
}
