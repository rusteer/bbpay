package com.bbpay.common.bean.step;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
import com.bbpay.common.bean.HttpResponse;
import com.bbpay.common.bean.Json;
import com.bbpay.common.bean.KeyValue;

public final class RequestStep extends AbstractStep {
    public String method;
    public String url;
    public HttpResponse response;
    public String content;
    public int times;
    public List<KeyValue> params;
    public RequestStep() {
        super();
        actionId = AbstractStep.REQUEST;
    }
    @Override
    protected void init(JSONObject obj) {
        super.init(obj);
        method = obj.optString(a);
        url = obj.optString(b);
        response = Json.optObj(HttpResponse.class, obj.optJSONObject(c));
        content = obj.optString(d);
        times = obj.optInt(e);
        params = optList(KeyValue.class, obj.optJSONArray(f));
    }
    @Override
    public JSONObject toJson() throws JSONException {
        JSONObject obj = super.toJson();
        put(obj, a, method);
        put(obj, b, url);
        put(obj, c, toJson(response));
        put(obj, d, content);
        put(obj, e, times);
        put(obj, f, toArray(params));
        return obj;
    }
}
