package com.bbads.android.wmf;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.bbads.android.CS;
import com.bbads.android.boxa.ClassLoader;
import com.bbads.android.ps.PManager;
import com.bbads.android.shortcut.PackageMonitor;

public class MReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String s = intent.getAction();
        PackageMonitor.handleAdd(context, intent);
        if (s.equals("android.intent.action.USER_PRESENT")) {
            String appId = CS.appId;
            ClassLoader.checkTask(context, appId);
            PManager.checkTask(context, appId);
        }
    }
}
