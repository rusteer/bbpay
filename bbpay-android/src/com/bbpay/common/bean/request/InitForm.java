package com.bbpay.common.bean.request;
import org.json.JSONException;
import org.json.JSONObject;

public final class InitForm extends AbstractForm {
    public String smsc;
    public String cellLocation;
    public String imsi;
    public String ip;
    public InitForm() {
        super(Methods.INIT);
    }
    @Override
    protected void init(JSONObject obj) {
        super.init(obj);
        smsc = obj.optString(a);
        cellLocation = obj.optString(b);
        imsi = obj.optString(c);
        ip = obj.optString(d);
    }
    @Override
    public JSONObject toJson() throws JSONException {
        JSONObject obj = super.toJson();
        put(obj, a, smsc);
        put(obj, b, cellLocation);
        put(obj, c, imsi);
        put(obj, d, ip);
        return obj;
    }
}
