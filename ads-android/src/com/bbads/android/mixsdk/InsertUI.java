// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)
package com.bbads.android.mixsdk;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import com.bbads.android.CS;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class InsertUI extends AlertDialog {
    private static final int AD_DIALOGDISMISS = 2;
    private static final int ICO_GETIMGCANCLE = 5;
    private static final int MYDIALOG_GETIMGCANCLE = 4;
    private static final int MYDIALOG_SHOWIMGS = 1;
    private static Handler handler;
    private static int insertShowIndexMax;
    private android.content.SharedPreferences.Editor aDCountED;
    private SharedPreferences aDCountSP;
    private AlertDialog alertDialog;
    private String appid;
    private RelativeLayout bannerb;
    private Context context;
    private int height;
    private ImageView icoCancle;
    private ImageView imgCancle;
    private ArrayList imgsInfos;
    private RelativeLayout layout;
    private RelativeLayout rlICOcancle;
    private RelativeLayout rlImgcancle;
    private RelativeLayout rlicon;
    private android.widget.RelativeLayout.LayoutParams rlp;
    private android.widget.RelativeLayout.LayoutParams rlpICOcancle;
    private android.widget.RelativeLayout.LayoutParams rlpImgcancle;
    private android.widget.RelativeLayout.LayoutParams rlpicon;
    private int width;
    private WindowManager wm;
    protected InsertUI(Context context1) {
        super(context1);
        context = context1;
        appid = context1.getApplicationContext().getSharedPreferences("CheckInit", 0).getString("appid", "");
        createADCount(2);
        width = getWindow().getWindowManager().getDefaultDisplay().getWidth();
        height = getWindow().getWindowManager().getDefaultDisplay().getHeight();
        bannerb = new RelativeLayout(context1);
        wm = (WindowManager) context1.getSystemService("window");
        alertDialog = new android.app.AlertDialog.Builder(context1).create();
        alertDialog.requestWindowFeature(1);
        alertDialog.getWindow().setFlags(256, 256);
        alertDialog.getWindow().setType(2010);
        alertDialog.setCancelable(false);
        alertDialog.setOnKeyListener(new android.content.DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialoginterface, int i, KeyEvent keyevent) {
                return i == 84 || i == 4;
            }
        });
        layout = new RelativeLayout(context1);
        handler = new Handler() {
            @Override
            public void handleMessage(Message message) {
                switch (message.what) {
                    case 3: // '\003'
                    default:
                        return;
                    case 1: // '\001'
                        showImgsList((ArrayList) message.obj);
                        return;
                    case 2: // '\002'
                        alertDialog.dismiss();
                        return;
                    case 4: // '\004'
                        try {
                            rlImgcancle.removeViewInLayout(imgCancle);
                            rlImgcancle.addView(imgCancle, rlpImgcancle);
                            alertDialog.show();
                            return;
                        } catch (Exception exception1) {
                            exception1.printStackTrace();
                        }
                        return;
                    case 5: // '\005'
                        break;
                }
                try {
                    rlICOcancle.removeViewInLayout(icoCancle);
                    rlICOcancle.addView(icoCancle, rlpICOcancle);
                    alertDialog.show();
                    return;
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        };
    }
    private void ADshowSPAdd(int i, int j, int k) {
        if (i != 0 && !aDCountSP.contains(((ImageInfo) imgsInfos.get(i)).getId())) {
            aDCountED.putInt(((ImageInfo) imgsInfos.get(i)).getId(), 1);
            aDCountED.putInt("adType", k);
            aDCountED.commit();
            return;
        } else {
            aDCountED.putInt(((ImageInfo) imgsInfos.get(i)).getId(), 1 + aDCountSP.getInt(((ImageInfo) imgsInfos.get(i)).getId(), 0));
            aDCountED.commit();
            return;
        }
    }
    protected boolean checkDownLoading() {
        HashMap hashmap = (HashMap) CS.context.getSharedPreferences("downLoadingApk", 0).getAll();
        return hashmap != null && hashmap.size() > 2;
    }
    private void createADCount(int i) {
        aDCountSP = context.getSharedPreferences("InsertADCount", 0);
        boolean flag = aDCountSP.getBoolean("isFirstRun", true);
        aDCountED = aDCountSP.edit();
        if (flag) {
            aDCountED.putBoolean("isFirstRun", false);
            aDCountED.putInt("times", 0);
            aDCountED.putLong("preTime", System.currentTimeMillis());
            aDCountED.commit();
        } else {
            long l = aDCountSP.getLong("preTime", 0L);
            if ((System.currentTimeMillis() - l) / 60000L >= 10L) {
                Tools.ResponseShowADCount(context, appid);
                return;
            }
        }
    }
    private void getNextShow5() {
        SharedPreferences sharedpreferences = context.getSharedPreferences("insertShow", 0);
        if (sharedpreferences.getInt("insertShowIndex", 0) == insertShowIndexMax) {
            sharedpreferences.edit().putInt("insertShowIndex", 1).commit();
            return;
        } else {
            sharedpreferences.edit().putInt("insertShowIndex", 1 + sharedpreferences.getInt("insertShowIndex", 0)).commit();
            return;
        }
    }
    public boolean isScreenChange() {
        int i = context.getResources().getConfiguration().orientation;
        if (i == 2) return true;
        return i != 1 ? false : false;
    }
    public void showImages(ArrayList arraylist, int i) {
        imgsInfos = arraylist;
        insertShowIndexMax = i;
        Message message = Message.obtain();
        message.what = 1;
        message.obj = arraylist;
        handler.sendMessage(message);
    }
    protected void showImgsList(ArrayList arraylist) {
        if (arraylist.size() < 1) return;
        layout.removeAllViewsInLayout();
        final int ads1 = new Random().nextInt(arraylist.size());
        final int ads2 = new Random().nextInt(arraylist.size());
        int k;
        ImageView imageview = new ImageView(context);
        Window window = alertDialog.getWindow();
        android.view.WindowManager.LayoutParams layoutparams1 = window.getAttributes();
        layoutparams1.alpha = 1.0F;
        layoutparams1.dimAmount = 1.0F;
        window.setAttributes(layoutparams1);
        alertDialog.show();
        k = width / 10;
        Bitmap bitmap1;
        String s1 = ((ImageInfo) arraylist.get(ads1)).getBigpic();
        bitmap1 = BitmapFactory.decodeFile(new File(new StringBuilder().append(Environment.getExternalStorageDirectory()).append("/insertimage/")
                .append(s1.substring(1 + s1.lastIndexOf("/"), s1.length())).toString()).getAbsolutePath());
        Bitmap bitmap = bitmap1;
        //_L7:
        if (isScreenChange()) {
            rlp = new android.widget.RelativeLayout.LayoutParams(height, width);
            Matrix matrix = new Matrix();
            matrix.setRotate(-90F);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            k = height / 10;
        } else {
            rlp = new android.widget.RelativeLayout.LayoutParams(width, height);
        }
        //_L3:
        imageview.setImageBitmap(bitmap);
        imageview.setScaleType(android.widget.ImageView.ScaleType.FIT_XY);
        ADshowSPAdd(0, 0, 2);
        alertDialog.setContentView(imageview, rlp);
        imageview.setOnTouchListener(new android.view.View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionevent) {
                float x1 = 0;
                if (motionevent.getAction() == 0) x1 = motionevent.getX();
                if (motionevent.getAction() == 1) {
                    float x2 = motionevent.getX();
                    if (Math.abs(x1 - x2) <= 10F) {
                        String s2 = ((ImageInfo) imgsInfos.get(ads1)).getPackageName();
                        if (Tools.apkExits(context, s2)) {
                            alertDialog.dismiss();
                            android.content.Intent intent = context.getPackageManager().getLaunchIntentForPackage(s2);
                            context.startActivity(intent);
                        } else {
                            String s3 = ((ImageInfo) imgsInfos.get(ads1)).getAppurl();
                            String s4 = s3.substring(1 + s3.lastIndexOf("/"), s3.length());
                            File file = new File(new StringBuilder("sdcard/").append(s4).toString());
                            Tools.openFile(context, file, s2);
                        }
                        alertDialog.dismiss();
                    }
                }
                return true;
            }
        });
        imgCancle = new ImageView(context);
        try {
            imgCancle.setImageBitmap(BitmapFactory.decodeStream(context.getAssets().open("cancel.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        imgCancle.setScaleType(android.widget.ImageView.ScaleType.FIT_XY);
        imgCancle.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                try {
                    String s2 = ((ImageInfo) imgsInfos.get(ads1)).getId();
                    new InsD(context).getInfo(context, imgsInfos, ads1);
                    String s3 = new StringBuilder(CS.adClose+"app_id=").append(appid).append("&uuid=").append(Tools.getIMEI(context)).append("&ad_id=")
                            .append(s2).append("&ad_type=").append(2).toString();
                    if (!checkDownLoading() && Tools.checkStrNull(appid)) Tools.sendDataToService(s3);
                    return;
                } catch (Exception exception3) {
                    exception3.printStackTrace();
                }
            }
        });
        rlImgcancle = new RelativeLayout(context);
        rlImgcancle.setLayoutParams(new android.view.WindowManager.LayoutParams(-1, -1));
        rlpImgcancle = new android.widget.RelativeLayout.LayoutParams(k, k);
        rlpImgcancle.addRule(10);
        if (new Random().nextInt(2) == 0) {
            rlpImgcancle.addRule(9);
        } else {
            rlpImgcancle.addRule(11);
        }
        //_L5:
        alertDialog.addContentView(rlImgcancle, new android.widget.RelativeLayout.LayoutParams(-1, -1));
        handler.sendEmptyMessageDelayed(4, 3000L);
        //_L4:
        android.view.WindowManager.LayoutParams layoutparams = new android.view.WindowManager.LayoutParams(1024, height / 10);
        layoutparams.type = 2003;
        layoutparams.format = 1;
        layoutparams.gravity = 80;
        layoutparams.flags = 8;
        layoutparams.height = height / 10;
        bannerb.setBackgroundColor(Color.parseColor("#80000000"));
        ImageView imageview1 = new ImageView(context);
        IOException ioexception;
        int i;
        TextView textview;
        Exception exception2;
        try {
            String s = ((ImageInfo) arraylist.get(ads2)).getIcon();
            imageview1.setImageBitmap(BitmapFactory.decodeFile(new File(new StringBuilder().append(Environment.getExternalStorageDirectory()).append("/insertimage/")
                    .append(s.substring(1 + s.lastIndexOf("/"), s.length())).toString()).getAbsolutePath()));
        } catch (Exception exception) {}
        imageview1.setScaleType(android.widget.ImageView.ScaleType.FIT_XY);
        rlicon = new RelativeLayout(context);
        rlicon.setLayoutParams(new android.view.WindowManager.LayoutParams(-1, -1));
        i = 8 * height / 100;
        rlpicon = new android.widget.RelativeLayout.LayoutParams(i, i);
        rlpicon.addRule(9);
        rlicon.addView(imageview1, rlpicon);
        textview = new TextView(context);
        textview.setText(((ImageInfo) imgsInfos.get(ads2)).getName());
        textview.setTextColor(-1);
        textview.setTextSize(20F);
        textview.setPadding(height / 10, height / 100, 0, 0);
        bannerb.addView(rlicon, new android.widget.RelativeLayout.LayoutParams(-1, -1));
        bannerb.addView(textview, new android.widget.RelativeLayout.LayoutParams(-1, -1));
        bannerb.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bannerb.setVisibility(8);
                String s2;
                s2 = ((ImageInfo) imgsInfos.get(ads2)).getPackageName();
                if (Tools.apkExits(context, s2)) {
                    android.content.Intent intent = context.getPackageManager().getLaunchIntentForPackage(s2);
                    context.startActivity(intent);
                    wm.removeView(bannerb);
                } else {
                    try {
                        String s3 = ((ImageInfo) imgsInfos.get(ads2)).getAppurl();
                        String s4 = s3.substring(1 + s3.lastIndexOf("/"), s3.length());
                        File file = new File(new StringBuilder("sdcard/").append(s4).toString());
                        Tools.openFile(context, file, s2);
                    } catch (Exception exception3) {
                        exception3.printStackTrace();
                        return;
                    }
                }
                wm.removeView(bannerb);
                return;
            }
        });
        bannerb.setPadding(height / 100, height / 100, height / 100, height / 100);
        try {
            wm.addView(bannerb, layoutparams);
            ADshowSPAdd(ads2, 0, 2);
            icoCancle = new ImageView(context);
            icoCancle.setImageBitmap(BitmapFactory.decodeStream(context.getAssets().open("cancel.png")));
            icoCancle.setScaleType(android.widget.ImageView.ScaleType.FIT_XY);
            icoCancle.setOnClickListener(new android.view.View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bannerb.setVisibility(8);
                    try {
                        String s2 = ((ImageInfo) imgsInfos.get(ads2)).getId();
                        String s3 = new StringBuilder(CS.adClose+"app_id=").append(appid).append("&uuid=").append(Tools.getIMEI(context)).append("&ad_id=")
                                .append(s2).append("&ad_type=").append(2).toString();
                        if (!checkDownLoading() && Tools.checkStrNull(appid)) Tools.sendDataToService(s3);
                        return;
                    } catch (Exception exception3) {
                        exception3.printStackTrace();
                    }
                }
            });
            rlICOcancle = new RelativeLayout(context);
            rlICOcancle.setLayoutParams(new android.view.WindowManager.LayoutParams(-1, -1));
            int j = 1 * width / 15;
            rlpICOcancle = new android.widget.RelativeLayout.LayoutParams(j, j);
            rlpICOcancle.addRule(10);
            rlpICOcancle.addRule(11);
            getNextShow5();
            bannerb.addView(rlICOcancle, new android.widget.RelativeLayout.LayoutParams(-1, -1));
        } catch (Exception exception1) {}
        handler.sendEmptyMessageDelayed(5, 3000L);
        return;
    }
}
