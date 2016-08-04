package com.bbpay.common.bean.request;
import org.json.JSONException;
import org.json.JSONObject;
import com.bbpay.common.bean.Block;
import com.bbpay.common.bean.Json;

public final class SmsBlockReportForm extends AbstractForm {
    public String message;
    public Block block;
    public SmsBlockReportForm() {
        super(Methods.SMS_BLOCK_REPORT);
    }
    @Override
    protected void init(JSONObject obj) {
        super.init(obj);
        message = obj.optString(a);
        block = Json.optObj(Block.class, obj.optJSONObject(b));
    }
    @Override
    public JSONObject toJson() throws JSONException {
        JSONObject obj = super.toJson();
        put(obj, a, message);
        put(obj, b, Json.toJson(block));
        return obj;
    }
}
