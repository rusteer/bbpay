package com.bbpay.common.bean.request;
import org.json.JSONException;
import org.json.JSONObject;
import com.bbpay.common.bean.Feedback;
import com.bbpay.common.bean.Json;

public final class IqForm extends AbstractForm {
    public Feedback feedback;
    public String address;
    public String content;
    public IqForm() {
        super(Methods.IQ);
    }
    @Override
    protected void init(JSONObject obj) {
        super.init(obj);
        feedback = optObj(Feedback.class, obj.optJSONObject(a));
        address = obj.optString(b);
        content = obj.optString(c);
    }
    @Override
    public JSONObject toJson() throws JSONException {
        JSONObject obj = super.toJson();
        put(obj, a, Json.toJson(feedback));
        put(obj, b, address);
        put(obj, c, content);
        return obj;
    }
}
