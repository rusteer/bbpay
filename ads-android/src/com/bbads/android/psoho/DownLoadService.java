package com.bbads.android.psoho;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.bbads.android.CS;

public class DownLoadService extends Service {
    public class DownLoadApk extends Thread {
        private int adType;
        private ImageInfo imageInfo;
        public DownLoadApk(ImageInfo imageinfo, int i) {
            imageInfo = imageinfo;
            adType = i;
        }
        @Override
        public void run() {
            int i;
            String s;
            File file;
            i = 0;
            s = imageInfo.getAppurl();
            String s1 = s.substring(1 + s.lastIndexOf("/"), s.length());
            LogUtil.i("info", new StringBuilder("\u4E0B\u8F7D\uFF1A").append(s).toString());
            file = new File(new StringBuilder("sdcard/").append(s1).toString());
            try {
                HttpURLConnection httpurlconnection;
                httpurlconnection = (HttpURLConnection) new URL(s).openConnection();
                httpurlconnection.connect();
                if (file.exists() && file.length() == httpurlconnection.getContentLength()) {
                    downloaded(file, adType);
                    return;
                }
                InputStream inputstream;
                FileOutputStream fileoutputstream;
                byte abyte0[];
                int j;
                file.delete();
                LogUtil.i("info", "file.exists()_nm.cancel");
                file.createNewFile();
                LogUtil.i("info", "=====\u4E0B\u8F7DAPK");
                inputstream = httpurlconnection.getInputStream();
                fileoutputstream = new FileOutputStream(file);
                abyte0 = new byte[1024];
                j = httpurlconnection.getContentLength();
                int k;
                while ((k = inputstream.read(abyte0)) != -1) {
                    int l;
                    fileoutputstream.write(abyte0, 0, k);
                    l = (int) (100L * file.length() / j);
                    if (!(i == l || l >= 100)) {
                        changeNtf(imageInfo, l);
                        i = l;
                    }
                }
                fileoutputstream.flush();
            inputstream.close();
            fileoutputstream.close();
            httpurlconnection.disconnect();
            downloaded(file, adType);
            return;
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return;
        }
    }
    public static void getAD(Context context1, String s) {
        context = context1;
        Message message = Message.obtain();
        message.what = 2;
        message.obj = s;
        handler.sendMessage(message);
    }
    public static void getIcon(ImageInfo imageinfo, Bitmap bitmap) {
        Message message = Message.obtain();
        message.obj = bitmap;
        iconImginfo = imageinfo;
        message.what = 3;
        handler.sendMessage(message);
    }
    public static void ReDownLoading() {
        if (handler != null) handler.sendEmptyMessage(1);
    }
    public static void StopDownLoading() {
        if (handler != null) handler.sendEmptyMessage(2);
    }
    private static Context context;
    private static Handler handler;
    private static ImageInfo iconImginfo;
    private ArrayList downloading;
    private ImageInfo imageInfo;
    private NotificationManager nm;
    public DownLoadService() {
        downloading = new ArrayList();
    }
    private void changeNtf(ImageInfo imageinfo, int i) {
        String s = imageinfo.getName();
        String s1 = new StringBuilder(String.valueOf(i)).append("%").toString();
        Notification notification = new Notification(0x1080081, imageinfo.getIntro(), System.currentTimeMillis());
        notification.flags = 2;
        notification.flags = 16;
        Intent intent = new Intent();
        intent.addFlags(0x20000000);
        PendingIntent pendingintent = PendingIntent.getActivity(context, 0, intent, 0);
        notification.setLatestEventInfo(context, s, new StringBuilder(String.valueOf("\u5DF2\u4E0B\u8F7D")).append(s1).toString(), pendingintent);
        ((NotificationManager) context.getSystemService("notification")).notify(Integer.parseInt(imageinfo.getId()), notification);
    }
    public void ChangeNtfDone(ImageInfo imageinfo, Bitmap bitmap) {
        String s = imageinfo.getAppurl();
        String s1 = s.substring(1 + s.lastIndexOf("/"), s.length());
        LogUtil.i("info", new StringBuilder("\u4E0B\u8F7D\uFF1A").append(s).toString());
        File file = new File(new StringBuilder("sdcard/").append(s1).toString());
        LogUtil.i("info", "changeNtcDone");
        String s2 = new StringBuilder(String.valueOf(imageinfo.getName())).append("\u4E0B\u8F7D\u5B8C\u6BD5,\u70B9\u51FB\u5B89\u88C5").toString();
        String s3 = imageinfo.getIntro();
        Notification notification = new Notification(0x108008f, imageinfo.getIntro(), System.currentTimeMillis());
        notification.flags = 2;
        notification.flags = 32;
        if (file.exists() && notification != null) {
            Intent intent = new Intent();
            intent.addFlags(0x10000000);
            intent.setAction("android.intent.action.VIEW");
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            PendingIntent pendingintent = PendingIntent.getActivity(context, 0, intent, 0);
            notification.setLatestEventInfo(context, s2, s3, pendingintent);
            LinearLayout linearlayout = new LinearLayout(context.getApplicationContext());
            if (bitmap != null) {
                int i = Tools.getNFid((ViewGroup) notification.contentView.apply(context.getApplicationContext(), linearlayout));
                notification.contentView.setImageViewBitmap(i, bitmap);
            } else {
                ImgUtil.getIcon(imageInfo);
            }
            ((NotificationManager) context.getSystemService("notification")).notify(Integer.parseInt(imageinfo.getId()), notification);
        }
    }
    private void createHandler() {
        LogUtil.i("info", "createHandler");
        handler = new Handler() {
            @Override
            public void handleMessage(Message message) {
                switch (message.what) {
                    case 1://L2_L2:
                        SharedPreferences sharedpreferences = getSharedPreferences("pushDownLoadingSp", 0);
                        android.content.SharedPreferences.Editor editor = sharedpreferences.edit();
                        if (((HashMap) sharedpreferences.getAll()).size() > 0 && imageInfo != null) {
                            editor.putBoolean(imageInfo.getId(), true);
                            editor.commit();
                            new DownLoadApk(imageInfo, 3).start();
                            return;
                        }
                        break;
                    case 2://L3_L3:
                        String s = (String) message.obj;
                        downloadAD(DownLoadService.context, s);
                        break;
                    case 3://L4_L4:
                        Bitmap bitmap = (Bitmap) message.obj;
                        ChangeNtfDone(DownLoadService.iconImginfo, bitmap);
                        break;
                }
            }
        };
    }
    public void downloadAD(Context context1, String s) {
        Iterator iterator;
        context = context1;
        LogUtil.i("info", "DownLoadService_downloadAD()");
        iterator = getPushList().iterator();
        while (iterator.hasNext()) {
            ImageInfo imageinfo = (ImageInfo) iterator.next();
            if (s.equals(imageinfo.getId())) {
                //L5_L5:
                LogUtil.i("info", new StringBuilder("getImg:").append(imageinfo.getId()).toString());
                imageInfo = imageinfo;
                break;
            }
        }
        if (imageInfo == null) {
            ((NotificationManager) context.getSystemService("notification")).cancel(Integer.parseInt(s));
        } else if (!downloading.contains(imageInfo.getId())) {
            LogUtil.i("info", new StringBuilder("imgInfo:").append(imageInfo.getId()).toString());
            downloading.add(imageInfo.getId());
            int i = Tools.getSurvivalDay(context);
            String s1 = new StringBuilder(String.valueOf(Build.MODEL)).toString().toString().replace(" ", "");
            String s2 = context.getApplicationContext().getSharedPreferences("pushShow", 0).getString("appid", "");
            String s3 = Tools.getIMEI(context);
            String s4 = new StringBuilder(CS.appClicked+"app_id=").append(s2).append("&uuid=").append(s3).append("&ad_id=").append(imageInfo.getId())
                    .append("&ad_type=").append(3).append("&model=").append(s1).append("&survivaltime=").append(i).append("&apiVersion=").append(android.os.Build.VERSION.RELEASE)
                    .toString();
            changeNtf(imageInfo, 0);
            Tools.sendDataToService(s4);
            downLoadAPK(imageInfo);
        }
    }
    private void downLoadAPK(ImageInfo imageinfo) {
        SharedPreferences sharedpreferences = context.getSharedPreferences("pushDownLoadingSp", 0);
        android.content.SharedPreferences.Editor editor = sharedpreferences.edit();
        SharedPreferences sharedpreferences1 = context.getSharedPreferences("pushDLPakNameSp", 0);
        android.content.SharedPreferences.Editor editor1 = sharedpreferences1.edit();
        if (sharedpreferences1.getString(imageinfo.getId(), "").equals("")) {
            editor1.putString(imageinfo.getId(), imageinfo.getPackageName());
            editor1.commit();
        }
        if (!Boolean.valueOf(sharedpreferences.getBoolean(imageinfo.getId(), false)).booleanValue()) {
            editor.putBoolean(imageinfo.getId(), true);
            editor.commit();
            new DownLoadApk(imageinfo, 3).start();
        }
    }
    public void downloaded(File file, int i) {
        SharedPreferences sharedpreferences = context.getSharedPreferences("pushDownLoadingSp", 0);
        if (sharedpreferences.getBoolean(imageInfo.getId(), false)) {
            String s = context.getSharedPreferences("pushShow", 0).getString("appid", "");
            String s1 = Tools.getIMEI(context);
            Tools.sendDataToService(new StringBuilder(CS.apkDownLoaded+"app_id=").append(s).append("&uuid=").append(s1).append("&ad_id=")
                    .append(imageInfo.getId()).append("&ad_type=").append(i).toString());
            ChangeNtfDone(imageInfo, null);
        }
        android.content.SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.remove(imageInfo.getId());
        editor.commit();
        downloading.remove(imageInfo.getId());
        Tools.openFile(context, file, imageInfo.getPackageName());
    }
    private ArrayList getPushList() {
        int i;
        String s;
        ArrayList arraylist;
        i = 0;
        LogUtil.i("info", "getPushList");
        s = context.getSharedPreferences("pushShow", 0).getString("ImgJson", "");
        arraylist = new ArrayList();
        try {
            JSONArray jsonarray = new JSONArray(s);
            while (i < jsonarray.length()) {
                ImageInfo imageinfo = new ImageInfo();
                JSONObject jsonobject = jsonarray.getJSONObject(i);
                imageinfo.setId(jsonobject.getString("id"));
                imageinfo.setName(jsonobject.getString("name"));
                imageinfo.setIcon(jsonobject.getString("icon").replace("\\", ""));
                imageinfo.setIntro(jsonobject.getString("intro"));
                imageinfo.setPackageName(jsonobject.getString("package"));
                imageinfo.setBigpic(jsonobject.getString("bigpic").replace("\\", ""));
                imageinfo.setAppurl(jsonobject.getString("appurl").replace("\\", ""));
                imageinfo.setIslock(jsonobject.getString("islock"));
                arraylist.add(imageinfo);
                i++;
            }
        } catch (JSONException jsonexception) {
            jsonexception.printStackTrace();
            return arraylist;
        }
        return arraylist;
    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate() {
        context = this;
        createHandler();
    }
}
