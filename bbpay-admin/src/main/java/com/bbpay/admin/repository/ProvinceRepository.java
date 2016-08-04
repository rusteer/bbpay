package com.bbpay.admin.repository;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import com.bbpay.admin.entity.ProvinceEntity;
import com.bbpay.admin.repository.framework.MyJpaRepository;

public interface ProvinceRepository extends MyJpaRepository<ProvinceEntity> {
    @Query("from ProvinceEntity a order by a.priority")
    List<ProvinceEntity> getAll();
}
