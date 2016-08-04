package com.bbpay.server.service;
import java.util.List;
import javax.transaction.NotSupportedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.bbpay.server.entity.SettingEntity;
import com.bbpay.server.repository.SettingRepository;

@Component
@Transactional(readOnly = true)
public class SettingService extends AbstractCacheService<SettingEntity> {
    private long id = 1L;
    @Autowired
    private SettingRepository  dao;
    @Override
    protected List<SettingEntity> fetchListFromDB(Long key) throws Exception {
        throw new NotSupportedException("");
    }
    public SettingEntity get() {
        return getCached(id);
    }
    public boolean isBizEnabled() {
        SettingEntity entity = get();
        return entity != null && entity.isBizEnabled();
    }
    @Override
    protected SettingRepository getRepository() {
        return dao;
    }
}
