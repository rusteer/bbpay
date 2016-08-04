package com.bbpay.server.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.bbpay.server.entity.AppInstanceEntity;
import com.bbpay.server.repository.AppInstanceRepository;
import com.bbpay.server.repository.framework.MyJpaRepository;

@Component
@Transactional(readOnly = true)
public class AppInstanceService extends AbstractService<AppInstanceEntity> {
    @Autowired
    private AppInstanceRepository dao;
    
    @Transactional(readOnly = false)
    public AppInstanceEntity loadOrSave(AppInstanceEntity temp) {
        Long appId = temp.getAppId();
        Long deviceId = temp.getDeviceId();
        String packageName = temp.getPackageName();
        int versionCode = temp.getVersionCode();
        AppInstanceEntity dbData = dao.getByUnique(appId, deviceId, packageName, versionCode,temp.getChannelId());
        if (dbData == null) {
            dbData = dao.save(temp);
        }
        return dbData;
    }
    @Override
    protected MyJpaRepository<AppInstanceEntity> getRepository() {
        return dao;
    }
}
