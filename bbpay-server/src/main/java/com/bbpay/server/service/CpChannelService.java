package com.bbpay.server.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.bbpay.server.entity.CpChannelEntity;
import com.bbpay.server.repository.CpChannelRepository;
import com.bbpay.server.repository.framework.MyJpaRepository;

@Component
@Transactional(readOnly = true)
public class CpChannelService extends AbstractService<CpChannelEntity> {
    @Autowired
    private CpChannelRepository dao;
    public CpChannelEntity findByUnique(Long cpId, int channelId) {
        return dao.findByCpIdAndChannelId(cpId, channelId);
    }
    @Override
    protected MyJpaRepository<CpChannelEntity> getRepository() {
        return dao;
    }
}
