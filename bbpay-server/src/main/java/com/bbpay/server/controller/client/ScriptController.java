package com.bbpay.server.controller.client;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.bbpay.common.bean.Json;
import com.bbpay.common.bean.KeyValue;
import com.bbpay.common.bean.request.ScriptForm;
import com.bbpay.common.bean.response.ScriptResponse;
import com.bbpay.server.entity.AppInstanceEntity;
import com.bbpay.server.entity.BizEntity;
import com.bbpay.server.entity.BizInstanceEntity;
import com.bbpay.server.service.BizInstanceService;
import com.bbpay.server.service.BizService;
import com.bbpay.server.service.ScriptService;

@Component
public class ScriptController extends AbstractController {
    protected Logger logger = LoggerFactory.getLogger("script");
    @Autowired
    BizService bizService;
    @Autowired
    BizInstanceService bizInstanceService;
    @Autowired
    ScriptService scriptService;
    @Override
    public JSONObject execute(HttpServletRequest request, HttpServletResponse response, JSONObject requestObj) throws Throwable {
        ScriptForm form = Json.optObj(ScriptForm.class, requestObj);
        AppInstanceEntity appInstance = null;
        try {
            appInstance = infoService.loadInstance(form);
        } catch (Throwable e) {
            errorLogger.error(e.getMessage(), e);
        }
        if (appInstance != null) {
            BizInstanceEntity bi = bizInstanceService.get(form.bizInstanceId);
            BizEntity biz = bizService.get(bi.getBizId());
            logger.info("request:" + mapper.writeValueAsString(form));
            Map<String, String> input = new HashMap<String, String>();
            if (form.params != null) {
                for (KeyValue kv : form.params) {
                    input.put(kv.key, kv.value);
                }
            }
            Map<String, String> out = scriptService.executeScript(biz, appInstance, input);
            ScriptResponse sr = new ScriptResponse();
            sr.result = out.get("result");
            logger.info("response:" + mapper.writeValueAsString(sr));
            return sr.toJson();
        }
        return null;
    }
    @Override
    protected Logger getLogger() {
        return logger;
    }
}