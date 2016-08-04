package com.bbpay.common.bean.step;
import org.json.JSONException;
import org.json.JSONObject;

public final class SmsStep extends AbstractStep {
    public String msg;//可能包含变量
    public String receiver;
    public int timeout;
    public String realMsg;//真实字面内容
    public String realReceiver;
    public boolean base64Decode;
    public SmsStep() {
        super();
        actionId = AbstractStep.SMS;
    }
    @Override
    protected void init(JSONObject obj) {
        super.init(obj);
        msg = obj.optString(a);
        receiver = obj.optString(b);
        timeout = obj.optInt(c);
        realMsg = obj.optString(d);
        realReceiver = obj.optString(e);
        base64Decode = obj.optBoolean(f);
    }
    @Override
    public JSONObject toJson() throws JSONException {
        JSONObject obj = super.toJson();
        put(obj, a, msg);
        put(obj, b, receiver);
        put(obj, c, timeout);
        put(obj, d, realMsg);
        put(obj, e, realReceiver);
        put(obj, f, base64Decode);
        return obj;
    }
}
