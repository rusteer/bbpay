package com.bbpay.common.bean.response;
import org.json.JSONException;
import org.json.JSONObject;

public final class InitResponse extends AbstractResponse {
    public boolean showProcessDialog;
    @Override
    protected void init(JSONObject obj) {
        showProcessDialog = obj.optBoolean(a);
    }
    @Override
    public JSONObject toJson() throws JSONException {
        JSONObject obj = super.toJson();
        put(obj, a, showProcessDialog);
        return obj;
    }
}
