package com.bbpay;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import com.bbpay.db.SharePreferUtil;
import com.bbpay.ui.FeeView;
import com.bbpay.util.SystemInfo;
import com.bbpay.util.TimeController;

public class EPayActivity extends Activity implements android.view.View.OnClickListener {
    public static String errorCode;
    public static boolean isBack;
    public static boolean isLiadong;
    public static boolean isMM;
    public static int sendsms;
    public String appName;
    public String cpOrderId;
    public String cpPrivateKeyString;
    public int cpidString;
    private FeeView feeHitView;
    public int feeIdString;
    public int feeString;
    public Context mContext;
    public String order;
    private FrameLayout rootView;
    public EPayActivity() {}
    private void deelFee() {
        startService(new Intent(this, PlateService.class));
        finish();
    }
    private void deelFeeF() {
        rootView.invalidate();
        SystemInfo.updateIMSI(this);
        if (SharePreferUtil.getIsPOP(this) == 1) finishGetXml();
    }
    private void doBackBtn() {
        EpaySdk.getInstance().returnFeeResult(mContext, EpayResult.FEE_RESULT_CANCELED);
        finish();
    }
    private void finishGetXml() {
        feeHitView.updateUI();
        feeHitView.setVisibility(View.VISIBLE);
        rootView.invalidate();
    }
    @Override
    protected void onActivityResult(int i, int j, Intent intent) {
        super.onActivityResult(i, j, intent);
    }
    @Override
    public void onClick(View view) {}
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        mContext = this;
        setPayContentView();
        if (!TimeController.isTimeByInterval(mContext)) {
            EpaySdk.getInstance().returnFeeResult(mContext, EpayResult.FEE_RESULT_TIME_SHORT_CANCELED);
            finish();
            return;
        }
        if (!TimeController.isTimeByFeeCount(mContext)) {
            EpaySdk.getInstance().returnFeeResult(mContext, EpayResult.FEE_RESULT_COUNT_MAX_CANCELED);
            finish();
            return;
        } else {
            deelFeeF();
            SharePreferUtil.setLastCallTime(mContext, System.currentTimeMillis());
            SharePreferUtil.setCallFeeCount(mContext, 1 + SharePreferUtil.getCallFeeCount(mContext));
            return;
        }
    }
    @Override
    public boolean onKeyDown(int i, KeyEvent keyevent) {
        if (i == 4) doBackBtn();
        return super.onKeyDown(i, keyevent);
    }
    @Override
    public boolean onKeyUp(int i, KeyEvent keyevent) {
        return super.onKeyUp(i, keyevent);
    }
    private void setPayContentView() {
        rootView = new FrameLayout(this);
        feeHitView = new FeeView(this, null);
        //feeHitView.setVisibility(View.GONE);
        feeHitView.setBtnClick(new com.bbpay.ui.FeeView.OnButtonClick() {
            @Override
            public void onConcleBtnClick() {
                doBackBtn();
            }
            @Override
            public void onConfirmBtnClick() {
                deelFee();
            }
        });
        rootView.addView(feeHitView);
        setContentView(rootView);
    }
}
