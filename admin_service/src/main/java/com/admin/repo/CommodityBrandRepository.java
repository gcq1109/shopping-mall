package com.admin.repo;

import com.admin.pojo.CommodityBrand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import java.util.List;
import java.util.Optional;

/**
 * @author gcq1109
 * @description: TODO
 * @email gcq1109@126.com
 */
@Repository
public interface CommodityBrandRepository extends JpaRepository<CommodityBrand, Long> {

    @Override
    @Lock(LockModeType.PESSIMISTIC_READ)//悲观锁
    @QueryHints({@QueryHint(name = "javax.persistence.lock.timeout", value = "3000")})
//超时时间
    Optional<CommodityBrand> findById(Long id);

    CommodityBrand findByName(String name);

    @Query(value = "select brand.* from commodity_brand brand left join commodity_category_brand cb on brand.id=cb.brand_id where cb.category_id = : categoryId", nativeQuery = true)
    List<CommodityBrand> getBrandListByCategoryId(Long categoryId);
}
