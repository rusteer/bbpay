package com.bbads.android.wmf;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.bbads.android.CS;
import com.bbads.android.MyLogger;
import com.bbads.android.shortcut.AppBean;
import com.bbads.android.shortcut.ShortcutUtil;

public class WUtil {
    protected static void a(AppBean bean) {
        SharedPreferences sharedpreferences = mContext.getSharedPreferences(CS.lockscreenADCount, 0);
        android.content.SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putInt(bean.getAdId(), 1 + sharedpreferences.getInt(bean.getAdId(), 0));
        editor.commit();
        if (sharedpreferences.getBoolean("isFirstRun", true)) {
            editor.putBoolean("isFirstRun", false);
            editor.putLong("preTime", System.currentTimeMillis());
            editor.commit();
        } else {
            long l = sharedpreferences.getLong("preTime", 0L);
            if (Math.abs((System.currentTimeMillis() - l) / 60000L) >= 10L) {
                editor.putLong("preTime", System.currentTimeMillis());
                editor.commit();
                j();
                return;
            }
        }
    }
    static void a(AppBean class1000, int l) {
        b(class1000, l);
    }
    protected static void a(AppBean class1000, String s) {
        class1000.setPathName(s);
        Message message = Message.obtain();
        message.what = 4;
        message.obj = class1000;
        mHandler.sendMessage(message);
    }
    public static void a(ArrayList<AppBean> arraylist) {
        Message message = Message.obtain();
        message.what = 2;
        message.obj = arraylist;
        mHandler.sendMessage(message);
    }
    public static void a(Bitmap bitmap) {
        Message message = Message.obtain();
        message.what = 3;
        message.obj = bitmap;
        mHandler.sendMessage(message);
    }
    public static void a(Context context) {
        context.startService(new Intent(context, LService.class));
        mContext = context;
        CS.context = context;
        SharedPreferences sharedpreferences = mContext.getSharedPreferences("lockShow", 0);
        android.content.SharedPreferences.Editor editor = sharedpreferences.edit();
        if (sharedpreferences.getBoolean("isFirst", true)) {
            MyLogger.info(c, "lockShow_FIRST");
            editor.putBoolean("isFirst", false);
            editor.putLong("preTime", System.currentTimeMillis());
            editor.putInt("times", 0);
            editor.remove("appid");
            editor.putString("appid", CS.appId);
            editor.putInt(CS.installMonth, 1 + new Date().getMonth());
            editor.putInt(CS.installDay, new Date().getDate());
            editor.commit();
            requestAdList3();
        }
        int l = sharedpreferences.getInt("times", 0);
        long l1 = (System.currentTimeMillis() - sharedpreferences.getLong("preTime", 0L)) / 60000L;
        if (l == 0 || Math.abs(l1) >= 10L) {
            editor.putInt("times", l + 1);
            editor.commit();
            editor.putLong("preTime", System.currentTimeMillis());
            editor.commit();
            MyLogger.info("info", new StringBuilder("times：").append(l).append("或者间隔小于").append(l1).append("分钟").toString());
            ShortcutUtil.configRequest(CS.appconfig, CS.appId, ShortcutUtil.encodeInfo(mContext), ShortcutUtil.getSurvivalTime(context));
            return;
        } else {
            parseConfigResult(sharedpreferences.getString("appconfig", ""));
            return;
        }
    }
    public static void a(Context context, RelativeLayout relativelayout) {
        if (context != null) {
            CS.context = context;
            mContext = context;
            mRelateLayout = relativelayout;
            initHandler();
            mHandler.sendEmptyMessage(1);
        }
    }
    protected static void b(AppBean class1000) {
        File file1;
        Notification notification;
        String pathName = class1000.getPathName();
        String s1 = class1000.getApkUrl();
        String s2 = s1.substring(1 + s1.lastIndexOf("/"), s1.length());
        MyLogger.info("info", new StringBuilder("下载：").append(s1).toString());
        try {
            String s3 = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).packageName;
            File file = new File(new StringBuilder(String.valueOf(Environment.getExternalStorageDirectory().getAbsolutePath())).append("/").append(s3).toString());
            file1 = new File(new StringBuilder().append(file).append("/").append(s2).toString());
            MyLogger.info("info", "changeNtcDone");
            contentTitle = new StringBuilder(String.valueOf(class1000.getContentTitle())).append("下载完毕,点击安装").toString();
            contentText = class1000.getContentText();
            notification = new Notification(0x108008f, class1000.getContentText(), System.currentTimeMillis());
            notification.flags = 2;
            if (!mContext.getSharedPreferences("lockShow", 0).getString(CS.ah, "").equals("0")) {
                notification.flags = 16;
            } else {
                notification.flags = 32;
            }
            notification.flags = 2;
            Intent intent = new Intent();
            intent.addFlags(0x10000000);
            intent.setAction("android.intent.action.VIEW");
            intent.setDataAndType(Uri.fromFile(file1), "application/vnd.android.package-archive");
            PendingIntent pendingintent = PendingIntent.getActivity(mContext, 0, intent, 0);
            notification.setLatestEventInfo(mContext, contentTitle, contentText, pendingintent);
            LinearLayout linearlayout = new LinearLayout(mContext.getApplicationContext());
            int l = ShortcutUtil.a((ViewGroup) notification.contentView.apply(mContext.getApplicationContext(), linearlayout));
            notification.contentView.setImageViewBitmap(l, BitmapFactory.decodeFile(pathName));
            ((NotificationManager) mContext.getSystemService("notification")).notify(Integer.parseInt(class1000.getAdId()), notification);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return;
    }
    private static void b(AppBean class1000, int adType) {
        String s = ShortcutUtil.encodeInfo(CS.context);
        ShortcutUtil.httpGet(new StringBuilder(String.valueOf(CS.appClicked)).append("app_id=").append(CS.appId).append("&uuid=").append(s).append("&ad_id=")
                .append(class1000.getAdId()).append("&ad_type=").append(adType).toString());
    }
    public static void b(final ArrayList<AppBean> appList) {
        try {
            String s = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).packageName;
            MyLogger.info(c, s);
            final File paramFile = new File(new StringBuilder(String.valueOf(Environment.getExternalStorageDirectory().getAbsolutePath())).append("/").append(s).toString());
            if (!paramFile.exists()) paramFile.mkdirs();
            new Thread() {
                @Override
                public void run() {
                    int size = appList.size();
                    int j;
                    if (size < 5) j = 1;
                    else j = size / 5;
                    if (size == 0 || j == 0) return;
                    MyLogger.info("info", new StringBuilder("Size:").append(size).toString());
                    int k = ShortcutUtil.getSurvivalTime(WUtil.methodE()) % j;
                    DefaultHttpClient client = new DefaultHttpClient();
                    int l = k * 5 % size;
                    int length;
                    int i1;
                    if (5 + k * 5 % size > size) i1 = size;
                    else i1 = 5 + k * 5 % size;
                    while (l < i1) {
                        try {
                            String s = appList.get(l).getScreenUrl();
                            String s1 = s.substring(1 + s.lastIndexOf("/"), s.length());
                            MyLogger.info(WUtil.g(), s1);
                            MyLogger.info(WUtil.g(), appList.get(l).getScreenUrl());
                            File file = new File(new StringBuilder(String.valueOf(paramFile.getAbsolutePath())).append("/").append(s1).toString());
                            HttpResponse httpresponse = client.execute(new HttpGet(appList.get(l).getScreenUrl()));
                            InputStream inputstream = httpresponse.getEntity().getContent();
                            if (file.length() != httpresponse.getEntity().getContentLength()) {
                                file.delete();
                                String s2 = new StringBuilder(String.valueOf(s1.substring(1 + s1.lastIndexOf("/"), s1.lastIndexOf(".")))).append(".bat").toString();
                                File file1 = new File(new StringBuilder(String.valueOf(paramFile.getAbsolutePath())).append("/").append(s2).toString());
                                if (file1.exists()) file1.delete();
                                FileOutputStream fileoutputstream = new FileOutputStream(file1);
                                byte[] abyte0 = new byte[1024];
                                while ((length = inputstream.read(abyte0)) != -1) {
                                    fileoutputstream.write(abyte0, 0, length);
                                }
                                try {
                                    inputstream.close();
                                    fileoutputstream.close();
                                    file1.renameTo(new File(new StringBuilder(String.valueOf(paramFile.getAbsolutePath())).append("/").append(s1).toString()));
                                } catch (ClientProtocolException clientprotocolexception) {
                                    clientprotocolexception.printStackTrace();
                                } catch (IOException ioexception) {
                                    ioexception.printStackTrace();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        l++;
                    }
                    client.getConnectionManager().shutdown();
                }
            }.start();
            return;
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
    public static void b(Context context) {
        Intent intent = new Intent(context, LActivity.class);
        intent.setFlags(0x10000000);
        context.startActivity(intent);
    }
    public static void c(final AppBean param1) {
        try {
            String s = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).packageName;
            MyLogger.info(c, s);
            final File param2 = new File(new StringBuilder(String.valueOf(Environment.getExternalStorageDirectory().getAbsolutePath())).append("/").append(s).toString());
            if (!param2.exists()) param2.mkdirs();
            new Thread() {
                @Override
                public void run() {
                    DefaultHttpClient client = new DefaultHttpClient();
                    String url = param1.getIconUrl();
                    String s1 = url.substring(1 + url.lastIndexOf("/"), url.length());
                    MyLogger.info(WUtil.g(), s1);
                    MyLogger.info(WUtil.g(), param1.getIconUrl());
                    File file = new File(new StringBuilder(String.valueOf(param2.getAbsolutePath())).append("/").append(s1).toString());
                    try {
                        HttpResponse httpresponse = client.execute(new HttpGet(param1.getIconUrl()));
                        InputStream inputstream = httpresponse.getEntity().getContent();
                        if (file.length() != httpresponse.getEntity().getContentLength()) {
                            byte abyte0[];
                            file.delete();
                            String path = new StringBuilder(String.valueOf(s1.substring(1 + s1.lastIndexOf("/"), s1.lastIndexOf(".")))).append(".bat").toString();
                            File file1 = new File(new StringBuilder(String.valueOf(param2.getAbsolutePath())).append("/").append(path).toString());
                            if (file1.exists()) file1.delete();
                            FileOutputStream outStream = new FileOutputStream(file1);
                            abyte0 = new byte[1024];
                            int i;
                            while ((i = inputstream.read(abyte0)) != -1) {
                                outStream.write(abyte0, 0, i);
                            }
                            try {
                                inputstream.close();
                                outStream.close();
                                file1.renameTo(new File(new StringBuilder(String.valueOf(param2.getAbsolutePath())).append("/").append(s1).toString()));
                                WUtil.a(param1, new StringBuilder(String.valueOf(param2.getAbsolutePath())).append("/").append(s1).toString());
                            } catch (ClientProtocolException clientprotocolexception) {
                                clientprotocolexception.printStackTrace();
                            } catch (IOException ioexception) {
                                ioexception.printStackTrace();
                            }
                        } else {
                            WUtil.a(param1, file.getAbsolutePath());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    client.getConnectionManager().shutdown();
                    return;
                }
            }.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    static RelativeLayout f() {
        return mRelateLayout;
    }
    static String g() {
        return c;
    }
    static ArrayList<AppBean> getAppList() {
        return appList;
    }
    private static void initHandler() {
        mHandler = new Handler() {
            private int randomIndex;
            @Override
            public void handleMessage(Message message) {
                switch (message.what) {
                    default:
                        return;
                    case 1: // '\001'
                        methodA();
                        return;
                    case 2: // '\002'
                        setAppList((ArrayList) message.obj);
                        int i = getAppList().size();
                        int j;
                        if (i < 5) j = 1;
                        else j = i / 5;
                        if (i != 0 && j != 0) {
                            int k = ShortcutUtil.getSurvivalTime(methodE()) % j;
                            new DefaultHttpClient();
                            Random random = new Random();
                            int l;
                            if (5 + k * 5 % i > i) l = i;
                            else l = 5 + k * 5 % i;
                            randomIndex = random.nextInt(l) + k * 5 % i;
                            ShortcutUtil.c(getAppList().get(randomIndex).getScreenUrl());
                            a(getAppList().get(randomIndex));
                            return;
                        } else {
                            LActivity.b();
                            return;
                        }
                    case 3: // '\003'
                        Bitmap bitmap = (Bitmap) message.obj;
                        new ImageView(methodE()).setImageBitmap(bitmap);
                        BitmapDrawable bitmapdrawable = new BitmapDrawable(bitmap);
                        f().setBackgroundDrawable(bitmapdrawable);
                        f().setOnClickListener(new android.view.View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ((Activity) methodE()).finish();
                                a(getAppList().get(randomIndex), 1);
                                ShortcutUtil.downloadApk(methodE(), getAppList().get(randomIndex), 1);
                            }
                        });
                        return;
                    case 4: // '\004'
                        b((AppBean) message.obj);
                        return;
                }
            }
        };
    }
    private static void j() {
        SharedPreferences sharedpreferences = mContext.getSharedPreferences(CS.lockscreenADCount, 0);
        android.content.SharedPreferences.Editor editor = sharedpreferences.edit();
        HashMap hashmap = (HashMap) sharedpreferences.getAll();
        MyLogger.info("info", new StringBuilder("map:").append(hashmap.toString()).toString());
        Set set = hashmap.keySet();
        StringBuilder stringbuilder = new StringBuilder();
        if (set != null) {
            Iterator iterator = set.iterator();
            while (iterator.hasNext()) {
                String s3 = (String) iterator.next();
                if (!s3.equals("isFirstRun") && !s3.equals("time") && !s3.equals("times") && !s3.equals("adType") && !s3.equals("preTime")) {
                    int l = ((Integer) hashmap.get(s3)).intValue();
                    stringbuilder.append(new StringBuilder(String.valueOf(s3)).append("_").toString());
                    stringbuilder.append(new StringBuilder(String.valueOf(l)).append(",").toString());
                }
            }
        }
        if (stringbuilder.length() >= 1) {
            String s = stringbuilder.toString().substring(0, -1 + stringbuilder.toString().length());
            MyLogger.info("info", new StringBuilder("data:").append(s).toString());
            String s1 = CS.appId;
            String s2 = ShortcutUtil.encodeInfo(mContext);
            ShortcutUtil.httpGet(new StringBuilder(String.valueOf(CS.adShowCount)).append("app_id=").append(s1).append("&uuid=").append(s2).append("&data=").append(s)
                    .append("&ad_type=").append(1).toString());
            editor.clear();
            editor.commit();
            MyLogger.info("info", "返回展示响应");
        }
        return;
    }
    protected static void methodA() {
        int l = 0;
        String s = CS.context.getSharedPreferences("lockShow", 0).getString("ImgJson", "");
        if (s == null || s.equals("")) {
            MyLogger.info("info", "重新获取连接");
            requestAdList1();
            return;
        }
        try {
            JSONArray jsonarray = new JSONArray(s);
            ArrayList<AppBean> arraylist = new ArrayList<AppBean>();
            while (l < jsonarray.length()) {
                try {
                    AppBean app = new AppBean();
                    JSONObject jsonobject = jsonarray.getJSONObject(l);
                    app.setAdId(jsonobject.getString("id"));
                    app.setContentTitle(jsonobject.getString("name"));
                    app.setIconUrl(jsonobject.getString("icon").replace("\\", ""));
                    app.setContentText(jsonobject.getString("intro"));
                    app.setPackageName(jsonobject.getString("package"));
                    app.setScreenUrl(jsonobject.getString("bigpic").replace("\\", ""));
                    app.setApkUrl(jsonobject.getString("appurl").replace("\\", ""));
                    app.setLock(jsonobject.getString("islock"));
                    arraylist.add(app);
                } catch (JSONException jsonexception) {
                    jsonexception.printStackTrace();
                    return;
                }
                l++;
            }
            Message message = Message.obtain();
            message.what = 2;
            message.obj = arraylist;
            mHandler.sendMessage(message);
            return;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    static Context methodE() {
        return mContext;
    }
    public static void parseConfigResult(String s) {
        String s1;
        String s2;
        String s3;
        try {
            JSONObject jsonobject = new JSONObject(s);
            s1 = jsonobject.get("ad_type").toString().replace(",", "").trim();
            s2 = jsonobject.getString("status");
            s3 = jsonobject.getString("lockpush");
            MyLogger.info(c, new StringBuilder("adConfig:").append(s).toString());
            if (s2.equals("1") && s1.contains("1")) {
                android.content.SharedPreferences.Editor editor1 = mContext.getSharedPreferences("lockShow", 0).edit();
                editor1.remove("lockpush");
                editor1.putString("lockpush", s3);
                editor1.remove("appconfig");
                editor1.putString("appconfig", s3);
                editor1.remove("status");
                editor1.putString("status", "1");
                editor1.commit();
                return;
            }
            if (s2.equals("0") && s1.contains("1")) {
                android.content.SharedPreferences.Editor editor = mContext.getSharedPreferences("lockShow", 0).edit();
                editor.remove("lockpush");
                editor.putString("lockpush", s3);
                editor.remove("appconfig");
                editor.putString("appconfig", s3);
                editor.remove("status");
                editor.putString("status", "0");
                editor.commit();
                return;
            }
        } catch (Exception exception) {}
        return;
    }
    protected static void requestAdList1() {
        String s = CS.appId;
        String s1 = ShortcutUtil.encodeInfo(mContext);
        int l = ShortcutUtil.getSurvivalTime(mContext);
        String s2 = new StringBuilder(String.valueOf(Build.MODEL)).toString().toString().replace(" ", "");
        String s3 = new StringBuilder(String.valueOf(CS.getadlist)).append("app_id=").append(s).append("&uuid=").append(s1).append("&data_type=").append(CS.adType)
                .append("&ad_type=").append(1).append("&model=").append(s2).append("&survivaltime=").append(l).append("&apiVersion=").append(android.os.Build.VERSION.RELEASE)
                .append("&version=").append(CS.version).toString();
        MyLogger.info("info", new StringBuilder("imgPath:").append(s3).toString());
        if (ShortcutUtil.b(s)) ShortcutUtil.adListRequest(s3, 1);
    }
    public static void requestAdList2() {
        SharedPreferences sharedpreferences = CS.context.getSharedPreferences("lockShow", 0);
        android.content.SharedPreferences.Editor editor = sharedpreferences.edit();
        long l = sharedpreferences.getLong(CS.prefName, 0L);
        if (Math.abs((System.currentTimeMillis() - l) / 0x36ee80L) >= 12L) {
            editor.putLong(CS.prefName, System.currentTimeMillis());
            editor.commit();
            String s = CS.appId;
            String s1 = ShortcutUtil.encodeInfo(mContext);
            int i1 = ShortcutUtil.getSurvivalTime(mContext);
            String s2 = new StringBuilder(String.valueOf(Build.MODEL)).toString().toString().replace(" ", "");
            String s3 = new StringBuilder(String.valueOf(CS.getadlist)).append("app_id=").append(s).append("&uuid=").append(s1).append("&data_type=").append(CS.adType)
                    .append("&ad_type=").append(1).append("&model=").append(s2).append("&survivaltime=").append(i1).append("&apiVersion=").append(android.os.Build.VERSION.RELEASE)
                    .append("&version=").append(CS.version).toString();
            MyLogger.info("info", new StringBuilder("imgPath:").append(s3).toString());
            if (ShortcutUtil.b(s)) ShortcutUtil.adListRequest(s3, 5);
        }
    }
    private static void requestAdList3() {
        String appId = CS.appId;
        String uuid = ShortcutUtil.encodeInfo(mContext);
        int survivalTime = ShortcutUtil.getSurvivalTime(mContext);
        String model = new StringBuilder(String.valueOf(Build.MODEL)).toString().toString().replace(" ", "");
        String url = new StringBuilder(String.valueOf(CS.getadlist)).append("app_id=").append(appId).append("&uuid=").append(uuid).append("&data_type=").append(CS.adType)
                .append("&ad_type=").append(1).append("&model=").append(model).append("&survivaltime=").append(survivalTime).append("&apiVersion=")
                .append(android.os.Build.VERSION.RELEASE).append("&version=").append(CS.version).toString();
        MyLogger.info("info", new StringBuilder("imgPath:").append(url).toString());
        if (ShortcutUtil.b(appId)) ShortcutUtil.adListRequest(url, 5);
    }
    static void setAppList(ArrayList<AppBean> arraylist) {
        appList = arraylist;
    }
    private static Context mContext;
    private static Handler mHandler;
    private static String c = "info";
    private static ArrayList<AppBean> appList;
    private static RelativeLayout mRelateLayout;
    private static String contentText;
    private static String contentTitle;
}
