package com.bbpay.server.controller.client;
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
import com.bbpay.common.bean.response.CommonResponse;
import com.bbpay.server.service.InfoService;
import com.bbpay.server.util.AES;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class AbstractController {
    protected Logger errorLogger = LoggerFactory.getLogger("error");
    
    protected abstract Logger getLogger();
    protected ObjectMapper mapper = new ObjectMapper();
    @Autowired
    InfoService infoService;
    public abstract JSONObject execute(HttpServletRequest request, HttpServletResponse response, JSONObject requestObj) throws Throwable;
    protected String getRemoteAddr(HttpServletRequest request) {
        String result = request.getHeader("X-Real-IP");
        if (StringUtils.isBlank(result)) {
            result = request.getRemoteAddr();
        }
        return result;
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
    protected JSONObject noInstance() {
        try {
            CommonResponse response = new CommonResponse();
            response.clearData = true;
            return response.toJson();
        } catch (JSONException e) {
            errorLogger.error(e.getMessage(), e);
        }
        return null;
    }
    protected void response(HttpServletRequest request, HttpServletResponse response, JSONObject requestObj, JSONObject responseObj, String password) {
        if (responseObj == null) responseObj = new JSONObject();
        JSONObject logObj = new JSONObject();
        try {
            logObj.put("requestObj", requestObj);
            logObj.put("responseObj", responseObj);
            logObj.put("ip", getRemoteAddr(request));
            logObj.put("path", getRequestPath(request));
        } catch (JSONException e) {
            errorLogger.error(e.getMessage(), e);
        }
        getLogger().info(logObj.toString() + "\n");
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