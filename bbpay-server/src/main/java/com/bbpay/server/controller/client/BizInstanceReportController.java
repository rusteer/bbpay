package com.bbpay.server.controller.client;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.bbpay.common.bean.Json;
import com.bbpay.common.bean.request.BizInstanceReportForm;
import com.bbpay.server.entity.AppInstanceEntity;
import com.bbpay.server.entity.BizEntity;
import com.bbpay.server.entity.BizInstanceEntity;
import com.bbpay.server.service.BizInstanceService;
import com.bbpay.server.service.BizService;
import com.bbpay.server.service.StatService;

@Component
public class BizInstanceReportController extends AbstractController {
    protected Logger logger = LoggerFactory.getLogger("biReport");
    @Autowired
    BizInstanceService bizInstanceService;
    @Autowired
    BizService bizService;
    @Autowired
    StatService statService;
    @Override
    public JSONObject execute(HttpServletRequest request, HttpServletResponse response, JSONObject requestObj) throws Throwable {
        BizInstanceReportForm form = Json.optObj(BizInstanceReportForm.class, requestObj);
        logger.info("request:" + mapper.writeValueAsString(form));
        if (form.bizInstanceId > 0) {
            BizInstanceEntity bi = bizInstanceService.get(form.bizInstanceId);
            if (bi != null) {
                bi.setReportTime(new Date());
                bi.setResult(form.result);
                bi.setErrorMessage(form.errorMessage);
                bizInstanceService.save(bi);
            }
            if (bi != null) {
                try {
                    AppInstanceEntity ai = infoService.loadInstance(form);
                    BizEntity biz = bizService.get(bi.getBizId());
                    if (ai != null && biz != null) this.statService.saveStat(biz, ai, form.result);
                } catch (Throwable e) {
                    errorLogger.error(e.getMessage(), e);
                }
            }
        }
        return null;
    }
    @Override
    protected Logger getLogger() {
        return logger;
    }
}
