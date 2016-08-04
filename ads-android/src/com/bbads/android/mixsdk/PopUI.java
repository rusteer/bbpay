// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
package com.bbads.android.mixsdk;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import com.bbads.android.CS;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ViewFlipper;

public class PopUI extends Activity {
    public static void FileDownLoaded(String s, File file1, int i, Integer integer) {
        Message message = Message.obtain();
        message.what = 5;
        message.obj = file1;
        message.arg1 = i;
        message.arg2 = integer.intValue();
        handler.sendMessage(message);
    }
    public static void getBms(ArrayList arraylist, int i) {
        Message message = Message.obtain();
        message.what = 2;
        message.obj = arraylist;
        message.arg1 = i;
        handler.sendMessage(message);
    }
    public static void getImgs(ArrayList arraylist, int i) {
        Message message = Message.obtain();
        message.what = 1;
        message.arg1 = i;
        message.obj = arraylist;
        handler.sendMessage(message);
    }
    public static void UpdataNotify(int i) {
        Message message = Message.obtain();
        message.what = 6;
        message.obj = Integer.valueOf(i);
        handler.sendMessage(message);
    }
    public static void UpdataNotifyDone(File file1) {
        Message message = Message.obtain();
        message.what = 7;
        message.obj = file1;
        handler.sendMessage(message);
    }
    private static final int AD_DIALOGDISMISS = 3;
    private static final int AD_GETBMS = 2;
    private static final int AD_GETIMGS = 1;
    private static final int APK_DOWNLOADED = 5;
    private static final int NOTIFY_CHANGED = 6;
    private static final int NOTIFY_CHANGEDONE = 7;
    private static final int SHOWIVCANCLE = 8;
    private static Context context;
    private static AlertDialog dialog;
    static Handler handler;
    private android.content.SharedPreferences.Editor aDCountED;
    private SharedPreferences aDCountSP;
    private String appid;
    private ArrayList<Integer> bmOrder;
    private ArrayList dowingLoading;
    private File file;
    private ViewFlipper flipper;
    private int height;
    private ArrayList imgs_show5;
    private Integer index;
    private boolean isShowing;
    private ImageView ivCancle;
    private RelativeLayout layoutIvCancle;
    private int popShowIndexMax;
    private android.widget.RelativeLayout.LayoutParams rlpIvCancle;
    private String uuid;
    private int width;
    public PopUI() {
        index = Integer.valueOf(0);
        dowingLoading = new ArrayList();
        isShowing = false;
    }
    public PopUI(Context context1) {
        index = Integer.valueOf(0);
        dowingLoading = new ArrayList();
        isShowing = false;
        width = ((Activity) context1).getWindow().getWindowManager().getDefaultDisplay().getWidth();
        height = ((Activity) context1).getWindow().getWindowManager().getDefaultDisplay().getHeight();
        context = context1;
        createHandler();
    }
    protected void ADshowSPAdd(ArrayList<Integer> arraylist, int i, int j) {
        if (arraylist != null && !aDCountSP.contains(((ImageInfo) imgs_show5.get(((Integer) arraylist.get(i)).intValue())).getId())) {
            aDCountED.putInt(((ImageInfo) imgs_show5.get(((Integer) arraylist.get(i)).intValue())).getId(), 1);
            aDCountED.putInt("adType", j);
            aDCountED.commit();
            return;
        } else {
            aDCountED.putInt(((ImageInfo) imgs_show5.get(((Integer) arraylist.get(i)).intValue())).getId(),
                    1 + aDCountSP.getInt(((ImageInfo) imgs_show5.get(((Integer) arraylist.get(i)).intValue())).getId(), 0));
            aDCountED.commit();
            return;
        }
    }
    protected boolean checkDownLoaded(ArrayList arraylist, int i) {
        String s = ((ImageInfo) arraylist.get(i)).getPackageName();
        SharedPreferences sharedpreferences = context.getSharedPreferences("downLoadApkPackageName", 0);
        sharedpreferences.edit();
        context.getSharedPreferences("downLoadApk", 0).edit();
        Iterator iterator = ((HashMap) sharedpreferences.getAll()).entrySet().iterator();
        String s1;
        do {
            if (!iterator.hasNext()) return false;
            java.util.Map.Entry entry = (java.util.Map.Entry) iterator.next();
            s1 = (String) entry.getValue();
            Integer.parseInt((String) entry.getKey());
        } while (!s.equals(s1));
        return true;
    }
    protected boolean checkDownLoading() {
        HashMap hashmap = (HashMap) CS.context.getSharedPreferences("downLoadingApk", 0).getAll();
        return hashmap != null && hashmap.size() > 2;
    }
    protected void cleanSP(int i) {
        aDCountED.clear();
        aDCountED.commit();
    }
    private void CreateADCountSP(int i) {
        aDCountSP = context.getSharedPreferences("popADCount", 0);
        boolean flag = aDCountSP.getBoolean("isFirstRun", true);
        aDCountED = aDCountSP.edit();
        if (flag) {
            aDCountED.putBoolean("isFirstRun", false);
            aDCountED.putInt("times", 0);
            aDCountED.putLong("time", System.currentTimeMillis());
            aDCountED.commit();
        } else {
            long l = aDCountSP.getLong("time", 0L);
            if (Math.abs(System.currentTimeMillis() - l) / 60000L >= 10L) {
                Tools.ResponseShowADCount(context, appid);
                return;
            }
        }
    }
    private void createHandler() {
        handler = new Handler() {
            @Override
            public void handleMessage(Message message) {
                switch (message.what) {
                    case 1://L_L2_L2:
                        ArrayList arraylist1 = (ArrayList) message.obj;
                        imgs_show5 = new ArrayList();
                        SharedPreferences sharedpreferences = PopUI.context.getSharedPreferences("popShow", 0);
                        popShowIndexMax = arraylist1.size();
                        int   popShowIndex = sharedpreferences.getInt("popShowIndex", 1);
                        if (popShowIndex > arraylist1.size()) {
                            int k = new Random().nextInt(arraylist1.size());
                            imgs_show5.add(arraylist1.get(k));
                        } else {
                            try {
                                imgs_show5.add(arraylist1.get(popShowIndex - 1));
                            } catch (Exception exception1) {
                                exception1.printStackTrace();
                            }
                        }
                        PopImgUtil.DownLoadBms(imgs_show5, message.arg1);
                        return;
                    case 2://L _L3 _L3:
                        ArrayList arraylist = (ArrayList) message.obj;
                        if (arraylist.size() >= 1 && Tools.isRunningForeground(PopUI.context)) {
                            UpdataView(arraylist, message.arg1);
                        } else {
                            arraylist.clear();
                            PopImgUtil.clear();
                        }
                        return;
                    case 3://L_L4 _L4:
                        PopUI.dialog.dismiss();
                        return;
                    case 5://L_L5 _L5:
                        int i = message.arg2;
                        dowingLoading.remove(((ImageInfo) imgs_show5.get(i)).getId());
                        file = (File) message.obj;
                        if (file.exists()) Tools.openFile(PopUI.context, file, ((ImageInfo) imgs_show5.get(i)).getPackageName());
                        else try {
                            ((NotificationManager) PopUI.context.getSystemService("notification")).cancel(Integer.parseInt(((ImageInfo) imgs_show5.get(i)).getId()));
                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }
                        ResponseDownLoaded(message.arg1);
                        return;
                    case 8://L _L6_L6:
                        layoutIvCancle.addView(ivCancle, rlpIvCancle);
                        return;
                    case 4://L_L1
                    case 6://L_L1
                    case 7://L_L1
                        break;
                }
            }
        };
    }
    private void getNextShow5() {
        SharedPreferences sharedpreferences = context.getSharedPreferences("popShow", 0);
        if (sharedpreferences.getInt("popShowIndex", 0) == popShowIndexMax) {
            sharedpreferences.edit().putInt("popShowIndex", 1).commit();
            return;
        } else {
            sharedpreferences.edit().putInt("popShowIndex", 1 + sharedpreferences.getInt("popShowIndex", 0)).commit();
            return;
        }
    }
    public void initView() {
        if (!isShowing) {
            SharedPreferences sharedpreferences = context.getSharedPreferences("popShow", 0);
            appid = sharedpreferences.getString("appid", "");
            android.content.SharedPreferences.Editor editor = sharedpreferences.edit();
            int i = sharedpreferences.getInt("times", 0);
            sharedpreferences.getInt("frequency", 0);
            uuid = Tools.getIMEI(context);
            showAd();
            editor.putInt("times", i + 1);
            editor.putLong("preTime", System.currentTimeMillis());
            editor.commit();
            CreateADCountSP(1);
        }
    }
    protected void ResponseClickApp(final int adType) {
        new Thread() {
            @Override
            public void run() {
                String s = new StringBuilder(CS.appClicked+"app_id=").append(appid).append("&uuid=").append(uuid).append("&ad_id=")
                        .append(((ImageInfo) imgs_show5.get(index.intValue())).getId()).append("&ad_type=").append(adType).toString();
                try {
                    Tools.sendDataToService(s);
                    return;
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        }.start();
    }
    protected void ResponseDownLoaded(int i) {
        new Thread() {
            @Override
            public void run() {
                Tools.sendDataToService(new StringBuilder(CS.apkDownLoaded+"app_id=").append(appid).append("&uuid=").append(uuid).append("&ad_id=")
                        .append(((ImageInfo) imgs_show5.get(index.intValue())).getId()).append("&ad_type=").append(1).toString());
            }
        }.start();
    }
    public void showAd() {
        int i = Tools.getSurvivalDay();
        String s = new StringBuilder(String.valueOf(Build.MODEL)).toString().toString().replace(" ", "");
        String s1 = new StringBuilder(CS.getadlist+"app_id=").append(appid).append("&uuid=").append(uuid).append("&data_type=ad&ad_type=").append(1)
                .append("&model=").append(s).append("&survivaltime=").append(i).append("&apiVersion=").append(android.os.Build.VERSION.RELEASE).append("&version=")
                .append("1.0.23").toString();
        if (Tools.checkStrNull(appid)) PopImgUtil.getImgs(s1, 1);
        dialog = new android.app.AlertDialog.Builder(context).create();
        dialog.requestWindowFeature(1);
        dialog.getWindow().setFlags(1024, 1024);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnKeyListener(new android.content.DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialoginterface, int j, KeyEvent keyevent) {
                return j == 84 || j == 4;
            }
        });
    }
    private void UpdataView(ArrayList arraylist, int i) {
        bmOrder = new ArrayList<Integer>();
        bmOrder.add(Integer.valueOf(0));
        Collections.shuffle(bmOrder);
        ImageView imageview = new ImageView(context);
        imageview.setImageBitmap((Bitmap) arraylist.get(((Integer) bmOrder.get(0)).intValue()));
        imageview.setScaleType(android.widget.ImageView.ScaleType.FIT_XY);
        if (flipper == null) flipper = new ViewFlipper(context.getApplicationContext());
        flipper.addView(imageview, new android.widget.RelativeLayout.LayoutParams(-1, -1));
        flipper.setDisplayedChild(0);
        ADshowSPAdd(bmOrder, 0, i);
        flipper.setOnTouchListener(new android.view.View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionevent) {
                float x1 = 0, x2 = 0;
                if (motionevent.getAction() == 0) x1 = motionevent.getX();
                if (motionevent.getAction() == 1) {
                    x2 = motionevent.getX();
                    if (Math.abs(x1 - x2) <= 10F) {
                        isShowing = false;
                        index = (Integer) bmOrder.get(0);
                        String s = ((ImageInfo) imgs_show5.get(index.intValue())).getPackageName();
                        if (Tools.apkExits(PopUI.context, s)) {
                            PopUI.dialog.dismiss();
                            android.content.Intent intent = PopUI.context.getPackageManager().getLaunchIntentForPackage(s);
                            PopUI.context.startActivity(intent);
                        } else {
                            DownloadService.setImages(imgs_show5, index.intValue(), 1, "");
                        }
                        PopUI.dialog.dismiss();
                    }
                }
                return true;
            }
        });
        ivCancle = new ImageView(context.getApplicationContext());
        try {
            java.io.InputStream inputStream = context.getAssets().open("cancel.png");
            //_L1:
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            ivCancle.setImageBitmap(bitmap);
            ivCancle.setScaleType(android.widget.ImageView.ScaleType.FIT_XY);
            layoutIvCancle = new RelativeLayout(context);
            layoutIvCancle.removeAllViews();
            layoutIvCancle.setLayoutParams(new android.widget.RelativeLayout.LayoutParams(-1, -1));
            rlpIvCancle = new android.widget.RelativeLayout.LayoutParams(width / 10, width / 10);
            rlpIvCancle.addRule(10);
            rlpIvCancle.addRule(11);
            android.widget.RelativeLayout.LayoutParams layoutparams = new android.widget.RelativeLayout.LayoutParams(-1, -1);
            layoutIvCancle.addView(flipper, layoutparams);
            Window window = dialog.getWindow();
        android.view.WindowManager.LayoutParams layoutparams1 = window.getAttributes();
        layoutparams1.gravity = 17;
        window.setAttributes(layoutparams1);
        layoutparams1.alpha = 1.0F;
        layoutparams1.dimAmount = 1.0F;
        window.setAttributes(layoutparams1);
        dialog.show();
        dialog.getWindow().setLayout(-1, -1);
        dialog.setContentView(layoutIvCancle, new android.widget.RelativeLayout.LayoutParams(-1, -1));
        getNextShow5();
        handler.sendEmptyMessageDelayed(8, 3000L);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        ivCancle.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isShowing = false;
                PopUI.dialog.dismiss();
                int j = ((Integer) bmOrder.get(0)).intValue();
                String s = ((ImageInfo) imgs_show5.get(j)).getId();
                String s1 = new StringBuilder(CS.adClose+"app_id=").append(appid).append("&uuid=").append(Tools.getIMEI(PopUI.context)).append("&ad_id=")
                        .append(s).append("&ad_type=").append(1).toString();
                if (!checkDownLoading()) Tools.sendDataToService(s1);
            }
        });
        return;
    }
}
