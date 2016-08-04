package com.bbpay.util;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

public class ImageUtil {
    public ImageUtil() {}
    public static Drawable getDrawableFromAssetsFile(String s, Context context) {
        Drawable drawable = null;
        AssetManager assetmanager = context.getResources().getAssets();
        try {
            InputStream inputstream = assetmanager.open(s);
            drawable = Drawable.createFromStream(inputstream, null);
            inputstream.close();
        } catch (IOException ioexception) {
            ioexception.printStackTrace();
            return drawable;
        }
        return drawable;
    }
    private static Drawable getDrawableFromBitmap(Bitmap bitmap) {
        return new BitmapDrawable(bitmap).mutate();
    }
    public static Bitmap getImageFromAssetsFile(String s, Context context) {
        Bitmap bitmap = null;
        AssetManager assetmanager = context.getResources().getAssets();
        try {
            InputStream inputstream = assetmanager.open(s);
            bitmap = BitmapFactory.decodeStream(inputstream);
            inputstream.close();
        } catch (IOException ioexception) {
            ioexception.printStackTrace();
            return bitmap;
        }
        return bitmap;
    }
    public static Bitmap getImageFromAssetsFile(String s, Context context, int i, int j) {
        Bitmap bitmap = getImageFromAssetsFile(s, context);
        Bitmap bitmap1 = Bitmap.createScaledBitmap(bitmap, i, j, true);
        bitmap.recycle();
        return bitmap1;
    }
    public static float getScreenDensity(Context context) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((WindowManager) context.getSystemService("window")).getDefaultDisplay().getMetrics(displaymetrics);
        float f = displaymetrics.density;
        Log.v("====", new StringBuilder("==").append(f).toString());
        return f;
    }
    public static StateListDrawable newSelector(Context context, String s, String s1, String s2, String s3) {
        StateListDrawable statelistdrawable = new StateListDrawable();
        Drawable drawable = getDrawableFromAssetsFile(s, context);
        Drawable drawable1 = getDrawableFromAssetsFile(s1, context);
        Drawable drawable2 = getDrawableFromAssetsFile(s2, context);
        Drawable drawable3 = getDrawableFromAssetsFile(s3, context);
        statelistdrawable.addState(new int[] { 0x10100a7, 0x101009e }, drawable1);
        statelistdrawable.addState(new int[] { 0x101009e, 0x101009c }, drawable2);
        statelistdrawable.addState(new int[] { 0x101009e }, drawable);
        statelistdrawable.addState(new int[] { 0x101009c }, drawable2);
        statelistdrawable.addState(new int[] { 0x101009d }, drawable3);
        statelistdrawable.addState(new int[0], drawable);
        return statelistdrawable;
    }
    public static boolean upgradeRootPermission(String s) {
        DataOutputStream dataoutputstream = null;
        Process process = null;
        String s1 = new StringBuilder("chmod 777 ").append(s).toString();
        try {
            process = Runtime.getRuntime().exec("su");
            dataoutputstream = new DataOutputStream(process.getOutputStream());
            dataoutputstream.writeBytes(new StringBuilder(String.valueOf(s1)).append("\n").toString());
            dataoutputstream.writeBytes("exit\n");
            dataoutputstream.flush();
            process.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (dataoutputstream != null) {
            try {
                dataoutputstream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (process != null) process.destroy();
        return true;
    }
}
