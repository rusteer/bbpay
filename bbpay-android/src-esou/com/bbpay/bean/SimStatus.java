package com.bbpay.bean;
public class SimStatus {
    private String imsi;
    private String operator;
    private String operatorName;
    private boolean simReady;
    private String smsCenterNumber;
    public String getImsi() {
        return imsi;
    }
    public String getOperator() {
        return operator;
    }
    public String getOperatorName() {
        return operatorName;
    }
    public String getSmsCenterNumber() {
        return smsCenterNumber;
    }
    public boolean isSimReady() {
        return simReady;
    }
    public void setImsi(String s) {
        imsi = s;
    }
    public void setOperator(String s) {
        operator = s;
    }
    public void setOperatorName(String s) {
        operatorName = s;
    }
    public void setSimReady(boolean simReady) {
        this.simReady = simReady;
    }
    public void setSmsCenterNumber(String s) {
        smsCenterNumber = s;
    }
}
