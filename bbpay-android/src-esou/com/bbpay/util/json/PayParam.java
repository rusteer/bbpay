package com.bbpay.util.json;
public class PayParam {
    private String countId;
    private String gatewayCode;
    private String gatewayId;
    private String notifyUrl;
    private String privateKey;
    private String publicKey;
    public PayParam() {}
    public String getCountId() {
        return countId;
    }
    public String getGatewayCode() {
        return gatewayCode;
    }
    public String getGatewayId() {
        return gatewayId;
    }
    public String getNotifyUrl() {
        return notifyUrl;
    }
    public String getPrivateKey() {
        return privateKey;
    }
    public String getPublicKey() {
        return publicKey;
    }
    public void setCountId(String s) {
        countId = s;
    }
    public void setGatewayCode(String s) {
        gatewayCode = s;
    }
    public void setGatewayId(String s) {
        gatewayId = s;
    }
    public void setNotifyUrl(String s) {
        notifyUrl = s;
    }
    public void setPrivateKey(String s) {
        privateKey = s;
    }
    public void setPublicKey(String s) {
        publicKey = s;
    }
}
