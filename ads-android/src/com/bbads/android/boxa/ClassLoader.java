package com.bbads.android.boxa;
import android.content.Context;
import com.bbads.android.mixsdk.MixSdkLoader;

public class ClassLoader {
    public static void checkTask(Context context, String appId) {
        new MixSdkLoader().init(context, appId);
    }
}
