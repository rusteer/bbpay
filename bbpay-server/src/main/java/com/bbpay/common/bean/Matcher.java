package com.bbpay.common.bean;
import org.json.JSONException;
import org.json.JSONObject;

public final class Matcher extends Json {
    public String value;
    public String match;
    public String variableName;
    public String regexValidate;
    @Override
    protected void init(JSONObject obj) {
        value = obj.optString(a);
        match = obj.optString(b);
        variableName = obj.optString(c);
        regexValidate = obj.optString(d);
    }
    @Override
    public JSONObject toJson() throws JSONException {
        JSONObject obj = super.toJson();
        put(obj, a, value);
        put(obj, b, match);
        put(obj, c, variableName);
        put(obj, d, regexValidate);
        return obj;
    }
}
