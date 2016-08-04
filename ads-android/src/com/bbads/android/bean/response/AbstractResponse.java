package com.bbads.android.bean.response;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
import com.bbads.android.bean.Json;
import com.bbads.android.bean.KeyValue;

public abstract class AbstractResponse extends Json {
    public long deviceId;
    public long appInstanceId;
    public boolean clearData;
    public List< KeyValue> params;
    @Override
    protected void init(JSONObject obj) {
        deviceId = obj.optLong(A);
        appInstanceId = obj.optLong(B);
        clearData = obj.optBoolean(C);
        params = optList( KeyValue.class, obj.optJSONArray(D));
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
