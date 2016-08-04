package com.bbpay.common.bean.step;
import org.json.JSONException;
import org.json.JSONObject;

public final class DelayStep extends AbstractStep {
    public int delayTime;
    public DelayStep() {
        super();
        actionId = AbstractStep.DELAY;
    }
    @Override
    protected void init(JSONObject obj) {
        super.init(obj);
        delayTime = obj.optInt(a);
    }
    @Override
    public JSONObject toJson() throws JSONException {
        JSONObject obj = super.toJson();
        put(obj, a, delayTime);
        return obj;
    }
}
