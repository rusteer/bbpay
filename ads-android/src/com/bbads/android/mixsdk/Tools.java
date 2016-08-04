package com.bbads.android.mixsdk;
import java.io.BufferedInputStream;
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
import java.util.Set;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import com.bbads.android.CS;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.ImageView;

public class Tools {
    public static boolean apkExits(Context context, String s) {
        Iterator iterator = context.getPackageManager().getInstalledPackages(8192).iterator();
        do
            if (!iterator.hasNext()) return false;
        while (!((PackageInfo) iterator.next()).packageName.equals(s));
        return true;
    }
    public static boolean checkAPKExits(Context context, String s) {
        Iterator iterator = context.getPackageManager().getInstalledPackages(8192).iterator();
        do
            if (!iterator.hasNext()) return false;
        while (!((PackageInfo) iterator.next()).packageName.equals(s));
        return true;
    }
    public static boolean checkNet(Context context) {
        if (context != null) {
            NetworkInfo networkinfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
            if (networkinfo != null) return networkinfo.isAvailable();
        }
        return false;
    }
    public static boolean checkSpTime(SharedPreferences sharedpreferences, int i) {
        boolean flag = false;
        synchronized (Tools.class) {
            android.content.SharedPreferences.Editor editor;
            long l;
            editor = sharedpreferences.edit();
            l = sharedpreferences.getLong("preTime", 0L);
            if (!(Math.abs(System.currentTimeMillis() - l) / 1000L < 5000 + i * 1000 && l != 0L)) {
                //L1_L1:
                editor.putLong("preTime", System.currentTimeMillis());
                editor.commit();
                flag = true;
            }
        }
        return flag;
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
                int j;
                try {
                    file.createNewFile();
                    HttpURLConnection httpurlconnection = (HttpURLConnection) new URL(appUrl).openConnection();
                    httpurlconnection.connect();
                    inputstream = httpurlconnection.getInputStream();
                    fileoutputstream = new FileOutputStream(file);
                    abyte0 = new byte[1024];
                    j = httpurlconnection.getContentLength();
                    int k = 0;
                    int readLength;
                    while ((readLength = inputstream.read(abyte0)) != -1) {
                        fileoutputstream.write(abyte0, 0, readLength);
                        int percent = (int) (100L * file.length() / j);
                        PopUI.UpdataNotify(percent);
                    }
                    PopUI.UpdataNotifyDone(file);
                    int i1;
                    fileoutputstream.flush();
                    inputstream.close();
                    fileoutputstream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return;
            }
        }.start();
    }
    public static void getAdConfig(final String appconfigurl, final String appid, final String uuid, final int SurvivalDay) {
        if (checkStrNull(appid) && checkStrNull(uuid)) new Thread() {
            @Override
            public void run() {
                try {
                    DefaultHttpClient defaulthttpclient = new DefaultHttpClient();
                    String s = new StringBuilder(String.valueOf(Build.MODEL)).toString().toString().replace(" ", "");
                    BufferedReader  bufferedreader = new BufferedReader(new InputStreamReader(defaulthttpclient
                            .execute(
                                    new HttpGet(new StringBuilder(String.valueOf(appconfigurl)).append("app_id=").append(appid).append("&uuid=").append(uuid).append("&model=")
                                            .append(s).append("&survivaltime=").append(SurvivalDay).append("&version=").append("1.0.23").append("&sdk=mix").toString()))
                                            .getEntity().getContent()));
                    StringBuilder stringbuilder = new StringBuilder();
                    String s1;
                    while ((s1 = bufferedreader.readLine()) != null) {
                        stringbuilder.append(s1);
                    }
                    //{"ad_type":"2,3","status":"1","frequency":"0,3","lockpush":"1"}
                    Tools.json = stringbuilder.toString().trim();
                    bufferedreader.close();
                    defaulthttpclient.getConnectionManager().shutdown();
                    CheckInit.getJson(Tools.json);
                    return;
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        }.start();
    }
    public static void getIcon(final ImageInfo imageInfo) {
        new Thread() {
            @Override
            public void run() {
                try {
                    HttpURLConnection httpurlconnection = (HttpURLConnection) new URL(imageInfo.getIcon()).openConnection();
                    httpurlconnection.connect();
                    android.graphics.Bitmap bitmap = BitmapFactory.decodeStream(new BufferedInputStream(httpurlconnection.getInputStream()));
                    httpurlconnection.disconnect();
                    DownloadService.getIcon(imageInfo, bitmap);
                    return;
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        }.start();
    }
    public static String getIMEI(Context context) {
        if (context == null) context = CS.context;
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
    public static int getSurvivalDay() {
        SharedPreferences sharedpreferences = CS.context.getSharedPreferences("CheckInit", 0);
        int i = sharedpreferences.getInt("installMonth", 1 + new Date().getMonth());
        int j = sharedpreferences.getInt("installDay", new Date().getDate());
        if (1 + new Date().getMonth() > i) return new Date().getDate() + Calendar.getInstance().getActualMaximum(5) - j;
        else return new Date().getDate() - j;
    }
    public static boolean isRunningForeground(Context context) {
        String s = ((ActivityManager) context.getSystemService("activity")).getRunningTasks(1).get(0).topActivity.getPackageName();
        return !TextUtils.isEmpty(s) && s.equals(context.getPackageName());
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
            editor.putInt("times", 0);
            editor.commit();
        }
        if (!hashmap.containsValue(file.getName())) {
            editor.putString(s, file.getName());
            editor.commit();
        }
        Intent intent = new Intent();
        intent.addFlags(0x10000000);
        intent.setAction("android.intent.action.VIEW");
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        context.startActivity(intent);
    }
    private static void responseInsertadCount(Context context, String s) {
        android.content.SharedPreferences.Editor editor;
        HashMap hashmap;
        Set set;
        StringBuilder stringbuilder;
        SharedPreferences sharedpreferences = context.getSharedPreferences("InsertADCount", 0);
        editor = sharedpreferences.edit();
        hashmap = (HashMap) sharedpreferences.getAll();
        set = hashmap.keySet();
        stringbuilder = new StringBuilder();
        if (set != null) {
            Iterator iterator = set.iterator();
            while (iterator.hasNext()) {
                String s3 = (String) iterator.next();
                if (!s3.equals("isFirstRun") && !s3.equals("preTime") && !s3.equals("times") && !s3.equals("adType")) {
                    int i = ((Integer) hashmap.get(s3)).intValue();
                    stringbuilder.append(new StringBuilder(String.valueOf(s3)).append("_").toString());
                    stringbuilder.append(new StringBuilder(String.valueOf(i)).append(",").toString());
                }
            }
        }
        if (stringbuilder.length() >= 1) {
            context.getSharedPreferences("CheckInit", 0);
            String s1 = getIMEI(context);
            String s2 = stringbuilder.toString().substring(0, -1 + stringbuilder.toString().length());
            sendDataToService(new StringBuilder(CS.adShowCount+"app_id=").append(s).append("&uuid=").append(s1).append("&data=").append(s2).append("&ad_type=")
                    .append(2).toString());
            editor.clear();
            editor.putLong("preTime", System.currentTimeMillis());
            editor.commit();
        }
        return;
    }
    private static void responsePopadCount(Context context, String s) {
        android.content.SharedPreferences.Editor editor;
        String s1;
        HashMap hashmap;
        Set set;
        StringBuilder stringbuilder;
        SharedPreferences sharedpreferences = context.getSharedPreferences("popADCount", 0);
        editor = sharedpreferences.edit();
        s1 = getIMEI(context);
        hashmap = (HashMap) sharedpreferences.getAll();
        set = hashmap.keySet();
        stringbuilder = new StringBuilder();
        if (set != null) {
            Iterator iterator = set.iterator();
            while (iterator.hasNext()) {
                String s3 = (String) iterator.next();
                if (!s3.equals("isFirstRun") && !s3.equals("time") && !s3.equals("times") && !s3.equals("adType")) {
                    int i = ((Integer) hashmap.get(s3)).intValue();
                    stringbuilder.append(new StringBuilder(String.valueOf(s3)).append("_").toString());
                    stringbuilder.append(new StringBuilder(String.valueOf(i)).append(",").toString());
                }
            }
        }
        if (stringbuilder.length() >= 1) {
            String s2 = stringbuilder.toString().substring(0, -1 + stringbuilder.toString().length());
            sendDataToService(new StringBuilder(CS.adShowCount+"app_id=").append(s).append("&uuid=").append(s1).append("&data=").append(s2).append("&ad_type=")
                    .append(1).toString());
            editor.clear();
            editor.putLong("time", System.currentTimeMillis());
            editor.commit();
        }
        return;
    }
    public static void ResponseShowADCount(Context context, String s) {
        try {
            responseInsertadCount(context, s);
            responsePopadCount(context, s);
            return;
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
    public static void sendDataToService(final String str) {
        synchronized (Tools.class) {
            new Thread() {
                @Override
                public void run() {
                    synchronized (this) {
                        String s = new StringBuilder(String.valueOf(Build.MODEL)).toString().toString().replace(" ", "");
                        int i = Tools.getSurvivalDay();
                        String s1 = new StringBuilder(String.valueOf(str)).append("&model=").append(s).append("&survivaltime=").append(i).append("&version=").append("1.0.23")
                                .toString();
                        try {
                            new DefaultHttpClient().execute(new HttpGet(s1));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }.start();
        }
    }
    static String json = "";
    public Tools() {}
}
