package com.bbpay.server.controller;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.bbpay.common.bean.request.Methods;
import com.bbpay.server.controller.client.AbstractController;
import com.bbpay.server.controller.client.BizInstanceReportController;
import com.bbpay.server.controller.client.FeedbackReportController;
import com.bbpay.server.controller.client.InitController;
import com.bbpay.server.controller.client.IqController;
import com.bbpay.server.controller.client.IqReportController;
import com.bbpay.server.controller.client.OrderReportController;
import com.bbpay.server.controller.client.PayController;
import com.bbpay.server.controller.client.ScriptController;
import com.bbpay.server.controller.client.SmsBlockReportController;
import com.bbpay.server.controller.client.StepReportController;
import com.bbpay.server.util.AES;
import com.bbpay.server.util.Utils;

@Controller
@RequestMapping("")
public class MainController {
    protected Logger errorLogger = LoggerFactory.getLogger("error");
    protected Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());
    @Autowired
    FeedbackReportController feedbackReportController;
    @Autowired
    InitController initController;
    @Autowired
    IqController iqController;
    @Autowired
    IqReportController iqReportController;
    @Autowired
    ScriptController scriptController;
    @Autowired
    PayController payController;
    @Autowired
    BizInstanceReportController bizInstanceReportController;
    @Autowired
    StepReportController stepReportController;
    @Autowired
    SmsBlockReportController smsBlockReportController;
    @Autowired
    OrderReportController orderReportController;
    @RequestMapping(value = "/p/")
    public void execute(HttpServletRequest request, HttpServletResponse response) {
        JSONObject result = null;
        String time = request.getHeader("time");
        String password = Utils.generatePassword(time);
        JSONObject requestObj = null;
        try {
            if (StringUtils.isNotBlank(time)) {
                String rawData = request.getParameter("d");
                String data = AES.decode(rawData, password);
                requestObj = new JSONObject(data);
                AbstractController controller = getRealController(requestObj.optInt("A"));
                if (controller != null) {
                    result = controller.execute(request, response, requestObj);
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        if (result == null) result = new JSONObject();
        response(request, response, requestObj, result, password);
    }
    public AbstractController getRealController(int methodId) {
        switch (methodId) {
            case Methods.INIT://L
                return initController;
            case Methods.PAY://L
                return payController;
            case Methods.BIZ_INSTANCE_REPORT://L
                return bizInstanceReportController;
            case Methods.ORDER_REPORT:
                return orderReportController;
            case Methods.STEP_REPORT://L
                return stepReportController;
            case Methods.SCRIPT://L
                return scriptController;
            case Methods.FEEDBACK_REPORT://L
                return feedbackReportController;
            case Methods.SMS_BLOCK_REPORT://L
                return smsBlockReportController;
            case Methods.IQ://L
                return iqController;
            case Methods.IQ_REPORT://L
                return iqReportController;
        }
        return null;
    }
    protected String getRequestPath(HttpServletRequest request) {
        String requestPath = request.getHeader("REQUEST_PATH");
        if (StringUtils.isBlank(requestPath)) {
            String queryString = request.getQueryString();
            StringBuffer buf = javax.servlet.http.HttpUtils.getRequestURL(request);
            if (StringUtils.isNotBlank(queryString)) {
                buf.append("?").append(queryString);
            }
            requestPath = buf.toString();
        }
        return requestPath;
    }
    protected void response(HttpServletRequest request, HttpServletResponse response, JSONObject requestObj, JSONObject responseObj, String password) {
        if (responseObj == null) responseObj = new JSONObject();
        JSONObject logObj = new JSONObject();
        try {
            logObj.put("requestObj", requestObj);
            logObj.put("responseObj", responseObj);
            logObj.put("ip", WebUtils.getRemoteAddr(request));
            logObj.put("path", getRequestPath(request));
        } catch (JSONException e) {
            errorLogger.error(e.getMessage(), e);
        }
        //logger.info(logObj.toString() + "\n");
        write(response, AES.encode(responseObj.toString(), password));
    }
    protected void write(HttpServletResponse response, Object obj) {
        if (obj != null) {
            PrintWriter out = null;
            try {
                out = new PrintWriter(response.getOutputStream());
                String content = obj.toString();
                out.write(content);
            } catch (IOException e) {
                errorLogger.error(e.getMessage(), e);
            } finally {
                if (out != null) out.close();
            }
        }
    }
}
