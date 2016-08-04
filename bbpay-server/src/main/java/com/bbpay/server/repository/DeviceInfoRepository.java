package com.bbpay.server.repository;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import com.bbpay.server.entity.DeviceInfoEntity;
import com.bbpay.server.repository.framework.MyJpaRepository;

public interface DeviceInfoRepository extends MyJpaRepository<DeviceInfoEntity> {
    @Query("from DeviceInfoEntity a where a.deviceId=?1  order by a.updateTime desc ")
    List<DeviceInfoEntity> findByDeviceId(Long deviceId);
    public Page<DeviceInfoEntity> findByDeviceId(Long deviceId, Pageable pageable);
    @Query("from DeviceInfoEntity a where a.deviceId=?1 and a.imsi=?2 and a.provinceId=?3 ")
    DeviceInfoEntity getByUnique(Long deviceId, String imsi, Long provinceId);
}
