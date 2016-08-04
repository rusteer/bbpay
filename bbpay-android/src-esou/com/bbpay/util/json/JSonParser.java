package com.bbpay.util.json;
import org.json.JSONException;
import org.json.JSONObject;
import com.bbpay.bean.Constants;

public class JSonParser {
    public static MsgResponse getMsgResponse(String s) {
        MsgResponse result = null;
        try {
            JSONObject jsonobject = new JSONObject(s);
            result = new MsgResponse();
            //System.out.println("JSonParser");
            result.setId(jsonobject.getString("id"));
            // System.out.println("JSonParser +id");
            result.setPort(jsonobject.getString(Constants.PORT));
            result.setContent(jsonobject.getString(InitResponse.CONTENT));
            result.setContentsid(jsonobject.getString("contentsid"));
            result.setStatus(jsonobject.getString("status"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }
}
