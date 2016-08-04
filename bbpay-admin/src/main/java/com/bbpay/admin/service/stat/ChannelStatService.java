package com.bbpay.admin.service.stat;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.bbpay.admin.entity.stat.ChannelStatEntity;
import com.bbpay.admin.repository.stat.ChannelStatRepository;
import com.bbpay.admin.service.framework.AbstractService;

@Component
@Transactional(readOnly = true)
public class ChannelStatService extends AbstractService<ChannelStatEntity> {
    @Autowired
    private ChannelStatRepository dao;
    @Autowired
    private BizChannelStatService bizChannelStatService;
    @Override
    protected ChannelStatRepository getRepository() {
        return dao;
    }
    public List<ChannelStatEntity> findByStatDate(String statDate) {
        return dao.findByStatDate(statDate);
    }
    public List<ChannelStatEntity> findByStatDateAndAppId(String statDate, Long appId) {
        return dao.findByStatDateAndAppId(statDate, appId);
    }
    public ChannelStatEntity findByUnique(String statDate, Long appId, int channelId) {
        return dao.findByStatDateAndAppIdAndChannelId(statDate, appId, channelId);
    }
}
