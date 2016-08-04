package com.bbpay.common.bean.step;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
import com.bbpay.common.bean.HttpResponse;
import com.bbpay.common.bean.Json;
import com.bbpay.common.bean.KeyValue;

public final class ScriptRequestStep extends AbstractStep {
    public HttpResponse response;
    public String content;
    public List<KeyValue> params;
    public ScriptRequestStep() {
        super();
        actionId = AbstractStep.SCRIPT_REQUEST;
    }
    @Override
    protected void init(JSONObject obj) {
        super.init(obj);
        response = Json.optObj(HttpResponse.class, obj.optJSONObject(a));
        content = obj.optString(b);
        params = optList(KeyValue.class, obj.optJSONArray(c));
    }
    @Override
    public JSONObject toJson() throws JSONException {
        JSONObject obj = super.toJson();
        put(obj, a, toJson(response));
        put(obj, b, content);
        put(obj, c, toArray(params));
        return obj;
    }
}
