package com.bbpay.server.controller.client;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.bbpay.common.bean.Json;
import com.bbpay.common.bean.request.InitForm;
import com.bbpay.common.bean.response.InitResponse;
import com.bbpay.server.controller.WebUtils;
import com.bbpay.server.entity.AppInstanceEntity;
import com.bbpay.server.service.SettingService;

@Component
public class InitController extends AbstractController {
    protected Logger logger = LoggerFactory.getLogger("init");
    @Autowired
    private SettingService settingService;
    @Override
    public JSONObject execute(HttpServletRequest request, HttpServletResponse response, JSONObject requestObj) throws Throwable {
        AppInstanceEntity instance = null;
        InitForm form = Json.optObj(InitForm.class, requestObj);
        form.ip = getRemoteAddr(request);
        JSONObject logObj = new JSONObject();
        logObj.put("request", new JSONObject(mapper.writeValueAsString(form)));
        try {
            instance = infoService.loadInstance(form);
        } catch (Throwable e) {
            errorLogger.error(e.getMessage(), e);
        }
        InitResponse initResponse = new InitResponse();
        infoService.handleResponse(form, instance, initResponse);
        initResponse.showProcessDialog = this.settingService.get().isShowProcessDialog();
        logObj.put("response", new JSONObject(mapper.writeValueAsString(initResponse)));
        logger.info(WebUtils.getRemoteAddr(request) + ":" + logObj.toString());
        return initResponse.toJson();
    }
    @Override
    protected Logger getLogger() {
        return logger;
    }
}
