package com.bbpay.admin.service;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.bbpay.admin.entity.CpChannelEntity;
import com.bbpay.admin.repository.CpChannelRepository;
import com.bbpay.admin.service.framework.AbstractService;

@Component
@Transactional(readOnly = true)
public class CpChannelService extends AbstractService<CpChannelEntity> {
    @Autowired
    private CpChannelRepository dao;
    public List<CpChannelEntity> getChannelList(long cpId) {
        return dao.findByCpId(cpId);
    }
    @Override
    protected CpChannelRepository getRepository() {
        return dao;
    }
}
