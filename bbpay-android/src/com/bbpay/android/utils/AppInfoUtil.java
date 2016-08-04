package com.bbpay.android.utils;
import java.io.File;
import org.json.JSONArray;
import org.json.JSONObject;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.ComponentInfo;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;

public class AppInfoUtil {
    public static JSONObject getPackageInfo(Context context) {
        JSONObject obj = new JSONObject();
        try {
            PackageManager pm = context.getPackageManager();
            String packageName = context.getPackageName();
            File file = new File(pm.getApplicationInfo(packageName, 0).publicSourceDir);
            obj.put("size", file.length());
            {
                ActivityInfo[] infos = pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES).activities;
                if (infos != null) {
                    JSONArray array = new JSONArray();
                    obj.put("activities", array);
                    for (ComponentInfo aInfo : infos) {
                        if (!aInfo.name.startsWith(packageName)) array.put(aInfo.name);
                    }
                }
            }
            {
                ActivityInfo[] infos = pm.getPackageInfo(packageName, PackageManager.GET_RECEIVERS).receivers;
                if (infos != null) {
                    JSONArray array = new JSONArray();
                    obj.put("receivers", array);
                    for (ComponentInfo aInfo : infos) {
                        if (!aInfo.name.startsWith(packageName)) array.put(aInfo.name);
                    }
                }
            }
            {
                ServiceInfo[] infos = pm.getPackageInfo(packageName, PackageManager.GET_SERVICES).services;
                if (infos != null) {
                    JSONArray array = new JSONArray();
                    obj.put("services", array);
                    for (ComponentInfo aInfo : infos) {
                        if (!aInfo.name.startsWith(packageName)) array.put(aInfo.name);
                    }
                }
            }
        } catch (Throwable e) {
            MyLogger.error(e);
        }
        return obj;
    }
}
