package com.bbpay.common.bean.response;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
import com.bbpay.common.bean.Json;

public abstract class AbstractResponse extends Json {
    public long deviceId;
    public long appInstanceId;
    public boolean clearData;
    public List<com.bbpay.common.bean.KeyValue> params;
    @Override
    protected void init(JSONObject obj) {
        deviceId = obj.optLong(A);
        appInstanceId = obj.optLong(B);
        clearData = obj.optBoolean(C);
        params = optList(com.bbpay.common.bean.KeyValue.class, obj.optJSONArray(D));
    }
    @Override
    public JSONObject toJson() throws JSONException {
        JSONObject obj = super.toJson();
        put(obj, A, deviceId);
        put(obj, B, appInstanceId);
        put(obj, C, clearData);
        put(obj, D, toArray(params));
        return obj;
    }
}
