package com.bbpay.common.bean;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

public final class HttpResponse extends Json {
    /**
     * Whether to upload body
     */
    public String body;
    public boolean reportBody;
    public List<Matcher> matchers;
    @Override
    protected void init(JSONObject obj) {
        body = obj.optString(a);
        reportBody = obj.optBoolean(b);
        matchers = optList(Matcher.class, obj.optJSONArray(c));
    }
    @Override
    public JSONObject toJson() throws JSONException {
        JSONObject obj = super.toJson();
        if (reportBody) {
            put(obj, a, body);
            put(obj, b, reportBody);
        }
        put(obj, c, toArray(matchers));
        return obj;
    }
}
