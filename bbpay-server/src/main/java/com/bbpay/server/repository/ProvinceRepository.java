package com.bbpay.server.repository;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import com.bbpay.server.entity.ProvinceEntity;
import com.bbpay.server.repository.framework.MyJpaRepository;

public interface ProvinceRepository extends MyJpaRepository<ProvinceEntity> {
    @Query("from ProvinceEntity a order by a.priority")
    List<ProvinceEntity> getAll();
}
