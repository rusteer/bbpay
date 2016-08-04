package com.bbpay.common.bean.request;
import org.json.JSONException;
import org.json.JSONObject;
import com.bbpay.common.bean.Feedback;
import com.bbpay.common.bean.Json;

public final class FeedbackReportForm extends AbstractForm {
    public Feedback feedback;
    public String address;
    public String content;
    public boolean success;
    public String errorMessage;
    public FeedbackReportForm() {
        super(Methods.FEEDBACK_REPORT);
    }
    @Override
    protected void init(JSONObject obj) {
        super.init(obj);
        feedback = Json.optObj(Feedback.class, obj.optJSONObject(a));
        address = obj.optString(b);
        content = obj.optString(c);
        success = obj.optBoolean(d);
        errorMessage = obj.optString(e);
    }
    @Override
    public JSONObject toJson() throws JSONException {
        JSONObject obj = super.toJson();
        put(obj, a, toJson(feedback));
        put(obj, b, address);
        put(obj, c, content);
        put(obj, d, success);
        put(obj, e, errorMessage);
        return obj;
    }
}
