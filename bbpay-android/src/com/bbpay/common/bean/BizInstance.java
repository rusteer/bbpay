package com.bbpay.common.bean;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.bbpay.common.bean.step.AbstractStep;

public class BizInstance extends Json {
    public long id;
    public int timeoutSeconds;
    public List<AbstractStep> stepList;
    public int executeResult;
    public String executeErrorMessage;
    public int price;
    //
    @Override
    public void init(JSONObject obj) {
        if (obj == null) return;
        id = obj.optLong(a);
        timeoutSeconds = obj.optInt(b);
        initSteps(obj.optJSONArray(c));
        executeResult = obj.optInt(d);
        executeErrorMessage = obj.optString(e);
        price = obj.optInt(f);
    }
    private void initSteps(JSONArray actionArray) {
        int index = 0;
        if (actionArray != null) {
            stepList = new ArrayList<AbstractStep>();
            for (int i = 0; i < actionArray.length(); i++) {
                AbstractStep step = AbstractStep.toStep(actionArray.optJSONObject(i));
                if (step != null) {
                    step.index = index++;
                    stepList.add(step);
                }
            }
        }
    }
    @Override
    public JSONObject toJson() throws JSONException {
        JSONObject obj = super.toJson();//
        put(obj, a, id);
        put(obj, b, timeoutSeconds);
        put(obj, c, toArray(stepList));
        put(obj, d, executeResult);
        put(obj, e, executeErrorMessage);
        put(obj, f, price);
        return obj;
    }
}
