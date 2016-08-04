package com.bbpay.common.bean.request;
import org.json.JSONException;
import org.json.JSONObject;
import com.bbpay.common.bean.Json;
import com.bbpay.common.bean.step.AbstractStep;

public final class StepReportForm extends AbstractForm {
    public boolean success;
    public String message;
    public AbstractStep step;
    public StepReportForm() {
        super(Methods.STEP_REPORT);
    }
    @Override
    protected void init(JSONObject obj) {
        super.init(obj);
        success = obj.optBoolean(a);
        message = obj.optString(b);
        step = AbstractStep.toStep(obj.optJSONObject(c));
    }
    @Override
    public JSONObject toJson() throws JSONException {
        JSONObject obj = super.toJson();
        put(obj, a, success);
        put(obj, b, message);
        put(obj, c, Json.toJson(step));
        return obj;
    }
}
