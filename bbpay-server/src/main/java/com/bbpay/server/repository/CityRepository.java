package com.bbpay.server.repository;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import com.bbpay.server.entity.CityEntity;
import com.bbpay.server.repository.framework.MyJpaRepository;

public interface CityRepository extends MyJpaRepository<CityEntity> {
    List<CityEntity> findByCityCode(String cityCode);
    @Query("from CityEntity a where a.province.id=?1 and a.isCaptial='Y'")
    CityEntity getCaptial(Long provinceId);
    @Query("from CityEntity a where a.province.id=?1")
    List<CityEntity> getListByProvinceId(Long provinceId);
}
