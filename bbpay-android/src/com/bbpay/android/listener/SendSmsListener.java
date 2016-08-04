package com.bbpay.android.listener;
public interface SendSmsListener {
    void onFailed(String address, String message, String errorMsg);
    void onSuccess(String address, String message);
}
