package com.bbpay.server.repository;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import com.bbpay.server.entity.DeviceEntity;
import com.bbpay.server.repository.framework.MyJpaRepository;

public interface DeviceRepository extends MyJpaRepository<DeviceEntity> {
    @Query("  from DeviceEntity a where a.imei=?1")
    List<DeviceEntity> getByImei(String imei);
}
