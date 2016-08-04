package com.bbpay.util.json;
import org.json.JSONException;
import org.json.JSONObject;

public class JSonParserInit {
    public JSonParserInit() {}
    public static InitResponse getInitResponse(String content) {
        InitResponse response = null;
        if (content != null) {
            try {
                JSONObject jsonobject = new JSONObject(content);
                response = new InitResponse();
                response.setContent(jsonobject.getString(InitResponse.CONTENT));
                response.setMobileImsi(jsonobject.getString(InitResponse.MOBILE_IMSI));
                response.setResultCode(jsonobject.getString(InitResponse.RESULT_CODE));
                response.setSendMobile(jsonobject.getString(InitResponse.SEND_MOBILE));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return response;
    }
}
