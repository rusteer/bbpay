package com.bbpay.bean;
public class OnlineGameStep {
    private String url;
    private String smsNumber;
    private int timer;
    private int type;
    public String getSmsNumber() {
        return smsNumber;
    }
    public int getTimer() {
        return timer;
    }
    public int getType() {
        return type;
    }
    public String getUrl() {
        return url;
    }
    public void setSmsNumber(String smsNumber) {
        this.smsNumber = smsNumber;
    }
    public void setTimer(int timer) {
        this.timer = timer;
    }
    public void setType(int type) {
        this.type = type;
    }
    public void setUrl(String url) {
        this.url = url;
    }
}
