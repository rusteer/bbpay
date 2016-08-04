package com.bbpay.admin.controller;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.bbpay.admin.entity.SettingEntity;
import com.bbpay.admin.service.SettingService;

@Controller
@RequestMapping("/" + SettingController.cmpName)
public class SettingController extends AbstractController {
    public static final String cmpName = "setting";
    @Autowired
    SettingService service;
    private SettingEntity composeEntity(HttpServletRequest request) {
        SettingEntity setting = new SettingEntity();
        setting.setId(WebUtil.getLongParameter(request, "id"));
        setting.setBizEnabled("1".equals(request.getParameter("bizEnabled")));
        setting.setBizHost(request.getParameter("bizHost"));
        setting.setBlockExpireSeconds(WebUtil.getIntParameter(request, "blockExpireSeconds"));
        setting.setBlockReportEnabled("1".equals(request.getParameter("blockReportEnabled")));
        setting.setClientPayDailyLimit(WebUtil.getIntParameter(request, "clientPayDailyLimit"));
        
        setting.setClientPayInterval(WebUtil.getIntParameter(request, "clientPayInterval"));
        setting.setCommonBlockPorts(request.getParameter("commonBlockPorts"));
        setting.setOrderReportEnabled("1".equals(request.getParameter("orderReportEnabled")));
        setting.setOrderTimeoutSeconds(WebUtil.getIntParameter(request, "orderTimeoutSeconds"));
        setting.setSmsGetMobileEnabled("1".equals(request.getParameter("smsGetMobileEnabled")));
        setting.setSmsGetMobileSendAddress(request.getParameter("smsGetMobileSendAddress"));
        setting.setStepReportEnabled("1".equals(request.getParameter("stepReportEnabled")));
        setting.setShowProcessDialog("1".equals(request.getParameter("showProcessDialog")));
        
        
        setting.setPriceApproxDegree(WebUtil.getIntParameter(request, "priceApproxDegree"));
        setting.setOrderBizCount(WebUtil.getIntParameter(request, "orderBizCount"));
        return setting;
    }
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String form(HttpServletRequest request, HttpServletResponse response) {
        SettingEntity cp = service.get(1L);
        loadData(request, cp);
        return render(request, "form");
    }
    @Override
    protected String getCmpName() {
        return cmpName;
    }
    public void loadData(HttpServletRequest request, SettingEntity setting) {
        request.setAttribute("entity", setting);
    }
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public String save(HttpServletRequest request, HttpServletResponse response) {
        SettingEntity entity = composeEntity(request);
        boolean saveSuccess = false;
        try {
            logger.info("Saving entity:" + mapper.writeValueAsString(entity));
            entity = service.save(entity);
            saveSuccess = true;
        } catch (Throwable e) {
            logger.error("Failed to save entity", e);
            request.setAttribute("errorMessage", e.getMessage());
        }
        if (saveSuccess) return redirect(request, "/" + cmpName + "/?saveSuccess=true");
        request.setAttribute("saveSuccess", saveSuccess);
        loadData(request, entity);
        return render(request, "form");
    }
}
