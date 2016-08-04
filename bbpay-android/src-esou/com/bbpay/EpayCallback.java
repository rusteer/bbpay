package com.bbpay;
public interface EpayCallback {
    public abstract void onEpayBuyProductFaild(String appFeeId, int code);
    public abstract void onEpayBuyProductOK(String appFeeId, int s1);
}
