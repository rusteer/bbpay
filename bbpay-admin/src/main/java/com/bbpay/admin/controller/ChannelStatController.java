package com.bbpay.admin.controller;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.bbpay.admin.entity.AppEntity;
import com.bbpay.admin.entity.CpEntity;
import com.bbpay.admin.entity.stat.BizChannelStatEntity;
import com.bbpay.admin.entity.stat.ChannelStatEntity;
import com.bbpay.admin.service.AppService;
import com.bbpay.admin.service.BizService;
import com.bbpay.admin.service.CpService;
import com.bbpay.admin.service.FormatUtil;
import com.bbpay.admin.service.stat.BizChannelStatService;
import com.bbpay.admin.service.stat.ChannelStatService;

@Controller
@RequestMapping("/" + ChannelStatController.cmpName)
public class ChannelStatController extends AbstractController {
    public static final String cmpName = "channelStat";
    @Autowired
    ChannelStatService service;
    @Autowired
    BizChannelStatService bizChannelStatService;
    @Autowired
    AppService appService;
    @Autowired
    BizService bizService;
    @Autowired
    CpService cpService;
    @RequestMapping(value = "/{statDate}/{bizId}/{channelId}", method = RequestMethod.GET)
    public String form(@PathVariable("statDate") String statDate, @PathVariable("bizId") Long bizId, @PathVariable("channelId") int channelId, HttpServletRequest request,
            HttpServletResponse response) {
        ChannelStatEntity statement = service.findByUnique(statDate, bizId, channelId);
        loadData(request, statement);
        return render(request, "form");
    }
    @RequestMapping(value = "/autoSet", method = RequestMethod.GET)
    public String autoSet(HttpServletRequest request, HttpServletResponse response) {
        String statDate = request.getParameter("statDate");
        if (StringUtils.isBlank(statDate)) {
            statDate = FormatUtil.format(new Date());
        }
        request.setAttribute("statDate", statDate);
        long appId = WebUtil.getLongParameter(request, "appId");
        List<ChannelStatEntity> statList = appId > 0 ? service.findByStatDateAndAppId(statDate, appId) : service.findByStatDate(statDate);
        Map<String, Integer> statMap = getStat(statDate, appId);
        for (ChannelStatEntity stat : statList) {
            if (stat.getStatementMoney() == 0) {
                String key = stat.getAppId() + "-" + stat.getChannelId();
                if (statMap.containsKey(key)) {
                    stat.setStatementMoney(statMap.get(key));
                    this.service.save(stat);
                }
            }
        }
        return this.redirect(request, "/" + cmpName + "/?statDate=" + statDate + "&appId=" + appId);
    }
    @Override
    protected String getCmpName() {
        return cmpName;
    }
    private Map<String, Integer> getStat(String statDate, Long appId) {
        Map<String, Integer> statMap = new HashMap<String, Integer>();
        List<BizChannelStatEntity> bizChannelStatList = appId > 0 ? this.bizChannelStatService.findByStatDateAndAppId(statDate, appId) : bizChannelStatService
                .findByStatDate(statDate);
        for (BizChannelStatEntity stat : bizChannelStatList) {
            AppEntity app = this.appService.get(stat.getAppId());
            CpEntity cp = this.cpService.get(app.getCpId());
            String key = stat.getAppId() + "-" + stat.getChannelId();
            int sum = statMap.containsKey(key) ? statMap.get(key) : 0;
            sum += stat.getStatementMoney() * (100-cp.getDiscountRate()) / 100;
            statMap.put(key, sum);
        }
        return statMap;
    }
    @RequestMapping(value = "/")
    public String list(HttpServletRequest request, HttpServletResponse response) {
        String statDate = request.getParameter("statDate");
        if (StringUtils.isBlank(statDate)) {
            statDate = FormatUtil.format(new Date());
        }
        request.setAttribute("statDate", statDate);
        long appId = WebUtil.getLongParameter(request, "appId");
        List<ChannelStatEntity> statList = appId > 0 ? service.findByStatDateAndAppId(statDate, appId) : service.findByStatDate(statDate);
        Map<Long, AppEntity> appMap = this.appService.getMapAll();
        Map<String, Integer> statMap = getStat(statDate, appId);
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (ChannelStatEntity bizStat : statList) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("stat", bizStat);
            map.put("app", appMap.get(bizStat.getAppId()));
            map.put("statementMoney", statMap.get(bizStat.getAppId() + "-" + bizStat.getChannelId()));
            list.add(map);
        }
        request.setAttribute("list", list);
        request.setAttribute("appList", this.appService.getAll());
        return render(request, "list");
    }
    public void loadData(HttpServletRequest request, ChannelStatEntity stat) {
        request.setAttribute("entity", stat);
        request.setAttribute("app", appService.get(stat.getAppId()));
    }
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public String save(HttpServletRequest request, HttpServletResponse response) {
        String statDate = request.getParameter("statDate");
        long appId = WebUtil.getLongParameter(request, "appId");
        int channelId = WebUtil.getIntParameter(request, "channelId");
        ChannelStatEntity entity = this.service.findByUnique(statDate, appId, channelId);
        entity.setStatementMoney(WebUtil.getIntParameter(request, "statementMoney") * 100);
        boolean saveSuccess = false;
        try {
            logger.info("Saving entity:" + mapper.writeValueAsString(entity));
            entity = service.save(entity);
            saveSuccess = true;
        } catch (Throwable e) {
            logger.error("Failed to save entity", e);
            request.setAttribute("errorMessage", e.getMessage());
        }
        if (saveSuccess) return redirect(request, "/" + cmpName + "/?date=" + entity.getStatDate() + "&saveSuccess=true");
        request.setAttribute("saveSuccess", saveSuccess);
        loadData(request, entity);
        return render(request, "form");
    }
}
