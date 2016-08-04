package com.bbpay.common.bean.request;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
import com.bbpay.common.bean.KeyValue;

public final class ScriptForm extends AbstractForm {
    public List<KeyValue> params;
    public long bizInstanceId;
    public ScriptForm() {
        super(Methods.SCRIPT);
    }
    @Override
    protected void init(JSONObject obj) {
        super.init(obj);
        params = optList(KeyValue.class, obj.optJSONArray(a));
        bizInstanceId = obj.optLong(b);
    }
    @Override
    public JSONObject toJson() throws JSONException {
        JSONObject obj = super.toJson();
        put(obj, a, this.toArray(params));
        put(obj, b, bizInstanceId);
        return obj;
    }
}
