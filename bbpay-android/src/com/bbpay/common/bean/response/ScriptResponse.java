package com.bbpay.common.bean.response;
import org.json.JSONException;
import org.json.JSONObject;

public final class ScriptResponse extends AbstractResponse {
    public String result;
    @Override
    protected void init(JSONObject obj) {
        super.init(obj);
        result = obj.optString(a);
    }
    @Override
    public JSONObject toJson() throws JSONException {
        JSONObject obj = super.toJson();
        put(obj, a, result);
        return obj;
    }
}
