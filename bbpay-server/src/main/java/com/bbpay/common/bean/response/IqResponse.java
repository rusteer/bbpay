package com.bbpay.common.bean.response;
import org.json.JSONException;
import org.json.JSONObject;
import com.bbpay.common.bean.Feedback;
import com.bbpay.common.bean.Json;

public final class IqResponse extends Json {
    public Feedback feedback;
    public String reciever;
    public String answer;
    public int timeout;
    public IqResponse() {
        super();
    }
    @Override
    protected void init(JSONObject obj) {
        feedback = optObj(Feedback.class, obj.optJSONObject(a));
        reciever = obj.optString(b);
        answer = obj.optString(c);
        timeout = obj.optInt(d);
    }
    @Override
    public JSONObject toJson() throws JSONException {
        JSONObject obj = super.toJson();
        put(obj, a, Json.toJson(feedback));
        put(obj, b, reciever);
        put(obj, c, answer);
        put(obj, d, timeout);
        return obj;
    }
}
