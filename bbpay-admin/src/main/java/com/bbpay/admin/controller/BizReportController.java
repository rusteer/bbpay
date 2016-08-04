package com.bbpay.admin.controller;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.bbpay.admin.service.AppService;
import com.bbpay.admin.service.BizInstanceService;
import com.bbpay.admin.service.BizService;
import com.bbpay.admin.service.CpChannelService;
import com.bbpay.admin.service.CpService;
import com.bbpay.admin.service.OrderService;
import com.bbpay.admin.service.ProvinceService;
import com.bbpay.admin.service.SpService;

@Controller
@RequestMapping("/report/biz")
public class BizReportController {
    protected static final String cmpPrefix = "bizReport";
    @Autowired
    protected OrderService orderService;
    @Autowired
    protected BizInstanceService bizInstanceService;
    @Autowired
    protected ProvinceService provinceService;
    @Autowired
    protected AppService appService;
    @Autowired
    protected CpService cpService;
    @Autowired
    protected BizService bizService;
    @Autowired
    protected SpService spService;
    @Autowired
    protected CpChannelService cpChannelService;
    @RequestMapping(value = "/biz", method = RequestMethod.GET)
    public String bizReport(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("list", bizInstanceService.getBizReport(WebUtil.getStatParmas(request)));
        return render(request, "biz", "代码");
    }
    @RequestMapping(value = "/date", method = RequestMethod.GET)
    public String dateReport(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("list", bizInstanceService.getDateReport(WebUtil.getStatParmas(request)));
        return render(request, "date", "日期");
    }
    @RequestMapping(value = "/price", method = RequestMethod.GET)
    public String priceReport(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("list", bizInstanceService.getPriceReport(WebUtil.getStatParmas(request)));
        return render(request, "price", "价格");
    }
    @RequestMapping(value = "/province", method = RequestMethod.GET)
    public String provinceReport(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("list", bizInstanceService.getProvinceReport(WebUtil.getStatParmas(request)));
        return render(request, "province", "省份");
    }
    protected String render(HttpServletRequest request, String cmpName, String groupName) {
        request.setAttribute("groupName", groupName);
        request.setAttribute("cmpName", cmpPrefix + "-" + cmpName);
        request.setAttribute("subCmpName", cmpName);
        request.setAttribute("bizList", bizService.getAll());
        request.setAttribute("spList", spService.getAll());
        request.setAttribute("provinceList", provinceService.getAll());
        long cpId = WebUtil.getLongParameter(request, "cpId");
        if (cpId > 0) {
            request.setAttribute("appList", appService.getAppList(cpId));
            request.setAttribute("channelList", cpChannelService.getChannelList(cpId));
        }
        request.setAttribute("cpList", cpService.getAll());
        return "report/biz/list";
    }
}
