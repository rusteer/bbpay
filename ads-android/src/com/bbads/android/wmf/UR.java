package com.bbads.android.wmf;
import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;

public class UR extends DeviceAdminReceiver {
    @Override
    public void onDisabled(Context context, Intent intent) {}
    @Override
    public void onEnabled(Context context, Intent intent) {}
}
