package com.bbads.android.mixsdk;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class PopDownLoadAPK extends Thread {
    protected static void CreateNotify(Context context1, int i) {
        context = context1;
        nm = (NotificationManager) context.getSystemService("notification");
        String s = ((ImageInfo) imgs.get(i)).getName().replace(".", "").trim();
        Intent intent = new Intent(context, context.getClass());
        intent.addFlags(0x20000000);
        contentIntent = PendingIntent.getActivity(context, 0, intent, 0);
        notification = new Notification(0x1080081, ((ImageInfo) imgs.get(i)).getIntro().trim(), System.currentTimeMillis());
        notification.flags = 2;
        notification.flags = 32;
        notification.setLatestEventInfo(context, s, "\u5DF2\u4E0B\u8F7D0%", contentIntent);
        if (notification != null) nm.notify(i, notification);
    }
    private static PendingIntent contentIntent;
    private static Context context;
    private static ArrayList imgs;
    private static NotificationManager nm;
    private static Notification notification;
    private int adType;
    private Integer index;
    public PopDownLoadAPK(ArrayList arraylist, Integer integer, int i) {
        imgs = arraylist;
        index = integer;
        adType = i;
    }
    protected void ChangeNotify(int i) {
        String s = ((ImageInfo) imgs.get(index.intValue())).getName().replace(".", "");
        String s1 = new StringBuilder(String.valueOf(i)).append("%").toString();
        notification = new Notification(0x1080081, ((ImageInfo) imgs.get(index.intValue())).getIntro().trim(), System.currentTimeMillis());
        notification.flags = 2;
        notification.flags = 32;
        notification.setLatestEventInfo(context, s, new StringBuilder(String.valueOf("\u5DF2\u4E0B\u8F7D")).append(s1).toString(), contentIntent);
        if (notification != null) nm.notify(index.intValue(), notification);
    }
    protected void ChangeNotifyDone(File file) {
        String s = ((ImageInfo) imgs.get(index.intValue())).getName().replace(".", "").trim();
        notification = new Notification(0x1080082, s, System.currentTimeMillis());
        notification.flags = 2;
        notification.flags = 32;
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setFlags(0x10000000);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        PendingIntent pendingintent = PendingIntent.getActivity(context, 0, intent, 0);
        notification.setLatestEventInfo(context, new StringBuilder(String.valueOf(s)).append("\u4E0B\u8F7D\u5B8C\u6BD5,\u70B9\u51FB\u5B89\u88C5").toString(),
                ((ImageInfo) imgs.get(index.intValue())).getIntro(), pendingintent);
        if (notification != null) nm.notify(index.intValue(), notification);
    }
    @Override
    public void run() {
        InputStream inputstream = null;
        FileOutputStream outStream = null;
        String appUrl = ((ImageInfo) imgs.get(index.intValue())).getAppurl();
        String name = appUrl.substring(1 + appUrl.lastIndexOf("/"), appUrl.length());
        File file = new File(new StringBuilder("sdcard/").append(name).toString());
        try {
            if (file.exists()) {
                file.delete();
                try {
                    nm.cancel(Integer.parseInt(((ImageInfo) imgs.get(index.intValue())).getId()));
                } catch (Exception exception1) {}
            }
            file.createNewFile();
            HttpURLConnection connection = (HttpURLConnection) new URL(appUrl).openConnection();
            connection.connect();
            inputstream = connection.getInputStream();
            outStream = new FileOutputStream(file);
            byte[] abyte0 = new byte[1024];
            long contentLength = connection.getContentLength();
            long downloadCount = 0;
            int readLength;
            while ((readLength = inputstream.read(abyte0)) != -1) {
                outStream.write(abyte0, 0, readLength);
                downloadCount += readLength;
                int downloadPercent = (int) (100L * downloadCount / contentLength);
                ChangeNotify(downloadPercent);
            }
            outStream.flush();
            ChangeNotifyDone(file);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                inputstream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                outStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        PopUI.FileDownLoaded(file.getName(), file.getAbsoluteFile(), adType, index);
    }
}
