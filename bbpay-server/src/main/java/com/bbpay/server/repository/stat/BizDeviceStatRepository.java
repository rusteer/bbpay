package com.bbpay.server.repository.stat;
import java.util.List;
import com.bbpay.server.entity.stat.BizDeviceStatEntity;
import com.bbpay.server.repository.framework.MyJpaRepository;

public interface BizDeviceStatRepository extends MyJpaRepository<BizDeviceStatEntity> {
    BizDeviceStatEntity findByStatDateAndBizIdAndDeviceId(String today, Long bizId, Long deviceId);
    List<BizDeviceStatEntity> findByStatDateAndDeviceId(String today, Long deviceId);
}
