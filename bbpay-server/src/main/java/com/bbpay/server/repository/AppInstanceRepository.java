package com.bbpay.server.repository;
import org.springframework.data.jpa.repository.Query;
import com.bbpay.server.entity.AppInstanceEntity;
import com.bbpay.server.repository.framework.MyJpaRepository;

public interface AppInstanceRepository extends MyJpaRepository<AppInstanceEntity> {
    @Query("from AppInstanceEntity a where a.appId=?1 and a.deviceId=?2 and a.packageName=?3 and a.versionCode=?4 and a.channelId=?5")
    AppInstanceEntity getByUnique(Long appId, Long deviceId, String packageName, int versionCode, int channelId);
}
