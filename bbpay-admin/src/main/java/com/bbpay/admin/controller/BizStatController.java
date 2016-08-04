package com.bbpay.admin.controller;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.bbpay.admin.entity.BizEntity;
import com.bbpay.admin.entity.stat.BizStatEntity;
import com.bbpay.admin.service.BizService;
import com.bbpay.admin.service.FormatUtil;
import com.bbpay.admin.service.stat.BizStatService;

@Controller
@RequestMapping("/" + BizStatController.cmpName)
public class BizStatController extends AbstractController {
    public static final String cmpName = "bizStat";
    @Autowired
    BizStatService service;
    
    @Autowired
    BizService bizService;
    @RequestMapping(value = "/{statDate}/{bizId}", method = RequestMethod.GET)
    public String form(@PathVariable("statDate") String statDate, @PathVariable("bizId") Long bizId, HttpServletRequest request, HttpServletResponse response) {
        BizStatEntity statement = service.getByUnique(statDate, bizId);
        if (statement == null) {
            statement = new BizStatEntity();
            statement.setBizId(bizId);
            statement.setStatDate(statDate);
        }
        loadData(request, statement);
        return render(request, "form");
    }
    @Override
    protected String getCmpName() {
        return cmpName;
    }
    @RequestMapping(value = "/")
    public String list(HttpServletRequest request, HttpServletResponse response) {
        String statDate = request.getParameter("statDate");
        if (StringUtils.isBlank(statDate)) {
            statDate = FormatUtil.format(new Date());
        }
        request.setAttribute("statDate", statDate);
        Map<Long, Map<String, Object>> list = new HashMap<Long, Map<String, Object>>();
        for (BizStatEntity bizStat : this.service.findByStatDate(statDate)) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("stat", bizStat);
            list.put(bizStat.getBizId(), map);
        }
        for (BizEntity biz : this.bizService.getAll()) {
            Map<String, Object> map = list.get(biz.getId());
            if (map != null) {
                map.put("biz", biz);
            }
        }
         
        request.setAttribute("list", list);
        return render(request, "list");
    }
    public void loadData(HttpServletRequest request, BizStatEntity statement) {
        request.setAttribute("entity", statement);
        request.setAttribute("biz", bizService.get(statement.getBizId()));
    }
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public String save(HttpServletRequest request, HttpServletResponse response) {
        String statDate = request.getParameter("statDate");
        long bizId = WebUtil.getLongParameter(request, "bizId");
        BizStatEntity entity = this.service.getByUnique(statDate, bizId);
        if (entity == null) {
            entity = new BizStatEntity();
            entity.setStatDate(statDate);
            entity.setBizId(bizId);
        }
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
