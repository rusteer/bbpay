package com.bbpay.server.repository.stat;
import java.util.List;
import com.bbpay.server.entity.stat.BizProvinceStatEntity;
import com.bbpay.server.repository.framework.MyJpaRepository;

public interface BizProvinceStatRepository extends MyJpaRepository<BizProvinceStatEntity> {
    BizProvinceStatEntity findByStatDateAndBizIdAndProvinceId(String today, Long bizId, Long provinceId);
    List<BizProvinceStatEntity> findByStatDateAndProvinceId(String today, Long provinceId);
}
