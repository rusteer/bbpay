package com.bbpay.server.repository;
import com.bbpay.server.entity.IpEntity;
import com.bbpay.server.repository.framework.MyJpaRepository;

public interface IpRepository extends MyJpaRepository<IpEntity> {
    IpEntity findByIp(String ip);
}
