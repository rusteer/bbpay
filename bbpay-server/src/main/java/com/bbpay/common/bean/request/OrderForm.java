package com.bbpay.common.bean.request;
import org.json.JSONException;
import org.json.JSONObject;

public final class OrderForm extends AbstractForm {
    public int price;
    public OrderForm() {
        super(Methods.PAY);
    }
    @Override
    protected void init(JSONObject obj) {
        super.init(obj);
        price = obj.optInt(a);
    }
    @Override
    public JSONObject toJson() throws JSONException {
        JSONObject obj = super.toJson();
        put(obj, a, price);
        return obj;
    }
}
