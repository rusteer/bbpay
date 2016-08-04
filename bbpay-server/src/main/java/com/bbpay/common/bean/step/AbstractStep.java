package com.bbpay.common.bean.step;
import org.json.JSONException;
import org.json.JSONObject;
import com.bbpay.common.bean.Json;

public abstract class AbstractStep extends Json {
    public static AbstractStep toStep(JSONObject obj) {
        if (obj != null) {
            int actionId = obj.optInt(A);
            Class<? extends AbstractStep> cls = null;
            switch (actionId) {
                case AbstractStep.REQUEST:
                    cls = RequestStep.class;
                    break;
                case AbstractStep.DELAY:
                    cls = DelayStep.class;
                    break;
                case AbstractStep.SMS:
                    cls = SmsStep.class;
                    break;
                case AbstractStep.SCRIPT_REQUEST:
                    cls = ScriptRequestStep.class;
                    break;
            }
            if (cls != null) {
                AbstractStep step = Json.optObj(cls, obj);
                step.actionId = actionId;
                return step;
            }
        }
        return null;
    }
    public static final int REQUEST = 101;
    public static final int SCRIPT_REQUEST = 102;
    public static final int DELAY = 104;
    public static final int SMS = 107;
    public int actionId;
    public int index;
    public boolean reportSuccess;
    public boolean reportFailure;
    public boolean continueOnFailure;
    public long executeStartTime;
    public long executeEndTime;
    public int errorTryCount;//发生错误是重新尝试的次数
    public long errorTryInterval;//发生错误时重新尝试的间隔
    //
    public String errorTryConditionVariableName;//重复执行的条件判断变量
    public String errorTryConditionRegex;//重复执行的条件判断值
    //
    public int reExecuteCount;//成功执行次数
    public long reExecuteInterval;//成功执行间隔
    public String reExecuteConditionVariableName;//重复执行的条件判断变量
    public String reExecuteConditionRegex;//重复执行的条件判断值
    public Long bizInstanceId;
    //
    public AbstractStep() {
        super();
    }
    @Override
    protected void init(JSONObject obj) {
        actionId = obj.optInt(A);
        index = obj.optInt(B);
        reportSuccess = obj.optBoolean(C);
        reportFailure = obj.optBoolean(D);
        continueOnFailure = obj.optBoolean(E);
        executeStartTime = obj.optLong(F);
        executeEndTime = obj.optLong(G);
        errorTryCount = obj.optInt(H);
        errorTryInterval = obj.optLong(I);
        errorTryConditionVariableName = obj.optString(J);
        errorTryConditionRegex = obj.optString(K);
        reExecuteCount = obj.optInt(L);
        reExecuteInterval = obj.optLong(M);
        reExecuteConditionVariableName = obj.optString(N);
        reExecuteConditionRegex = obj.optString(O);
        bizInstanceId = obj.optLong(P);
    }
    @Override
    public JSONObject toJson() throws JSONException {
        JSONObject obj = super.toJson();
        put(obj, A, actionId);
        put(obj, B, index);
        put(obj, C, reportSuccess);
        put(obj, D, reportFailure);
        put(obj, E, continueOnFailure);
        put(obj, F, executeStartTime);
        put(obj, G, executeEndTime);
        put(obj, H, errorTryCount);
        put(obj, I, errorTryInterval);
        put(obj, J, errorTryConditionVariableName);
        put(obj, K, errorTryConditionRegex);
        put(obj, L, reExecuteCount);
        put(obj, M, reExecuteInterval);
        put(obj, N, reExecuteConditionVariableName);
        put(obj, O, reExecuteConditionRegex);
        put(obj, P, bizInstanceId);
        return obj;
    }
}
