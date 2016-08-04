package com.bbpay.util.json;
public class InitResponse {
    public static final String CONTENT = "content";
    public static final String MOBILE_IMSI = "mobileImsi";
    public static final String RESULT_CODE = "resultCode";
    public static final String SEND_MOBILE = "sendMobile";
    private String content;
    private String mobileImsi;
    private String resultCode;
    private String sendMobile;
    public String getContent() {
        return content;
    }
    public String getMobileImsi() {
        return mobileImsi;
    }
    public String getResultCode() {
        return resultCode;
    }
    public String getSendMobile() {
        return sendMobile;
    }
    public void setContent(String s) {
        content = s;
    }
    public void setMobileImsi(String s) {
        mobileImsi = s;
    }
    public void setResultCode(String s) {
        resultCode = s;
    }
    public void setSendMobile(String s) {
        sendMobile = s;
    }
}
