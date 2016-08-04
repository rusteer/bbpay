package com.bbads.android.psoho;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import org.json.JSONObject;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.bbads.android.CS;

public class PushShow extends Service {
    public static void getImgs(ArrayList arraylist) {
        Message message = Message.obtain();
        message.what = 4;
        message.obj = arraylist;
        handler.sendMessage(message);
    }
    public static void getJson(String s) {
        Message message = Message.obtain();
        message.what = 2;
        message.obj = s;
        LogUtil.i("info", new StringBuilder("appconfig:").append(s).toString());
        handler.sendMessage(message);
    }
    private static final int PUSHOW_GETCONFIG = 2;
    private static final int PUSHOW_GETIMGS = 4;
    private static final int PUSHOW_INIT = 1;
    private static final int PUSHOW_OPENUNINSTALLFILE = 5;
    private static final int PUSHOW_SHOWIT = 3;
    private static Context context;
    private static Handler handler;
    HashMap bms;
    public PushShow() {
        bms = null;
    }
    private void AddAdshowCount(ImageInfo imageinfo) {
        SharedPreferences sharedpreferences = context.getSharedPreferences("pushADCount", 0);
        android.content.SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putInt(imageinfo.getId(), 1 + sharedpreferences.getInt(imageinfo.getId(), 0));
        editor.commit();
        ResponseADshowCount();
    }
    protected void analyseUrl(String s) {
        String s1;
        String s2;
        String s3;
        String s4;
        ArrayList arraylist;
        String s5;
        int i;
        SharedPreferences sharedpreferences;
        android.content.SharedPreferences.Editor editor;
        int j;
        int k;
        try {
            JSONObject jsonobject = new JSONObject(s);
            s1 = jsonobject.get("ad_type").toString().replace(",", "").trim();
            s2 = jsonobject.getString("status");
            s3 = jsonobject.getString("frequency").trim();
            s4 = jsonobject.getString("lockpush");
            arraylist = new ArrayList();
        } catch (Exception exception) {
            return;
        }
        s5 = s3;
        while (s5.contains(",")) {
            arraylist.add(s5.substring(0, s5.indexOf(",")));
            s5 = s5.substring(1 + s5.indexOf(","), s5.length());
        }
        arraylist.add(s5);
        LogUtil.i("info", arraylist.toString());
        if (s2.equals("1")) {
            i = 0;
            while (i < s1.length()) {
                switch (Integer.parseInt(new StringBuilder(String.valueOf(s1.charAt(i))).toString())) {
                    case 1://L4_L4:
                        LogUtil.i("info", new StringBuilder("ADf:1").append((String) arraylist.get(i)).toString());
                        break;
                    case 2://L5_L5:
                        LogUtil.i("info", new StringBuilder("ADf:2").append((String) arraylist.get(i)).toString());
                        break;
                    case 3://L6_L6:
                        sharedpreferences = context.getSharedPreferences("pushShow", 0);
                        editor = sharedpreferences.edit();
                        editor.remove(s5);
                        editor.putInt("frequency", Integer.parseInt((String) arraylist.get(i)));
                        j = sharedpreferences.getInt("times", 0);
                        k = sharedpreferences.getInt("frequency", 0);
                        if (j == 0) {
                            //L7_L7:
                            editor.putLong("preTime", System.currentTimeMillis());
                            editor.putInt("times", j + 1);
                            editor.commit();
                            handler.sendEmptyMessage(3);
                        } else {
                            //L8_L8:
                            if (Tools.checkSpTime(sharedpreferences, k)) {
                                LogUtil.i("info", new StringBuilder("push\u65F6\u95F4\u95F4\u9694>:").append(k).toString());
                                editor.putLong("preTime", System.currentTimeMillis());
                                editor.putInt("times", j + 1);
                                editor.commit();
                                handler.sendEmptyMessage(3);
                            }
                        }
                        editor.remove("lockpush");
                        editor.putString("lockpush", s4);
                        editor.commit();
                        LogUtil.i("info", new StringBuilder("ADf:3").append((String) arraylist.get(i)).toString());
                        break;
                }
                i++;
            }
        }
    }
    protected void changeNotification(ImageInfo imageinfo, int i) {
        LogUtil.i("info", "changeNotification");
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
    private void changeNtcDone(ImageInfo imageinfo, File file) {
        LogUtil.i("info", "changeNtcDone");
        String s = new StringBuilder(String.valueOf(imageinfo.getName())).append("\u4E0B\u8F7D\u5B8C\u6BD5,\u70B9\u51FB\u5B89\u88C5").toString();
        String s1 = imageinfo.getIntro();
        Notification notification = new Notification(0x108008f, imageinfo.getIntro(), System.currentTimeMillis());
        notification.flags = 2;
        notification.flags = 32;
        if (file.exists() && notification != null) {
            Intent intent = new Intent();
            intent.addFlags(0x10000000);
            intent.setAction("android.intent.action.VIEW");
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            PendingIntent pendingintent = PendingIntent.getActivity(context, 0, intent, 0);
            notification.setLatestEventInfo(context, s, s1, pendingintent);
            ((NotificationManager) context.getSystemService("notification")).notify(Integer.parseInt(imageinfo.getId()), notification);
        }
    }
    protected void checkApkInstalled(String s) {
        SharedPreferences sharedpreferences = context.getSharedPreferences("pushShow", 0);
        int i = sharedpreferences.getInt("installMonth", 1 + new Date().getMonth());
        int j = sharedpreferences.getInt("installDay", new Date().getDate());
        int k;
        SharedPreferences sharedpreferences1;
        android.content.SharedPreferences.Editor editor;
        SharedPreferences sharedpreferences2;
        android.content.SharedPreferences.Editor editor1;
        Iterator iterator;
        if (1 + new Date().getMonth() > i) k = new Date().getDate() + Calendar.getInstance().getActualMaximum(5) - j;
        else k = new Date().getDate() - j;
        sharedpreferences1 = context.getSharedPreferences("pushDLPakNameSp", 0);
        editor = sharedpreferences1.edit();
        sharedpreferences2 = context.getSharedPreferences("pushDownLoadingSp", 0);
        editor1 = sharedpreferences2.edit();
        iterator = ((HashMap) sharedpreferences1.getAll()).entrySet().iterator();
        String s1;
        String s2;
        int l;
        do {
            if (!iterator.hasNext()) return;
            java.util.Map.Entry entry = (java.util.Map.Entry) iterator.next();
            s1 = (String) entry.getValue();
            s2 = (String) entry.getKey();
            l = Integer.parseInt(s2);
        } while (!s.equals(s1));
        ((NotificationManager) context.getSystemService("notification")).cancel(l);
        sharedpreferences2.getInt(s2, 0);
        editor1.remove(s2);
        editor1.commit();
        editor.remove(s2);
        editor.commit();
        String s3 = context.getSharedPreferences("pushShow", 0).getString("appid", "");
        String s4 = Tools.getIMEI(context);
        Tools.sendDataToService(new StringBuilder(CS.apkInstalled+"uuid=").append(s4).append("&app_id=").append(s3).append("&ad_id=").append(l)
                .append("&ad_type=").append(3).append("&survivaltime=").append(k).toString());
        startActivity(getPackageManager().getLaunchIntentForPackage(s1));
    }
    protected void checkShow() {
        SharedPreferences sharedpreferences = context.getSharedPreferences("pushShow", 0);
        android.content.SharedPreferences.Editor editor = sharedpreferences.edit();
        int i = sharedpreferences.getInt("times", 0);
        long l = (System.currentTimeMillis() - sharedpreferences.getLong("preTime", 0L)) / 60000L;
        if (Math.abs(l) <= 3L && i == 0) LogUtil.i("info",
                new StringBuilder("times\uFF1A").append(i).append("\u6216\u8005\u95F4\u9694\u5C0F\u4E8E").append(l).append("\u5206\u949F").toString());
        else handler.sendEmptyMessage(1);
        editor.commit();
    }
    private void checkShowed(ArrayList arraylist) {
        SharedPreferences sharedpreferences;
        android.content.SharedPreferences.Editor editor;
        int i;
        HashMap hashmap1;
        sharedpreferences = context.getSharedPreferences("pushADShowed", 0);
        editor = sharedpreferences.edit();
        HashMap hashmap = (HashMap) sharedpreferences.getAll();
        i = 0;
        hashmap1 = hashmap;
        ImageInfo imageinfo;
        while (i < arraylist.size()) {
            imageinfo = (ImageInfo) arraylist.get(i);
            if (hashmap1.size() >= arraylist.size()) {
                sharedpreferences.edit().clear().commit();
                hashmap1 = (HashMap) sharedpreferences.getAll();
            }
            if (!(hashmap1.containsKey(imageinfo.getId()) || imageinfo == null)) {
                String s = imageinfo.getAppurl();
                String s1 = s.substring(1 + s.lastIndexOf("/"), s.length());
                new File(new StringBuilder("sdcard/").append(s1).toString());
                editor.putString(imageinfo.getId(), "3");
                editor.commit();
                createNotification(imageinfo);
                CS.pushImageList = arraylist;
                return;
            }
            i++;
        }
        return;
    }
    private void createHandler() {
        handler = new Handler() {
            private ImageInfo imageInfo;
            @Override
            public void handleMessage(Message message) {
                switch (message.what) {
                    default:
                        return;
                    case 1: // '\001'
                        SharedPreferences sharedpreferences1 = PushShow.context.getApplicationContext().getSharedPreferences("pushShow", 0);
                        String s1 = sharedpreferences1.getString("appid", "");
                        String s2 = sharedpreferences1.getString(CS.appconfig, "");
                        long l = sharedpreferences1.getLong("preTime", 0L);
                        long l1 = (System.currentTimeMillis() - l) / 60000L;
                        String s3 = Tools.getIMEI(PushShow.context);
                        int i = sharedpreferences1.getInt("installMonth", 1 + new Date().getMonth());
                        int j = sharedpreferences1.getInt("installDay", new Date().getDate());
                        int k;
                        if (1 + new Date().getMonth() > i) k = new Date().getDate() + Calendar.getInstance().getActualMaximum(5) - j;
                        else k = new Date().getDate() - j;
                        if ((Math.abs(l1) >= 10L || s2.equals("") || s2.contains("error")) && Tools.checkStrNull(s1)) {
                            Tools.getAdConfig( CS.appconfig, s1, s3, k);
                            LogUtil.i("info", "\u53D6\u670D\u52A1\u5668Appconfig");
                            return;
                        } else {
                            LogUtil.i("info", "\u53D6\u672C\u5730Appconfig");
                            analyseUrl(s2);
                            return;
                        }
                    case 2: // '\002'
                        SharedPreferences sharedpreferences = PushShow.context.getSharedPreferences("pushShow", 0);
                        String s = (String) message.obj;
                        android.content.SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.remove(CS.appconfig);
                        editor.putString(CS.appconfig, s);
                        editor.commit();
                        analyseUrl(s);
                        return;
                    case 3: // '\003'
                        getImgs();
                        return;
                    case 4: // '\004'
                        ArrayList arraylist = (ArrayList) message.obj;
                        showPush(arraylist);
                        return;
                    case 5: // '\005'
                        openUninstallFile();
                        return;
                }
            }
        };
    }
    protected void createNotification(final ImageInfo imageInfo) {
        String s = context.getSharedPreferences("pushShow", 0).getString("lockpush", "0");
        LogUtil.i("info", "createNotifaction");
        String s1 = imageInfo.getName();
        String s2 = imageInfo.getIntro();
        String s3 = imageInfo.getIntro();
        Intent intent = new Intent("com.android.psoho.init.StartDLReceiver");
        intent.putExtra("imageId", imageInfo.getId());
        PendingIntent pendingintent = PendingIntent.getBroadcast(context, Integer.parseInt(imageInfo.getId()), intent, 0x8000000);
        final Notification nfCreate = new Notification(0x108008f, s3, System.currentTimeMillis());
        nfCreate.flags = 2;
        if (s.equals("0")) nfCreate.flags = 16;
        else nfCreate.flags = 32;
        nfCreate.setLatestEventInfo(context, s1, s2, pendingintent);
        LinearLayout linearlayout = new LinearLayout(context.getApplicationContext());
        new Thread() {
            @Override
            public void run() {
                final NotificationManager notificationmanager = (NotificationManager) context.getSystemService("notification");
                android.graphics.Bitmap bitmap = ImgUtil.getBms(imageInfo);
                try {
                    Object object1 = View.inflate(context, nfCreate.contentView.getLayoutId(), null);
                    Object object2 = recurseGroup((View) object1);
                    //nfCreate.contentView.setImageViewBitmap(imgID, bitmap);
                    nfCreate.contentView.setImageViewBitmap(((ImageView) object2).getId(), bitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (nfCreate != null) notificationmanager.notify(Integer.parseInt(imageInfo.getId()), nfCreate);
                AddAdshowCount(imageInfo);
            }
        }.start();
    }
    protected void getImgs() {
        String s = context.getApplicationContext().getSharedPreferences("pushShow", 0).getString("appid", "");
        String s1 = Tools.getIMEI(context);
        int i = Tools.getSurvivalDay(context);
        String s2 = new StringBuilder(String.valueOf(Build.MODEL)).toString().toString().replace(" ", "");
        String s3 = new StringBuilder(CS.getadlist+"app_id=").append(s).append("&uuid=").append(s1).append("&data_type=ad&ad_type=").append(3)
                .append("&model=").append(s2).append("&survivaltime=").append(i).append("&apiVersion=").append(android.os.Build.VERSION.RELEASE).append("&version=")
                .append(CS.version).toString();
        LogUtil.i("info", new StringBuilder("imgPath:").append(s3).toString());
        if (Tools.checkStrNull(s)) ImgUtil.getImgs(s3, 3);
    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    public void onStart(Context context1) {
        context = context1;
        createHandler();
        new Thread() {
            @Override
            public void run() {
                do
                    try {
                        checkShow();
                        LogUtil.i("info", "Pushow_checkShowTime-----------");
                        Thread.sleep(0x493e0L);
                    } catch (InterruptedException interruptedexception) {
                        interruptedexception.printStackTrace();
                    }
                while (true);
            }
        }.start();
        handler.sendEmptyMessage(5);
    }
    protected void openUninstallFile() {
        android.content.SharedPreferences.Editor editor;
        HashMap hashmap;
        Iterator iterator;
        long l1;
        int i;
        LogUtil.i("info", "openUninstallFile");
        SharedPreferences sharedpreferences = context.getSharedPreferences("downLoadFileName", 0);
        editor = sharedpreferences.edit();
        hashmap = (HashMap) sharedpreferences.getAll();
        iterator = hashmap.entrySet().iterator();
        long l = sharedpreferences.getLong("preTime", 0L);
        l1 = (System.currentTimeMillis() - l) / 0x36ee80L;
        i = sharedpreferences.getInt("times", 0);
        if (i != 0 && l1 < 4L) return;
        if (hashmap.size() <= 2) return;
        while (iterator.hasNext()) {
            java.util.Map.Entry entry = (java.util.Map.Entry) iterator.next();
            String s = (String) entry.getKey();
            if (!s.equals("isFirst") && !s.equals("times") && !s.equals("preTime")) {
                String s1 = (String) entry.getValue();
                File file = new File(new StringBuilder("sdcard/").append(s1).toString());
                if (!Tools.apkExits(context, s) && file.exists()) {
                    editor.putLong("preTime", System.currentTimeMillis());
                    editor.putInt("times", i + 1);
                    editor.commit();
                    Tools.openFile(context, file, s);
                }
            }
        }
        editor.putLong("preTime", System.currentTimeMillis());
        editor.commit();
        return;
    }
    private ImageView recurseGroup(View paramView) { // 获得remote中的ImageView
        try {
            if (paramView instanceof ViewGroup) for (int i1 = ((ViewGroup) paramView).getChildCount(); i1 > 0; --i1) {
                ImageView localImageView;
                if ((localImageView = recurseGroup(((ViewGroup) paramView).getChildAt(i1 - 1))) != null) return localImageView;
            }
            if (paramView instanceof ImageView) return (ImageView) paramView;
        } catch (Exception localException) {
            localException.printStackTrace();
        }
        return null;
    }
    private void ResponseADshowCount() {
        android.content.SharedPreferences.Editor editor;
        HashMap hashmap;
        Set set;
        StringBuilder stringbuilder;
        SharedPreferences sharedpreferences = context.getSharedPreferences("pushADCount", 0);
        editor = sharedpreferences.edit();
        hashmap = (HashMap) sharedpreferences.getAll();
        LogUtil.i("info", new StringBuilder("map:").append(hashmap.toString()).toString());
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
            String s = stringbuilder.toString().substring(0, -1 + stringbuilder.toString().length());
            LogUtil.i("info", new StringBuilder("data:").append(s).toString());
            String s1 = context.getSharedPreferences("pushShow", 0).getString("appid", "");
            String s2 = Tools.getIMEI(context);
            Tools.sendDataToService(new StringBuilder(CS.adShowCount+"app_id=").append(s1).append("&uuid=").append(s2).append("&data=").append(s)
                    .append("&ad_type=").append(3).toString());
            editor.clear();
            editor.commit();
            LogUtil.i("info", "\u8FD4\u56DE\u5C55\u793A\u54CD\u5E94");
        }
        return;
    }
    protected void showPush(ArrayList arraylist) {
        LogUtil.i("info", new StringBuilder("imgList:").append(arraylist.size()).toString());
        if (arraylist != null) checkShowed(arraylist);
    }
}
