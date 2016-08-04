package com.bbpay.util;
public class KeyValue {
    private String key;
    private String value;
    public KeyValue() {}
    public KeyValue(String s, String s1) {
        key = s;
        value = s1;
    }
    public String getKey() {
        return key;
    }
    public String getValue() {
        return value;
    }
    public void setKey(String s) {
        key = s;
    }
    public void setValue(String s) {
        value = s;
    }
}
