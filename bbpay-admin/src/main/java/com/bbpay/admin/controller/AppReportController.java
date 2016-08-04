package com.bbpay.admin.controller;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.bbpay.admin.service.AppService;
import com.bbpay.admin.service.BizInstanceService;
import com.bbpay.admin.service.CpChannelService;
import com.bbpay.admin.service.CpService;
import com.bbpay.admin.service.OrderService;
import com.bbpay.admin.service.ProvinceService;

@Controller
@RequestMapping("/report/app")
public class AppReportController {
    protected static final String cmpPrefix = "appReport";
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
    protected CpChannelService cpChannelService;
    @RequestMapping(value = "/date", method = RequestMethod.GET)
    public String dateReport(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("list", orderService.getDateStat(WebUtil.getStatParmas(request)));
        return render(request, "date", "日期");
    }
    @RequestMapping(value = "/price", method = RequestMethod.GET)
    public String priceReport(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("list", orderService.getPriceStat(WebUtil.getStatParmas(request)));
        return render(request, "price", "价格");
    }
    protected String render(HttpServletRequest request, String cmpName, String groupName) {
        request.setAttribute("groupName", groupName);
        request.setAttribute("cmpName", cmpPrefix + "-" + cmpName);
        request.setAttribute("subCmpName", cmpName);
        request.setAttribute("provinceList", provinceService.getAll());
        long cpId = WebUtil.getLongParameter(request, "cpId");
        if (cpId > 0) {
            request.setAttribute("appList", appService.getAppList(cpId));
            request.setAttribute("channelList", cpChannelService.getChannelList(cpId));
        }
        request.setAttribute("cpList", cpService.getAll());
        return "report/app/list";
    }
}
