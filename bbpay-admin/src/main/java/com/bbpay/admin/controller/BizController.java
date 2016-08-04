package com.bbpay.admin.controller;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.bbpay.admin.entity.BizEntity;
import com.bbpay.admin.entity.BlockEntity;
import com.bbpay.admin.service.BizService;
import com.bbpay.admin.service.BlockService;
import com.bbpay.admin.service.CityService;
import com.bbpay.admin.service.FeedbackService;
import com.bbpay.admin.service.GroupService;
import com.bbpay.admin.service.ProvinceService;
import com.bbpay.admin.service.SettingService;
import com.bbpay.admin.service.SpService;

@Controller
@RequestMapping("/" + BizController.cmpName)
public class BizController extends AbstractController {
    private static final int blockCount = 8;
    public static final String cmpName = "biz";
    @Autowired
    BizService bizService;
    @Autowired
    ProvinceService provinceService;
    @Autowired
    CityService cityService;
    @Autowired
    SettingService settingService;
    @Autowired
    FeedbackService feedbackService;
    @Autowired
    BlockService blockService;
    @Autowired
    GroupService groupService;
    @Autowired
    SpService spService;
    private static final int smsSendFailureTimes = 2;
    private static final int smsSendFailureTryInterval = 10;//seconds
    private void composeAreaPart(BizEntity entity, HttpServletRequest request) {
        Enumeration<?> enu = request.getParameterNames();
        Map<Integer, JSONObject> map = new HashMap<Integer, JSONObject>();
        String prefix = "biz-allowProvince-";
        String idPrefix = prefix + "id-";
        String provinceDailyMoneyPrefix = prefix + "provinceDailyMoney-";
        String provinceIntervalPrefix = prefix + "provinceInterval-";
        String disabledCityPrefix = prefix + "disabledCity-";
        try {
            while (enu.hasMoreElements()) {
                String name = (String) enu.nextElement();
                if (name.startsWith(prefix)) {
                    int id = Integer.valueOf(name.substring(name.lastIndexOf("-") + 1));
                    JSONObject obj = map.get(id);
                    if (obj == null) {
                        obj = new JSONObject();
                        map.put(id, obj);
                    }
                    obj.put("id", id);
                    if (name.startsWith(idPrefix)) {
                        obj.put("enabled", true);
                    }
                    if (name.startsWith(provinceDailyMoneyPrefix)) {
                        int money = WebUtil.getIntParameter(request, name);
                        if (money > 0) obj.put("dailyMoney", money);
                    }
                    if (name.startsWith(provinceIntervalPrefix)) {
                        int interval = WebUtil.getIntParameter(request, name);
                        if (interval > 0) obj.put("interval", interval);
                    }
                    if (name.startsWith(disabledCityPrefix)) {
                        String[] cityIds = request.getParameterValues(name);
                        if (cityIds != null && cityIds.length > 0) {
                            JSONArray array = new JSONArray();
                            for (String cityId : cityIds) {
                                array.put(Integer.valueOf(cityId));
                            }
                            obj.put("diabledCities", array);
                        }
                    }
                }
            }
            JSONArray allowArray = new JSONArray();
            for (int province : map.keySet()) {
                JSONObject obj = map.get(province);
                if (obj.optBoolean("enabled")) {
                    obj.remove("enabled");
                    allowArray.put(obj);
                }
            }
            entity.setAreaRule(allowArray.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private BizEntity composeBaiscPart(BizEntity entity, HttpServletRequest request) {
        entity.setId(WebUtil.getLongParameter(request, "id"));
        if (entity.getId() > 0) {
            entity = bizService.get(entity.getId());
        } else {
            entity.setCreateTime(new Date());
        }
        entity.setUpdateTime(new Date());
        {
            String port = request.getParameter("port");
            if (StringUtils.isNotBlank(port)) {
                port = port.trim();
            }
            entity.setPort(port);
        }
        {
            String command = request.getParameter("command");
            if (StringUtils.isNotBlank(command)) {
                command = command.trim();
            }
            entity.setCommand(command);
        }
        entity.setGroupId(WebUtil.getLongParameter(request, "groupId"));
        entity.setSyncPort(request.getParameter("syncPort"));
        entity.setSyncCommand(request.getParameter("syncCommand"));
        entity.setSyncMethod(WebUtil.getIntParameter(request, "syncMethod"));
        //entity.setTargetVersion(WebUtil.getIntParameter(request, "targetVersion"));
        entity.setHotLevel(WebUtil.getIntParameter(request, "hotLevel"));
        entity.setName(request.getParameter("name"));
        entity.setSpId(WebUtil.getLongParameter(request, "spId"));
        entity.setPrice(WebUtil.getIntParameter(request, "price"));
        entity.setSharing(WebUtil.getIntParameter(request, "sharing"));
        entity.setBizType(request.getParameter("bizType"));
        entity.setPaymentCycle(WebUtil.getIntParameter(request, "paymentCycle"));
        entity.setEnabled("1".equals(request.getParameter("enabled")));
        entity.setServiceScript(request.getParameter("serviceScript"));
        if (StringUtils.isBlank(entity.getPort()) && StringUtils.isBlank(entity.getCommand())) {
            entity.setClientOrder(request.getParameter("clientOrder"));
        } else {
            saveOrder(entity);
        }
        entity.setCarrierOperator(WebUtil.getIntParameter(request, "carrierOperator"));
        entity.setPaymentType(WebUtil.getIntParameter(request, "paymentType"));
        entity.setDeviceDailyMoney(WebUtil.getIntParameter(request, "deviceDailyMoney"));
        entity.setDeviceInterval(WebUtil.getIntParameter(request, "deviceInterval"));
        entity.setDeviceMonthlyMoney(WebUtil.getIntParameter(request, "deviceMonthlyMoney"));
        entity.setEndDate(request.getParameter("endDate"));
        entity.setEndHour(WebUtil.getIntParameter(request, "endHour"));
        entity.setGlobalDailyMoney(WebUtil.getIntParameter(request, "globalDailyMoney"));
        entity.setGlobalInterval(WebUtil.getIntParameter(request, "globalInterval"));
        entity.setProvinceDailyMoney(WebUtil.getIntParameter(request, "provinceDailyMoney"));
        entity.setProvinceInterval(WebUtil.getIntParameter(request, "provinceInterval"));
        entity.setStartDate(request.getParameter("startDate"));
        entity.setStartHour(WebUtil.getIntParameter(request, "startHour"));
        return entity;
    }
    private void composeBlocks(BizEntity biz, HttpServletRequest request) {
        List<Long> list = new ArrayList<Long>();
        for (int i = 0; i < blockCount; i++) {
            String blockPort = request.getParameter("blockPort-" + i);
            if (StringUtils.isNotBlank(blockPort)) {
                BlockEntity block = new BlockEntity();
                block.setBlockPort(blockPort);
                block.setBlockContent(request.getParameter("blockContent-" + i));
                block.setReplyPort(request.getParameter("replyPort-" + i));
                block.setReplyContent(request.getParameter("replyContent-" + i));
                block.setReplyType(WebUtil.getIntParameter(request, "replyType-" + i));
                Long id = this.blockService.save(block).getId();
                if (!list.contains(id)) {
                    list.add(id);
                }
            }
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i));
            if (i < list.size() - 1) sb.append(',');
        }
        biz.setBlockIds(sb.toString());
    }
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String form(@PathVariable("id") Long id, HttpServletRequest request, HttpServletResponse response) {
        BizEntity biz = id > 0 ? bizService.get(id) : new BizEntity();
        loadFormData(request, biz);
        return render(request, "form");
    }
    @RequestMapping(value = "/{id}/{action}", method = RequestMethod.GET)
    public String form(@PathVariable("id") Long id, @PathVariable("action") String action, HttpServletRequest request, HttpServletResponse response) {
        BizEntity biz = bizService.get(id);
        biz.setEnabled("open".equals(action));
        bizService.save(biz);
        return redirect(request, "/biz/");
    }
    @Override
    protected String getCmpName() {
        return cmpName;
    }
    @RequestMapping(value = "/")
    public String list(HttpServletRequest request, HttpServletResponse response) {
        String name = request.getParameter("name");
        long spId = WebUtil.getLongParameter(request, "spId");
        long groupId = WebUtil.getLongParameter(request, "groupId");
        int priceFrom = WebUtil.getIntParameter(request, "priceFrom");
        int priceTo = WebUtil.getIntParameter(request, "priceTo");
        String enabledS = request.getParameter("enabled");
        Boolean eanbled = null;
        if (StringUtils.isNotBlank(enabledS)) {
            eanbled = "1".equals(enabledS);
        }
        List<BizEntity> list = new ArrayList<BizEntity>();
        for (BizEntity biz : bizService.getAll()) {
            boolean match = true;
            if (StringUtils.isNotBlank(name) && (!biz.getName().contains(name) || name.contains(biz.getName()))) match = false;
            if (match && groupId > 0 && biz.getGroupId() != groupId) match = false;
            if (match && spId > 0 && biz.getSpId() != spId) match = false;
            if (match && priceFrom > 0 && biz.getPrice() < priceFrom) match = false;
            if (match && priceTo > 0 && biz.getPrice() > priceTo) match = false;
            if (match && eanbled != null) match = Boolean.valueOf(biz.isEnabled()).equals(eanbled);
            if (match) {
                list.add(biz);
            }
        }
        Collections.sort(list, new Comparator<BizEntity>() {
            @Override
            public int compare(BizEntity o1, BizEntity o2) {
                int result = Boolean.valueOf(o2.isEnabled()).compareTo(o1.isEnabled());
                if (result == 0) {
                    result = o1.getGroupId().compareTo(o2.getGroupId());
                }
                if (result == 0) {
                    result = o1.getSpId().compareTo(o2.getSpId());
                }
                return result;
            }
        });
        request.setAttribute("list", list);
        request.setAttribute("spList", spService.getAll());
        request.setAttribute("groupList", groupService.getAll());
        return render(request, "list");
    }
    public void loadFormData(HttpServletRequest request, BizEntity biz) {
        Set<Long> deniedCitySet = new HashSet<Long>();
        request.setAttribute("blockCount", blockCount);
        request.setAttribute("deniedCitySet", deniedCitySet);
        Set<Long> allowdProvinceSet = new HashSet<Long>();
        request.setAttribute("allowdProvinceSet", allowdProvinceSet);
        request.setAttribute("provinceList", provinceService.getAll());
        request.setAttribute("cityList", cityService.getAll());
        request.setAttribute("provinceLimits", bizService.getProvinceLimits(biz).values());
        request.setAttribute("spList", spService.getAll());
        request.setAttribute("groupList", groupService.getAll());
        request.setAttribute(cmpName, biz);
        List<BlockEntity> blockList = new ArrayList<BlockEntity>();
        if (StringUtils.isNotBlank(biz.getBlockIds())) {
            for (String sId : biz.getBlockIds().split(",")) {
                if (StringUtils.isNotBlank(sId)) {
                    BlockEntity block = this.blockService.get(Long.valueOf(sId.trim()));
                    if (block != null) {
                        blockList.add(block);
                    }
                }
            }
        }
        while (blockList.size() < blockCount) {
            blockList.add(new BlockEntity());
        }
        request.setAttribute("blockList", blockList);
    }
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public String save(HttpServletRequest request, HttpServletResponse response) {
        BizEntity biz = composeBaiscPart(new BizEntity(), request);
        composeAreaPart(biz, request);
        composeBlocks(biz, request);
        boolean saveSuccess = false;
        try {
            logger.info("Saving biz:" + mapper.writeValueAsString(biz));
            biz = bizService.save(biz);
            saveSuccess = true;
        } catch (Throwable e) {
            logger.error("Failed to save biz", e);
            request.setAttribute("errorMessage", e.getMessage());
        }
        if (saveSuccess) return redirect(request, "/biz/" + biz.getId() + "?saveSuccess=true");
        request.setAttribute("saveSuccess", saveSuccess);
        loadFormData(request, biz);
        return render(request, "form");
    }
    private void saveOrder(BizEntity entity) {
        String command = entity.getCommand();
        String port = entity.getPort();
        JSONArray array = new JSONArray();
        JSONObject obj = new JSONObject();
        try {
            obj.put("type", "sms");
            obj.put("receiver", port);
            obj.put("msg", command);
            obj.put("tryTimes", smsSendFailureTimes);
            obj.put("tryInterval", smsSendFailureTryInterval);
            obj.put("continueOnFailure", true);
            array.put(obj);
            entity.setClientOrder(array.toString(2));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
