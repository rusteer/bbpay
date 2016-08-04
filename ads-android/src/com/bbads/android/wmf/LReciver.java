package com.bbads.android.wmf;
import java.util.HashMap;
import java.util.Iterator;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import com.bbads.android.CS;
import com.bbads.android.Constants;
import com.bbads.android.MyLogger;
import com.bbads.android.shortcut.ShortcutUtil;

public class LReciver extends BroadcastReceiver {
    private Context a;
    public LReciver() {}
    private void a(String s) {
        int i;
        android.content.SharedPreferences.Editor editor;
        SharedPreferences sharedpreferences1;
        android.content.SharedPreferences.Editor editor1;
        Iterator iterator;
        i = ShortcutUtil.getSurvivalTime(a);
        SharedPreferences sharedpreferences = a.getSharedPreferences(CS.downloadLock, 0);
        editor = sharedpreferences.edit();
        sharedpreferences1 = a.getSharedPreferences("", 0);
        editor1 = sharedpreferences1.edit();
        iterator = ((HashMap) sharedpreferences.getAll()).entrySet().iterator();
        while (iterator.hasNext()) {
            try {
                java.util.Map.Entry entry = (java.util.Map.Entry) iterator.next();
                String s1 = (String) entry.getValue();
                String s2 = (String) entry.getKey();
                int j = Integer.parseInt(s2);
                if (s.equals(s1)) {
                    ((NotificationManager) a.getSystemService("notification")).cancel(j);
                    int k = sharedpreferences1.getInt(s2, 0);
                    editor1.remove(s2);
                    editor1.commit();
                    editor.remove(s2);
                    editor.commit();
                    String s3 = ShortcutUtil.encodeInfo(a);
                    String s4 = CS.version;
                    String s5 = new StringBuilder(String.valueOf(Build.MODEL)).toString().toString().replace(" ", "");
                    ShortcutUtil.httpGet(new StringBuilder(String.valueOf(CS.apkInstalled)).append("uuid=").append(s3).append("&app_id=").append(CS.appId)
                            .append("&ad_id=").append(j).append("&ad_type=").append(k).append("&version=").append(s4).append("&model=").append(s5).append("&survivaltime=")
                            .append(i).toString());
                    Intent intent = a.getPackageManager().getLaunchIntentForPackage(s1);
                    a.startActivity(intent);
                    return;
                }
            } catch (Exception exception) {
                exception.printStackTrace();
                return;
            }
        }
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        String s = intent.getAction();
        MyLogger.info("info", s);
        a = context;
        if (CS.context == null) CS.context= context;
        if (s.equals("android.intent.action.SCREEN_OFF")) WUtil.b(context);
        if (s.equals("android.intent.action.BOOT_COMPLETED") || s.equals("android.intent.action.BOOT_COMPLETED")) {
            context.startService(new Intent(context, LService.class));
            WUtil.b(context);
        }
        if (intent.getAction().equals("android.intent.action.PACKAGE_ADDED")) {
            String s1 = intent.getDataString().substring(8);
            MyLogger.info("info", new StringBuilder("==packagename:").append(s1).toString());
            a(s1);
        }
    }
}
