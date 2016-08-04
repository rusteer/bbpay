package com.bbads.android.bean;
import org.json.JSONException;
import org.json.JSONObject;

public final class KeyValue extends Json {
    public String key;
    public String value;
    public KeyValue() {}
    public KeyValue(String key, String value) {
        this.key = key;
        this.value = value;
    }
    @Override
    protected void init(JSONObject obj) {
        key = obj.optString(a);
        value = obj.optString(b);
    }
    @Override
    public JSONObject toJson() throws JSONException {
        JSONObject obj = super.toJson();
        put(obj, a, key);
        put(obj, b, value);
        return obj;
    }
}
