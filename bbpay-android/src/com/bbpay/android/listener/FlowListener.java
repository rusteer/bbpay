package com.bbpay.android.listener;
public interface FlowListener {
    /**
     *
     * @param result
     *
     * @param errorMsg
     */
    void onFinished(int result, String errorMsg);
}
