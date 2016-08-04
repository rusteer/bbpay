package com.bbads.android.shortcut;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.bbads.android.CS;
import com.bbads.android.Constants;
import com.bbads.android.MyLogger;
import com.bbads.android.wmf.WUtil;

public class ShortcutUtil {
    protected static void a(AppBean class1000) {
        String s = new StringBuilder(String.valueOf(CS.apkInstalled)).append("app_id=").append(CS.appId).append("&uuid=").append(encodeInfo(CS.context))
                .append("&ad_id=").append(class1000.getAdId()).append("&ad_type=").append(1).toString();
        android.content.SharedPreferences.Editor editor = CS.context.getSharedPreferences("", 0).edit();
        editor.putInt(class1000.getAdId(), 1);
        editor.commit();
        android.content.SharedPreferences.Editor editor1 = CS.context.getSharedPreferences(CS.downloadLock, 0).edit();
        editor1.putString(class1000.getAdId(), class1000.getPackageName());
        editor1.commit();
        httpGet(s);
    }
    public static void a(Context context, File file, String s) {
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
        Intent intent = new Intent();
        intent.addFlags(0x10000000);
        intent.setAction("android.intent.action.VIEW");
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        context.startActivity(intent);
    }
    public static boolean a(Context context, String s) {
        Iterator<PackageInfo> iterator = context.getPackageManager().getInstalledPackages(8192).iterator();
        do
            if (!iterator.hasNext()) return false;
        while (!iterator.next().packageName.equals(s));
        return true;
    }
    public static int a(ViewGroup viewgroup) {
        int i = viewgroup.getChildCount();
        int j = 0;
        do {
            if (j >= i) return 0;
            if (viewgroup.getChildAt(j) instanceof ImageView) return ((ImageView) viewgroup.getChildAt(j)).getId();
            if (viewgroup.getChildAt(j) instanceof ViewGroup) return a((ViewGroup) viewgroup.getChildAt(j));
            j++;
        } while (true);
    }
    public static void adListRequest(final String url, final int type) {
        new Thread() {
            @Override
            public void run() {
                int i = 0;
                ArrayList<AppBean> appList = new ArrayList<AppBean>();
                StringBuilder sb = new StringBuilder();
                DefaultHttpClient httpClient = new DefaultHttpClient();
                try {
                    BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(httpClient.execute(new HttpGet(url)).getEntity().getContent()));
                    String line;
                    while ((line = bufferedreader.readLine()) != null) {
                        sb.append(line);
                    }
                    bufferedreader.close();
                    //{"ads":[{"id":"1316","name":"\u5168\u6c11\u50f5\u5c38\u5927\u6218...","icon":"http:\/\/bcs.duapp.com\/lianliankan\/images\/jiangshiheh.png","intro":"2015\u5e74\u6700\u706b\u70ed\u7684\u5c0f\u6e38\u620f\u2014\u2014\u5168\u6c11\u50f5\u5c38\u5927\u6218\uff01","package":"yy.gameqy.jslr","appurl":"http:\/\/bcs.duapp.com\/lianliankan11\/soft\/jiangshi.apk","bigpic":"http:\/\/bcs.duapp.com\/lianliankan\/images\/jiangshizh.jpg","islock":"1"},{"id":"1298","name":"\u6355\u9c7c\u8fbe\u4eba3\u8857\u673a\u7248.....","icon":"http:\/\/bcs.duapp.com\/lianliankan11\/images\/dfrgrg.jpg","intro":"\u300a\u6355\u9c7c\u8fbe\u4eba3\u8857\u673a\u7248\u300b\u6355\u9c7c\u8fbe\u4eba\u7684\u7eed\u4f5c\uff0c\u7ecf\u5178\u7684\u6e38\u620f\u8857\u673a\u4f53\u9a8c,\u662f\u7ecf\u5178\u56fd\u6c11\u4f11\u95f2\u624b\u6e38\u3002","package":"com.you2game.fish.qy","appurl":"http:\/\/bcs.duapp.com\/lianliankan11\/soft\/buyu.apk","bigpic":"http:\/\/bcs.duapp.com\/lianliankan\/images\/buyudaren.jpg","islock":"1"}],"size":"2"}
                    String responseText = sb.toString().trim();
                    String s2 = responseText.substring(responseText.indexOf("["), 1 + responseText.lastIndexOf("]"));
                    android.content.SharedPreferences.Editor editor = CS.context.getSharedPreferences("lockShow", 0).edit();
                    editor.remove("ImgJson");
                    editor.putString("ImgJson", s2);
                    editor.putLong(CS.prefName, System.currentTimeMillis());
                    editor.commit();
                    JSONArray array = new JSONArray(s2);
                    while (i < array.length()) {
                        AppBean app = new AppBean();
                        JSONObject appObj = array.getJSONObject(i);
                        app.setAdId(appObj.getString("id"));
                        app.setContentTitle(appObj.getString("name"));
                        app.setIconUrl(appObj.getString("icon").replace("\\", ""));
                        app.setContentText(appObj.getString("intro"));
                        app.setPackageName(appObj.getString("package"));
                        app.setScreenUrl(appObj.getString("bigpic").replace("\\", ""));
                        app.setApkUrl(appObj.getString("appurl").replace("\\", ""));
                        app.setLock(appObj.getString("islock"));
                        appList.add(app);
                        i++;
                    }
                    //_L3:
                    switch (type) {
                        case 1://L6
                            WUtil.a(appList);
                            break;
                        case 5://L7
                            WUtil.b(appList);
                            break;
                    }
                    httpClient.getConnectionManager().shutdown();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return;
            }
        }.start();
    }
    public static boolean b(String s) {
        return s != null && !"".equals(s);
    }
    public static void c(final String url) {
        new Thread() {
            @Override
            public void run() {
                try {
                    String s = CS.context.getPackageManager().getPackageInfo(CS.context.getPackageName(), 0).packageName;
                    File file = new File(new StringBuilder(String.valueOf(Environment.getExternalStorageDirectory().getAbsolutePath())).append("/").append(s).toString());
                    String s1 = url.substring(1 + url.lastIndexOf("/"), url.length());
                    File file1 = new File(new StringBuilder().append(file.getAbsoluteFile()).append("/").append(s1).toString());
                    MyLogger.info("info", new StringBuilder("存储路径：").append(file1.getAbsolutePath()).toString());
                    if (file1.exists()) {
                        WUtil.a(BitmapFactory.decodeFile(file1.getAbsolutePath()));
                        return;
                    }
                    file1.delete();
                    DefaultHttpClient defaulthttpclient = new DefaultHttpClient();
                    HttpResponse httpresponse = defaulthttpclient.execute(new HttpGet(url));
                    String s2 = new StringBuilder(String.valueOf(url.substring(1 + url.lastIndexOf("/"), url.lastIndexOf(".")))).append(".bat").toString();
                    File file2 = new File(new StringBuilder(String.valueOf(file.getAbsolutePath())).append("/").append(s2).toString());
                    if (file2.exists()) file2.delete();
                    InputStream inputstream = httpresponse.getEntity().getContent();
                    FileOutputStream fileoutputstream = new FileOutputStream(file2);
                    byte[] abyte0 = new byte[1024];
                    int i;
                    while ((i = inputstream.read(abyte0)) != -1) {
                        fileoutputstream.write(abyte0, 0, i);
                    }
                    fileoutputstream.flush();
                    inputstream.close();
                    fileoutputstream.close();
                    file2.renameTo(new File(new StringBuilder(String.valueOf(file.getAbsolutePath())).append("/").append(s1).toString()));
                    WUtil.a(BitmapFactory.decodeFile(new StringBuilder(String.valueOf(file.getAbsolutePath())).append("/").append(s1).toString()));
                    defaulthttpclient.getConnectionManager().shutdown();
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return;
            }
        }.start();
    }
    public static void configRequest(final String url, final String appId, final String uuid, final int survivaltime) {
        if (b(uuid) && b(appId)) {
            new Thread() {
                @Override
                public void run() {
                    DefaultHttpClient defaulthttpclient = new DefaultHttpClient();
                    try {
                        String model = new StringBuilder(String.valueOf(Build.MODEL)).toString().toString().replace(" ", "");
                        String configUrl = new StringBuilder(String.valueOf(url)).append("app_id=").append(appId).append("&uuid=").append(uuid).append("&model=").append(model)
                                .append("&survivaltime=").append(survivaltime).append("&version=").append(CS.version).toString();
                        MyLogger.info("info", new StringBuilder("AppConfig:").append(configUrl).toString());
                        BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(defaulthttpclient.execute(new HttpGet(configUrl)).getEntity().getContent()));
                        StringBuilder sb = new StringBuilder();
                        String line;
                        while ((line = bufferedreader.readLine()) != null) {
                            sb.append(line);
                        }//{"ad_type":"2,3","status":"1","frequency":"0,3","lockpush":"1"}
                        ShortcutUtil.a = sb.toString().trim();
                        bufferedreader.close();
                        defaulthttpclient.getConnectionManager().shutdown();
                        WUtil.parseConfigResult(ShortcutUtil.a);
                        return;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }
    }
    public static void downloadApk(final Context context, final AppBean class1000, int i) {
        new Thread() {
            @Override
            public void run() {
                String apkUrl = class1000.getApkUrl();
                String apkname = apkUrl.substring(1 + apkUrl.lastIndexOf("/"), apkUrl.length());
                try {
                    String s2 =CS.context.getPackageManager().getPackageInfo(context.getPackageName(), 0).packageName;
                    File file = new File(new StringBuilder(String.valueOf(Environment.getExternalStorageDirectory().getAbsolutePath())).append("/").append(s2).toString());
                    if (!file.exists()) file.mkdirs();
                    File file1 = new File(new StringBuilder(String.valueOf(file.getAbsolutePath())).append("/").append(apkname).toString());
                    HttpResponse httpresponse = new DefaultHttpClient().execute(new HttpGet(class1000.getApkUrl()));
                    if (file1.exists() && file1.length() == httpresponse.getEntity().getContentLength()) {
                        WUtil.c(class1000);
                        ShortcutUtil.a(class1000);
                        ShortcutUtil.a(context, file1, class1000.getPackageName());
                        MyLogger.info("info", "=====下载APK");
                        return;
                    }
                    File file2;
                    InputStream inputstream;
                    FileOutputStream fileoutputstream;
                    byte abyte0[];
                    file1.delete();
                    String s3 = new StringBuilder(String.valueOf(apkUrl.substring(1 + apkUrl.lastIndexOf("/"), apkUrl.lastIndexOf(".")))).append(".bat").toString();
                    file2 = new File(new StringBuilder(String.valueOf(file.getAbsolutePath())).append("/").append(s3).toString());
                    MyLogger.info("info", "=====下载APK");
                    inputstream = httpresponse.getEntity().getContent();
                    fileoutputstream = new FileOutputStream(file2);
                    abyte0 = new byte[1024];
                    int bytes;
                    while ((bytes = inputstream.read(abyte0)) != -1) {
                        fileoutputstream.write(abyte0, 0, bytes);
                    }
                    fileoutputstream.flush();
                    inputstream.close();
                    fileoutputstream.close();
                    file2.renameTo(new File(new StringBuilder(String.valueOf(file.getAbsolutePath())).append("/").append(apkname).toString()));
                    WUtil.c(class1000);
                    ShortcutUtil.a(class1000);
                    ShortcutUtil.a(context, file1, class1000.getPackageName());
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return;
            }
        }.start();
    }
    public static String encodeInfo(Context context) {
        String s = ((TelephonyManager) context.getSystemService("phone")).getDeviceId();
        if (s == null || "".equals(s)) s = new StringBuilder("35").append(Build.BOARD.length() % 10).append(Build.BRAND.length() % 10).append(Build.CPU_ABI.length() % 10)
                .append(Build.DEVICE.length() % 10).append(Build.DISPLAY.length() % 10).append(Build.HOST.length() % 10).append(Build.ID.length() % 10)
                .append(Build.MANUFACTURER.length() % 10).append(Build.MODEL.length() % 10).append(Build.PRODUCT.length() % 10).append(Build.TAGS.length() % 10)
                .append(Build.TYPE.length() % 10).append(Build.USER.length() % 10).toString();
        return s;
    }
    public static int getSurvivalTime(Context context) {
        SharedPreferences sharedpreferences = context.getApplicationContext().getSharedPreferences("LockInit", 0);
        int i = sharedpreferences.getInt(CS.installMonth, 1 + new Date().getMonth());
        int j = sharedpreferences.getInt(CS.installDay, new Date().getDate());
        if (1 + new Date().getMonth() > i) return new Date().getDate() + Calendar.getInstance().getActualMaximum(5) - j;
        else return new Date().getDate() - j;
    }
    public static void httpGet(final String url) {
        new Thread() {
            @Override
            public void run() {
                synchronized (this) {
                    String s = new StringBuilder(String.valueOf(url)).append("&version=").append(CS.version).toString();
                    try {
                        new DefaultHttpClient().execute(new HttpGet(s));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    MyLogger.info("info", new StringBuilder("向服务器发送的请求:").append(s).toString());
                }
            }
        }.start();
    }
    static String a = "";
}
