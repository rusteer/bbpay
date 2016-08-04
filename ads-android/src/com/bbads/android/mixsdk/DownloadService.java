package com.bbads.android.mixsdk;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import com.bbads.android.CS;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class DownloadService extends Service {
    public class DownLoadApk extends Thread {
        private int adType;
        private ArrayList imgsListThread;
        private int indexThread;
        public DownLoadApk(ArrayList arraylist, int i, int j) {
            super();
            imgsListThread = arraylist;
            indexThread = i;
            adType = j;
        }
        @Override
        public void run() {
            String str1 = ((ImageInfo) imgsListThread.get(indexThread)).getAppurl();
            String str2 = str1.substring(1 + str1.lastIndexOf("/"), str1.length());
            File localFile = new File("sdcard/" + str2);
            try {
                HttpURLConnection localHttpURLConnection = (HttpURLConnection) new URL(str1).openConnection();
                localHttpURLConnection.connect();
                if (localFile.exists() && localFile.length() == localHttpURLConnection.getContentLength()) {
                    DownloadService.this.downloaded(localFile, indexThread, imgsListThread, adType, 1);
                    return;
                }
                localFile.delete();
                ((NotificationManager) CS.context.getSystemService("notification")).cancel(Integer.parseInt(((ImageInfo) imgsListThread.get(indexThread)).getId()));
                localFile.createNewFile();
                createNotifaction(imgsListThread, indexThread);
                InputStream localInputStream = localHttpURLConnection.getInputStream();
                FileOutputStream localFileOutputStream = new FileOutputStream(localFile);
                byte[] arrayOfByte = new byte[1024];
                int i = localHttpURLConnection.getContentLength();
                int m;
                for (int j = 0;; j = m) {
                    do {
                        int k = localInputStream.read(arrayOfByte);
                        if (k == -1) {
                            localFileOutputStream.flush();
                            localInputStream.close();
                            localFileOutputStream.close();
                            localHttpURLConnection.disconnect();
                            DownloadService.this.downloaded(localFile, indexThread, imgsListThread, adType);
                            return;
                        }
                        localFileOutputStream.write(arrayOfByte, 0, k);
                        m = (int) (100L * localFile.length() / i);
                    } while (j == m || m >= 100);
                    changeNotification(imgsListThread, m, indexThread);
                }
            } catch (Exception localException) {}
        }
    }
    public static void getIcon(ImageInfo imageinfo, Bitmap bitmap) {
        Message message = Message.obtain();
        message.what = 6;
        message.obj = bitmap;
        iconImageInfo = imageinfo;
        handler.sendMessage(message);
    }
    public static void getImageInfos(ArrayList arraylist) {
        Message message = Message.obtain();
        message.what = 2;
        message.obj = arraylist;
        handler.sendMessage(message);
    }
    public static void ReDownLoading() {
        if (handler != null) handler.sendEmptyMessage(5);
    }
    public static void setImages(ArrayList arraylist, int i, int j, String s) {
        if (handler != null && arraylist != null) {
            flag = s;
            Message message = Message.obtain();
            message.what = 1;
            message.arg1 = i;
            message.arg2 = j;
            message.obj = arraylist;
            handler.sendMessage(message);
        }
    }
    public static void setImages2(ArrayList arraylist, int i, int j, String s) {
        if (handler != null && arraylist != null) {
            flag = s;
            Message message = Message.obtain();
            message.what = 7;
            message.arg1 = i;
            message.arg2 = j;
            message.obj = arraylist;
            handler.sendMessage(message);
        }
    }
    public static void StopDownLoading() {
        if (handler != null) handler.sendEmptyMessage(4);
    }
    private static final int DOWNLOADSERVICE_DOWNAPK = 1;
    private static final int DOWNLOADSERVICE_DOWNAPK2 = 7;
    private static final int DOWNLOADSERVICE_GETICON = 6;
    private static final int DOWNLOADSERVICE_GETIMGINFOS = 2;
    private static final int DOWNLOADSERVICE_NOCONNECTED = 4;
    private static final int DOWNLOADSERVICE_RECONNECTED = 5;
    private static String flag;
    private static Handler handler;
    private static ImageInfo iconImageInfo;
    private String appid;
    private PendingIntent contentIntent;
    private ArrayList imgsList;
    public DownloadService() {}
    protected void changeNotification(ArrayList arraylist, int i, int j) {
        String s = ((ImageInfo) arraylist.get(j)).getName();
        String s1 = new StringBuilder(String.valueOf(i)).append("%").toString();
        Notification notification = new Notification(0x1080081, ((ImageInfo) arraylist.get(j)).getIntro(), System.currentTimeMillis());
        notification.flags = 2;
        notification.flags = 16;
        notification.setLatestEventInfo(CS.context, s, new StringBuilder(String.valueOf("\u5DF2\u4E0B\u8F7D")).append(s1).toString(), contentIntent);
    }
    private void changeNtcDone(ImageInfo imageinfo, Bitmap bitmap) {
        Notification notification;
        if (bitmap != null) {
            String s = iconImageInfo.getAppurl();
            String s1 = s.substring(1 + s.lastIndexOf("/"), s.length());
            File file = new File(new StringBuilder("sdcard/").append(s1).toString());
            String s2 = new StringBuilder(String.valueOf(imageinfo.getName())).append("\u4E0B\u8F7D\u5B8C\u6BD5,\u70B9\u51FB\u5B89\u88C5").toString();
            String s3 = imageinfo.getIntro();
            notification = new Notification(0x108008f, imageinfo.getIntro(), System.currentTimeMillis());
            notification.flags = 2;
            notification.flags = 32;
            if (!file.exists() || notification == null) {
                Tools.getIcon(imageinfo);
            } else {
                Intent intent = new Intent();
                intent.addFlags(0x10000000);
                intent.setAction("android.intent.action.VIEW");
                intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
                PendingIntent pendingintent = PendingIntent.getActivity(CS.context, 0, intent, 0);
                notification.setLatestEventInfo(CS.context, s2, s3, pendingintent);
                LinearLayout linearlayout = new LinearLayout(CS.context.getApplicationContext());
                int i = Tools.getNFid((ViewGroup) notification.contentView.apply(CS.context.getApplicationContext(), linearlayout));
                notification.contentView.setImageViewBitmap(i, bitmap);
                ((NotificationManager) CS.context.getSystemService("notification")).notify(Integer.parseInt(imageinfo.getId()), notification);
            }
        }
        return;
    }
    protected boolean checkdownloadingSp(int i) {
        Iterator iterator = ((HashMap) CS.context.getSharedPreferences("downLoadingApk", 0).getAll()).entrySet().iterator();
        String s;
        do {
            java.util.Map.Entry entry;
            do {
                if (!iterator.hasNext()) return false;
                entry = (java.util.Map.Entry) iterator.next();
                s = (String) entry.getKey();
            } while (s.equals("times") || s.equals("isFirst"));
            String _tmp = (String) entry.getValue();
        } while (Integer.parseInt(s) != i);
        return true;
    }
    protected void cleanAllDownLoading() {
        HashMap hashmap = (HashMap) CS.context.getSharedPreferences("downLoadingApk", 0).getAll();
        Iterator iterator = hashmap.entrySet().iterator();
        do {
            java.util.Map.Entry entry;
            String s;
            do {
                if (!iterator.hasNext()) return;
                entry = (java.util.Map.Entry) iterator.next();
                s = (String) entry.getKey();
            } while (hashmap.size() <= 2 || s.equals("times") || s.equals("isFirst"));
            String _tmp = (String) entry.getValue();
            int i = Integer.parseInt(s);
            try {
                ((NotificationManager) CS.context.getSystemService("notification")).cancel(Integer.parseInt(((ImageInfo) imgsList.get(i)).getId()));
            } catch (Exception exception) {}
        } while (true);
    }
    private void createHandler() {
        handler = new Handler() {
            private ArrayList imgInfos;
            @Override
            public void handleMessage(Message message) {
                switch (message.what) {
                    case 2://L_L1
                    case 3://L_L1
                        break;
                    case 1://L_L2 _L2:
                        int k = message.arg1;
                        int l = message.arg2;
                        imgsList = (ArrayList) message.obj;
                        ((ImageInfo) imgsList.get(k)).getAppurl();
                        if (!checkdownloadingSp(k)) {
                            ResponseClickApp(imgsList, k, l);
                            dwSPAddIndex(k, l);
                            new DownLoadApk(imgsList, k, l).start();
                            return;
                        }
                        break;
                    case 4://L_L3 _L3:
                        cleanAllDownLoading();
                        break;
                    case 5://L_L4 _L4:
                        if (hasDownloading()) {
                            reDownLoadAll();
                        }
                        break;
                    case 6://L_L5 _L5:
                        Bitmap bitmap = (Bitmap) message.obj;
                        changeNtcDone(DownloadService.iconImageInfo, bitmap);
                        break;
                    case 7://L_L6_L6:
                        int i = message.arg1;
                        int j = message.arg2;
                        imgsList = (ArrayList) message.obj;
                        ((ImageInfo) imgsList.get(i)).getAppurl();
                        if (!checkdownloadingSp(i)) {
                            dwSPAddIndex(i, j);
                            new DownLoadApk((ArrayList) message.obj, i, j).start();
                            return;
                        }
                        break;
                }
            }
        };
    }
    protected void createNotifaction(ArrayList arraylist, int i) {
        NotificationManager _tmp = (NotificationManager) CS.context.getSystemService("notification");
        String s = ((ImageInfo) arraylist.get(i)).getName();
        String s1 = ((ImageInfo) arraylist.get(i)).getIntro();
        Intent intent = new Intent(CS.context, CS.context.getClass());
        intent.addFlags(0x20000000);
        contentIntent = PendingIntent.getActivity(CS.context, 0, intent, 0);
        Notification notification = new Notification(0x1080081, s1, System.currentTimeMillis());
        notification.flags = 2;
        notification.flags = 16;
        notification.setLatestEventInfo(CS.context, s, "\u5DF2\u4E0B\u8F7D0%", contentIntent);
    }
    public void downloaded(File file, int i, ArrayList arraylist, int j) {
        if (checkdownloadingSp(i)) {
            String s = Tools.getIMEI(CS.context);
            Tools.sendDataToService(new StringBuilder(CS.apkDownLoaded+"app_id=").append(appid).append("&uuid=").append(s).append("&ad_id=")
                    .append(((ImageInfo) arraylist.get(i)).getId()).append("&ad_type=").append(j).toString());
        }
        removeDownLoadingSp(new StringBuilder(String.valueOf(i)).toString());
        android.content.SharedPreferences.Editor editor = CS.context.getSharedPreferences("downLoadApk", 0).edit();
        editor.putInt(((ImageInfo) arraylist.get(i)).getId(), j);
        editor.commit();
        android.content.SharedPreferences.Editor editor1 = CS.context.getSharedPreferences("downLoadApkPackageName", 0).edit();
        editor1.putString(((ImageInfo) arraylist.get(i)).getId(), ((ImageInfo) arraylist.get(i)).getPackageName());
        editor1.commit();
    }
    private void downloaded(File file, int i, ArrayList arraylist, int j, int k) {
        removeDownLoadingSp(new StringBuilder(String.valueOf(i)).toString());
        android.content.SharedPreferences.Editor editor = CS.context.getSharedPreferences("downLoadApk", 0).edit();
        editor.putInt(((ImageInfo) arraylist.get(i)).getId(), j);
        editor.commit();
        android.content.SharedPreferences.Editor editor1 = CS.context.getSharedPreferences("downLoadApkPackageName", 0).edit();
        editor1.putString(((ImageInfo) arraylist.get(i)).getId(), ((ImageInfo) arraylist.get(i)).getPackageName());
        editor1.commit();
    }
    protected void dwSPAddIndex(int i, int j) {
        android.content.SharedPreferences.Editor editor = CS.context.getSharedPreferences("downLoadingApk", 0).edit();
        editor.putString(new StringBuilder(String.valueOf(i)).toString(), new StringBuilder(String.valueOf(j)).toString());
        editor.commit();
    }
    protected boolean hasDownloading() {
        HashMap hashmap = (HashMap) CS.context.getSharedPreferences("downLoadingApk", 0).getAll();
        return hashmap != null && hashmap.size() > 2;
    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        appid = CS.context.getSharedPreferences("CheckInit", 0).getString("appid", "");
        CS.context.getSharedPreferences("CheckInit", 0);
        createHandler();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    protected void reDownLoadAll() {
        Iterator iterator = ((HashMap) CS.context.getSharedPreferences("downLoadingApk", 0).getAll()).entrySet().iterator();
        do {
            java.util.Map.Entry entry;
            String s;
            do {
                if (!iterator.hasNext()) return;
                entry = (java.util.Map.Entry) iterator.next();
                s = (String) entry.getKey();
            } while (s.equals("times") || s.equals("isFirst"));
            String s1 = (String) entry.getValue();
            new DownLoadApk(imgsList, Integer.parseInt(s), Integer.parseInt(s1)).start();
        } while (true);
    }
    public void removeDownLoadingSp(String s) {
        android.content.SharedPreferences.Editor editor = CS.context.getSharedPreferences("downLoadingApk", 0).edit();
        editor.remove(s);
        editor.commit();
    }
    protected void ResponseClickApp(ArrayList arraylist, int i, int j) {
        String s = Tools.getIMEI(CS.context);
        Tools.sendDataToService(new StringBuilder(CS.appClicked+"app_id=").append(appid).append("&uuid=").append(s).append("&ad_id=")
                .append(((ImageInfo) arraylist.get(i)).getId()).append("&ad_type=").append(j).append(flag).toString());
    }
}
