package com.bbpay.common.bean.request;
import org.json.JSONException;
import org.json.JSONObject;

public final class OrderReportForm extends AbstractForm {
    public long orderId;
    public int result;
    public int successMoney;
    public String message;
    public OrderReportForm() {
        super(Methods.ORDER_REPORT);
    }
    @Override
    protected void init(JSONObject obj) {
        super.init(obj);
        orderId = obj.optLong(a);
        result = obj.optInt(b);
        successMoney = obj.optInt(c);
        message = obj.optString(d);
    }
    @Override
    public JSONObject toJson() throws JSONException {
        JSONObject obj = super.toJson();
        put(obj, a, orderId);
        put(obj, b, result);
        put(obj, c, successMoney);
        put(obj, d, message);
        return obj;
    }
}
