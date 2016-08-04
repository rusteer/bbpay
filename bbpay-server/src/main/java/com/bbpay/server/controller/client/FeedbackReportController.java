package com.bbpay.server.controller.client;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import com.bbpay.common.bean.Json;
import com.bbpay.common.bean.request.FeedbackReportForm;

@Component
public class FeedbackReportController extends AbstractController {
    protected Logger logger = LoggerFactory.getLogger("feedbackReport");
    @Override
    public JSONObject execute(HttpServletRequest request, HttpServletResponse response, JSONObject requestObj) throws Throwable {
        FeedbackReportForm form = Json.optObj(FeedbackReportForm.class, requestObj);
        logger.info("request:" + mapper.writeValueAsString(form));
        return null;
    }
    @Override
    protected Logger getLogger() {
        return logger;
    }
}
