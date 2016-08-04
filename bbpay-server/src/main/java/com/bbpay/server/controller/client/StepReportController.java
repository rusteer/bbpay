package com.bbpay.server.controller.client;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import com.bbpay.common.bean.Json;
import com.bbpay.common.bean.request.StepReportForm;

@Component
public class StepReportController extends AbstractController {
    protected Logger logger = LoggerFactory.getLogger("stepReport");
    @Override
    public JSONObject execute(HttpServletRequest request, HttpServletResponse response, JSONObject requestObj) throws Throwable {
        StepReportForm form = Json.optObj(StepReportForm.class, requestObj);
        logger.info("request:" + mapper.writeValueAsString(form));
        return null;
    }
    @Override
    protected Logger getLogger() {
        return logger;
    }
}