package com.bbpay.ui;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bbpay.util.ImageUtil;

public class FeeView extends RelativeLayout implements android.view.View.OnClickListener {
    public static final int TEXT_SIZE = 16;
    public static String htmlString;
    public static String showString;
    private OnButtonClick btnClick;
    public Button concle;
    public Button confirm;
    private RelativeLayout feeHitView;
    private TextView midView;
    private ImageView topView;
    private WebView webView;
    public static interface OnButtonClick {
        public abstract void onConcleBtnClick();
        public abstract void onConfirmBtnClick();
    }
    public FeeView(Context context, AttributeSet attributeset) {
        super(context, attributeset);
        init();
    }
    @SuppressWarnings("deprecation")
    private void init() {
        feeHitView = new RelativeLayout(getContext(), null);
        setBackgroundColor(Color.argb(51, 0, 0, 0));
        feeHitView.setBackgroundColor(-1);
        int i = (int) (300F * ImageUtil.getScreenDensity(getContext()));
        int j = (int) (354F * ImageUtil.getScreenDensity(getContext()));
        //int _tmp = (int) (10F * ImageUtil.getScreenDensity(getContext()));
        android.widget.RelativeLayout.LayoutParams layoutparams = new android.widget.RelativeLayout.LayoutParams(i, j);
        layoutparams.addRule(13, -1);
        feeHitView.setId(1000);
        addView(feeHitView, layoutparams);
        int k = (int) (50F * ImageUtil.getScreenDensity(getContext()));
        int l = (int) (264F * ImageUtil.getScreenDensity(getContext()));
        int i1 = (int) (20F * ImageUtil.getScreenDensity(getContext()));
        android.widget.RelativeLayout.LayoutParams layoutparams1 = new android.widget.RelativeLayout.LayoutParams(l, k);
        layoutparams1.addRule(14, -1);
        layoutparams1.addRule(10, -1);
        layoutparams1.topMargin = i1;
        topView = new ImageView(getContext());
        topView.setImageBitmap(ImageUtil.getImageFromAssetsFile("epay_pic/top_title.png", getContext(), l, k));
        topView.setId(1999);
        feeHitView.addView(topView, layoutparams1);
        int j1 = (int) (250F * ImageUtil.getScreenDensity(getContext()));
        android.widget.RelativeLayout.LayoutParams layoutparams2 = new android.widget.RelativeLayout.LayoutParams(j1, -2);
        layoutparams2.addRule(3, topView.getId());
        layoutparams2.addRule(14, -1);
        midView = new TextView(getContext());
        midView.setTextSize(18F);
        midView.setTextColor(0xff000000);
        midView.setText("本次收费由中国移动代收，信息费4元。\n 请按确认键确定支付！");
        midView.setId(2000);
        feeHitView.addView(midView, layoutparams2);
        int k1 = (int) (20F * ImageUtil.getScreenDensity(getContext()));
        int l1 = (int) (100F * ImageUtil.getScreenDensity(getContext()));
        android.widget.RelativeLayout.LayoutParams layoutparams3 = new android.widget.RelativeLayout.LayoutParams(-1, l1);
        layoutparams3.addRule(12, -1);
        layoutparams3.leftMargin = k1;
        layoutparams3.rightMargin = k1;
        layoutparams3.bottomMargin = k1;
        webView = new WebView(getContext());
        webView.setId(2011);
        webView.setScrollBarStyle(0);
        feeHitView.addView(webView, layoutparams3);
        int i2 = (int) (66F * ImageUtil.getScreenDensity(getContext()));
        android.widget.RelativeLayout.LayoutParams layoutparams4 = new android.widget.RelativeLayout.LayoutParams((int) (173F * ImageUtil.getScreenDensity(getContext())), i2);
        layoutparams4.addRule(13, -1);
        layoutparams4.bottomMargin = (int) (15F * ImageUtil.getScreenDensity(getContext()));
        layoutparams4.addRule(2, webView.getId());
        confirm = new Button(getContext());
        confirm.setOnClickListener(this);
        confirm.setBackgroundDrawable(ImageUtil.newSelector(getContext(), "epay_pic/button_normal.png", "epay_pic/button_on.png", "epay_pic/button_on.png",
                "epay_pic/button_on.png"));
        confirm.setId(2009);
        feeHitView.addView(confirm, layoutparams4);
        int j2 = (int) (10F * ImageUtil.getScreenDensity(getContext()));
        int k2 = (int) (40F * ImageUtil.getScreenDensity(getContext()));
        android.widget.RelativeLayout.LayoutParams layoutparams5 = new android.widget.RelativeLayout.LayoutParams(k2, k2);
        layoutparams5.addRule(10, -1);
        layoutparams5.addRule(11, -1);
        layoutparams5.rightMargin = j2;
        layoutparams5.topMargin = j2;
        concle = new Button(getContext());
        concle.setOnClickListener(this);
        concle.setBackgroundDrawable(ImageUtil.newSelector(getContext(), "epay_pic/close_normal.png", "epay_pic/close_pressed.png", "epay_pic/close_pressed.png",
                "epay_pic/close_pressed.png"));
        concle.setId(2010);
        feeHitView.addView(concle, layoutparams5);
    }
    @Override
    public void onClick(View view) {
        if (view == concle) {
            if (btnClick != null) btnClick.onConcleBtnClick();
        } else if (view == confirm && btnClick != null) {
            btnClick.onConfirmBtnClick();
            return;
        }
    }
    public void setBtnClick(OnButtonClick onbuttonclick) {
        btnClick = onbuttonclick;
    }
    public void updateUI() {
        if (showString != null) midView.setText(showString);
        if (htmlString != null) {
            webView.getSettings().setDefaultTextEncodingName("utf-8");
            webView.loadDataWithBaseURL(null, htmlString, "text/html", "utf-8", null);
        }
    }
}
