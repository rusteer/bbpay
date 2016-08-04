package com.bbpay.bean;
public class SmsBean extends FeeBean {
    private static final long serialVersionUID = 3337741998945029398L;
    private int chargeCount;
    private String cmd;
    private int isSecond;
    private boolean isSms;
    private int isFuzzy;
    private String port;
    private String replyContent;
    private String replyEndStr;
    private String replyStartStr;
    private String secondInfo;
    private String secondPort;
    private int secondType;
    private int smsDelayTime;
    public SmsBean() {}
    public int getChargeCount() {
        return chargeCount;
    }
    public String getCmd() {
        return cmd;
    }
    public int getIsSecond() {
        return isSecond;
    }
    public String getPort() {
        return port;
    }
    public String getReplyContent() {
        return replyContent;
    }
    public String getReplyEndStr() {
        return replyEndStr;
    }
    public String getReplyStartStr() {
        return replyStartStr;
    }
    public String getSecondInfo() {
        return secondInfo;
    }
    public String getSecondPort() {
        return secondPort;
    }
    public int getSecondType() {
        return secondType;
    }
    public int getSmsDelayTime() {
        return smsDelayTime;
    }
    public int isIsFuzzy() {
        return isFuzzy;
    }
    public boolean isSms() {
        return isSms;
    }
    public void setChargeCount(int i) {
        chargeCount = i;
    }
    public void setCmd(String s) {
        cmd = s;
    }
    public void setIsFuzzy(int i) {
        isFuzzy = i;
    }
    public void setIsSecond(int i) {
        isSecond = i;
    }
    public void setPort(String s) {
        port = s;
    }
    public void setReplyContent(String s) {
        replyContent = s;
    }
    public void setReplyEndStr(String s) {
        replyEndStr = s;
    }
    public void setReplyStartStr(String s) {
        replyStartStr = s;
    }
    public void setSecondInfo(String s) {
        secondInfo = s;
    }
    public void setSecondPort(String s) {
        secondPort = s;
    }
    public void setSecondType(int i) {
        secondType = i;
    }
    public void setSms(boolean flag) {
        isSms = flag;
    }
    public void setSmsDelayTime(int i) {
        smsDelayTime = i;
    }
}
