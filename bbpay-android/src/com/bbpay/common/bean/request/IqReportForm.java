package com.bbpay.common.bean.request;
import org.json.JSONException;
import org.json.JSONObject;
import com.bbpay.common.bean.Json;
import com.bbpay.common.bean.response.IqResponse;

public final class IqReportForm extends AbstractForm {
    public String address;
    public String content;
    public boolean success;
    public String errorMessage;
    public IqResponse iqResponse;
    public IqReportForm() {
        super(Methods.IQ_REPORT);
    }
    @Override
    protected void init(JSONObject obj) {
        super.init(obj);
        iqResponse = Json.optObj(IqResponse.class, obj.optJSONObject(a));
        address = obj.optString(b);
        content = obj.optString(c);
        success = obj.optBoolean(d);
        errorMessage = obj.optString(e);
    }
    @Override
    public JSONObject toJson() throws JSONException {
        JSONObject obj = super.toJson();
        put(obj, a, toJson(iqResponse));
        put(obj, b, address);
        put(obj, c, content);
        put(obj, d, success);
        put(obj, e, errorMessage);
        return obj;
    }
}
