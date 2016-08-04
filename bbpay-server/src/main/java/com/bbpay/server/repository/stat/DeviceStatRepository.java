package com.bbpay.server.repository.stat;
import com.bbpay.server.entity.stat.DeviceStatEntity;
import com.bbpay.server.repository.framework.MyJpaRepository;

public interface DeviceStatRepository extends MyJpaRepository<DeviceStatEntity> {
    DeviceStatEntity findByStatDateAndDeviceId(String today, Long deviceId);
}
