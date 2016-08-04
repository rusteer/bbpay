package com.bbpay.common.bean.response;
import java.util.List;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;
import com.bbpay.common.bean.BizInstance;
import com.bbpay.common.bean.Block;
import com.bbpay.common.bean.Feedback;
import com.bbpay.server.entity.BizEntity;

public final class Order extends AbstractResponse {
    /**
     * 所有的Order成功,才算计费成功
     */
    public static final int POLICY_ALL = 0;
    /**
     * 只要有一个Order成功,就算计费成功
     */
    public static final int POLICY_SOME = 1;
    //
    public List<BizInstance> bizInstanceList;
    public List<Block> blockList;
    public List<Feedback> feedbackList;
    public int successPolicy;
    public int timeoutSeconds;
    public long id;
    
    
    //---json ignore begin----------
    public Map<Long, String> bizNotMatchReason;
    public List<BizEntity> bizList;
    public String globalNotMatchReason;
    //---json ignore end----------
    
    //
    @Override
    public void init(JSONObject obj) {
        super.init(obj);
        bizInstanceList = optList(BizInstance.class, obj.optJSONArray(a));
        blockList = optList(Block.class, obj.optJSONArray(b));
        feedbackList = optList(Feedback.class, obj.optJSONArray(c));
        successPolicy = obj.optInt(d);
        timeoutSeconds = obj.optInt(e);
        id = obj.optLong(f);
    }
    @Override
    public JSONObject toJson() throws JSONException {
        JSONObject obj = super.toJson();//
        put(obj, a, toArray(bizInstanceList));
        put(obj, b, toArray(blockList));
        put(obj, c, toArray(feedbackList));
        put(obj, d, successPolicy);
        put(obj, e, timeoutSeconds);
        put(obj, f, id);
        return obj;
    }
}