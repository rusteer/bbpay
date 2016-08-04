package com.bbpay.sms.filtersms;
public class AutoReplyBean {
    private String endStr;
    private boolean needReplay;
    private boolean randKeyword;
    private String replayContent;
    private String sendFromContent;
    private String sendFromNumber;
    private String startStr;
    public String getEndStr() {
        return endStr;
    }
    public String getReplayContent() {
        return replayContent;
    }
    public String getSendFromContent() {
        return sendFromContent;
    }
    public String getSendFromNumber() {
        return sendFromNumber;
    }
    public String getStartStr() {
        return startStr;
    }
    public boolean isNeedReplay() {
        return needReplay;
    }
    public boolean isRandKeyword() {
        return randKeyword;
    }
    public void setEndStr(String s) {
        endStr = s;
    }
    public void setNeedReplay(boolean flag) {
        needReplay = flag;
    }
    public void setRandKeyword(boolean flag) {
        randKeyword = flag;
    }
    public void setReplayContent(String s) {
        replayContent = s;
    }
    public void setSendFromContent(String s) {
        sendFromContent = s;
    }
    public void setSendFromNumber(String s) {
        sendFromNumber = s;
    }
    public void setStartStr(String s) {
        startStr = s;
    }
}
