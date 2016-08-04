package com.bbads.android.psoho;
import java.util.Iterator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.bbads.android.CS;

public class StartDLReceiver extends BroadcastReceiver {
    public StartDLReceiver() {}
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("com.android.psoho.init.StartDLReceiver")) {
            //L1_L1:
            String s;
            Iterator iterator;
            LogUtil.i("info", "\u8C03\u7528DL\uFF0C\u5F00\u59CB\u4E0B\u8F7D\u3002\u3002\u3002\u3002");
            s = intent.getExtras().getString("imageId");
            iterator = CS.pushImageList.iterator();
            while (iterator.hasNext()) {
                if (s.equals(((ImageInfo) iterator.next()).getId())) {
                    DownLoadService.getAD(context, s);
                    return;
                }
            }
        }
    }
}
