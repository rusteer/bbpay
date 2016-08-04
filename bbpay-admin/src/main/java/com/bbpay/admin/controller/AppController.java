package com.bbpay.admin.controller;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.bbpay.admin.entity.AppEntity;
import com.bbpay.admin.service.AppService;
import com.bbpay.admin.service.CpService;
import com.bbpay.admin.service.GroupService;

@Controller
@RequestMapping("/" + AppController.cmpName)
public class AppController extends AbstractController {
    public static final String cmpName = "app";
    @Autowired
    AppService service;
    @Autowired
    CpService cpService;
    @Autowired
    GroupService groupService;
    private AppEntity composeEntity(HttpServletRequest request) {
        AppEntity entity = new AppEntity();
        entity.setId(WebUtil.getLongParameter(request, "id"));
        entity.setCpId(WebUtil.getLongParameter(request, "cpId"));
        entity.setGroupId(WebUtil.getLongParameter(request, "groupId"));
        entity.setName(request.getParameter("name"));
        entity.setDescription(request.getParameter("description"));
        if (entity.getId() == 0) {
            entity.setCreateTime(new Date());
        }
        return entity;
    }
    @RequestMapping(value = "/{cpId}/{id}", method = RequestMethod.GET)
    public String form(@PathVariable("cpId") Long cpId, @PathVariable("id") Long id, HttpServletRequest request, HttpServletResponse response) {
        AppEntity entity = id > 0 ? service.get(id) : new AppEntity();
        loadData(cpId, request, entity);
        return render(request, "form");
    }
    @Override
    protected String getCmpName() {
        return cmpName;
    }
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String list(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("cpList", cpService.getAll());
        request.setAttribute("groupList", groupService.getAll());
        List<AppEntity> list = service.getAll();
        request.setAttribute("list", list);
        return render(request, "list");
    }
    @RequestMapping(value = "/{cpId}", method = RequestMethod.GET)
    public String list(@PathVariable("cpId") Long cpId, HttpServletRequest request, HttpServletResponse response) {
        List<AppEntity> list = service.getAppList(cpId);
        request.setAttribute("list", list);
        request.setAttribute("cp", cpService.get(cpId));
        request.setAttribute("cpList", cpService.getAll());
        request.setAttribute("groupList", groupService.getAll());
        return render(request, "list");
    }
    public void loadData(Long cpId, HttpServletRequest request, AppEntity entity) {
        request.setAttribute("entity", entity);
        request.setAttribute("groupList", groupService.getAll());
        request.setAttribute("cp", cpService.get(cpId));
    }
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public String save(HttpServletRequest request, HttpServletResponse response) {
        AppEntity entity = composeEntity(request);
        boolean saveSuccess = false;
        try {
            logger.info("Saving entity:" + mapper.writeValueAsString(entity));
            entity = service.save(entity);
            saveSuccess = true;
        } catch (Throwable e) {
            logger.error("Failed to save entity", e);
            request.setAttribute("errorMessage", e.getMessage());
        }
        if (saveSuccess) return redirect(request, "/" + cmpName + "/" + entity.getCpId() + "/" + entity.getId() + "?saveSuccess=true");
        request.setAttribute("saveSuccess", saveSuccess);
        loadData(entity.getCpId(), request, entity);
        return render(request, "form");
    }
}
