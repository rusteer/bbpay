package com.bbpay.server.controller.client;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import com.bbpay.common.bean.Json;
import com.bbpay.common.bean.request.IqForm;

@Component
public class IqController extends AbstractController {
    protected Logger logger = LoggerFactory.getLogger("iq");
    @Override
    public JSONObject execute(HttpServletRequest request, HttpServletResponse response, JSONObject requestObj) throws Throwable {
        IqForm form = Json.optObj(IqForm.class, requestObj);
        logger.info("request:" + mapper.writeValueAsString(form));
        return null;
    }
    @Override
    protected Logger getLogger() {
        return logger;
    }
}