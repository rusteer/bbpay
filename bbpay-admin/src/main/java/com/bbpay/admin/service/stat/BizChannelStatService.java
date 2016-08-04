package com.bbpay.admin.service.stat;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.bbpay.admin.entity.stat.BizChannelStatEntity;
import com.bbpay.admin.repository.stat.BizChannelStatRepository;
import com.bbpay.admin.service.framework.AbstractService;

@Component
@Transactional(readOnly = true)
public class BizChannelStatService extends AbstractService<BizChannelStatEntity> {
    private BizChannelStatRepository dao;
    public List<BizChannelStatEntity> findByStatDateAndBizId(String statDate, Long bizId) {
        return dao.findByStatDateAndBizId(statDate, bizId);
    }
    public List<BizChannelStatEntity> findByStatDateAndAppId(String statDate, Long appId) {
        return dao.findByStatDateAndAppId(statDate, appId);
    }
    public List<BizChannelStatEntity> findByStatDate(String statDate) {
        return dao.findByStatDate(statDate);
    }
    @Override
    protected BizChannelStatRepository getRepository() {
        return dao;
    }
    @Autowired
    public void setDao(BizChannelStatRepository dao) {
        this.dao = dao;
    }
}
