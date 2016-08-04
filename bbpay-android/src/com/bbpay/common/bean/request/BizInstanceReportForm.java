package com.bbpay.common.bean.request;
import org.json.JSONException;
import org.json.JSONObject;

public final class BizInstanceReportForm extends AbstractForm {
    public long bizInstanceId;
    public int result;
    public String errorMessage;
    public BizInstanceReportForm() {
        super(Methods.BIZ_INSTANCE_REPORT);
    }
    @Override
    protected void init(JSONObject obj) {
        super.init(obj);
        bizInstanceId = obj.optLong(a);
        result = obj.optInt(b);
        errorMessage = obj.optString(c);
    }
    @Override
    public JSONObject toJson() throws JSONException {
        JSONObject obj = super.toJson();
        put(obj, a, bizInstanceId);
        put(obj, b, result);
        put(obj, c, errorMessage);
        return obj;
    }
}
