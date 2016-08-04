package com.bbpay.server.service;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.bbpay.server.entity.AppEntity;
import com.bbpay.server.entity.AppInstanceEntity;
import com.bbpay.server.entity.BizEntity;
import com.bbpay.server.entity.BizInstanceEntity;
import com.bbpay.server.repository.BizInstanceRepository;
import com.bbpay.server.repository.framework.MyJpaRepository;

@Component
@Transactional(readOnly = true)
public class BizInstanceService extends AbstractService<BizInstanceEntity> {
    @Autowired
    private AppService appService;
    @Autowired
    private BizInstanceRepository dao;
    public BizInstanceEntity create(AppInstanceEntity ai, BizEntity biz, Long orderId) {
        BizInstanceEntity entity = new BizInstanceEntity();
        entity.setOrderId(orderId);
        entity.setBizId(biz.getId());
        entity.setAppInstanceId(ai.getId());
        entity.setResult(-1);
        //dump keys start
        entity.setPrice(biz.getPrice());
        entity.setSharing(biz.getSharing());
        entity.setPackageName(ai.getPackageName());
        entity.setVersionCode(ai.getVersionCode());
        entity.setDeviceId(ai.getDeviceId());
        entity.setProvinceId(ai.getDeviceInfo().getProvinceId());
        entity.setCityId(ai.getDeviceInfo().getCityId());
        entity.setChannelId(ai.getChannelId());
        entity.setAppId(ai.getAppId());
        entity.setSpId(biz.getSpId());
        entity.setCarrierOperator(ai.getDeviceInfo().getCarrierOperator());
        AppEntity app = appService.get(ai.getAppId());
        if (app != null) {
            entity.setCpId(app.getCpId());
        }
        //dump keys end
        //record.setStepCount(stepCount);
        Date date = new Date();
        entity.setOrderTime(date);
        entity.setOrderDate(FormatUtil.dateFormat.format(date));
        return save(entity);
    }
    @Override
    protected MyJpaRepository<BizInstanceEntity> getRepository() {
        return dao;
    }
    
}
