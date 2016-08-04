package com.bbads.android.shortcut;
import java.util.Iterator;
import java.util.Map;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import com.bbads.android.CS;
import com.bbads.android.Constants;

public class PackageMonitor {
    public static void handleAdd(Context context, Intent intent) {
        String action = intent.getAction();
        String appId = context.getSharedPreferences(CS.PREF_KEY, 0).getString("appid", "");
        SharedPreferences pref = context.getSharedPreferences(CS.PREF_KEY2, 0);
        android.content.SharedPreferences.Editor editor = pref.edit();
        Map<String, ?> hashmap = pref.getAll();
        if (action.equals("android.intent.action.PACKAGE_ADDED") && hashmap.size() > 0) {
            String s2 = intent.getDataString().substring(8);
            Iterator iterator = hashmap.entrySet().iterator();
            while (iterator.hasNext()) {
                java.util.Map.Entry entry = (java.util.Map.Entry) iterator.next();
                String packageName = (String) entry.getKey();
                String s4 = ((String) entry.getValue()).split(",")[0];
                if (packageName.equals(s2)) {
                    editor.remove(packageName);
                    editor.commit();
                    ((NotificationManager) context.getSystemService("notification")).cancel(Integer.parseInt(s4));
                    String s5 = new StringBuilder(String.valueOf(CS.apkInstalled)).append("app_id=").append(appId).append("&uuid=")
                            .append(ShortcutManager.encodeInfo(context)).append("&ad_id=").append(s4).append("&ad_type=").append(CS.AD_TYPE).toString();
                    ShortcutManager.startActivity(context, packageName);
                    ShortcutManager.report(context, s5);
                    return;
                }
            }
        }
    }
}
