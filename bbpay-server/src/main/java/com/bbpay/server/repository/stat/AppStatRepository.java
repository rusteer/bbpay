package com.bbpay.server.repository.stat;
import com.bbpay.server.entity.stat.AppStatEntity;
import com.bbpay.server.repository.framework.MyJpaRepository;

public interface AppStatRepository extends MyJpaRepository<AppStatEntity> {
    AppStatEntity findByStatDateAndAppId(String today, Long appId);
}
