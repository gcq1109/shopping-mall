package com.admin.repo;

import com.admin.pojo.CommodityCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import java.util.Optional;

/**
 * @author gcq1109
 * @description: TODO
 * @email gcq1109@126.com
 */
@Repository
public interface CommodityCategoryRepository extends JpaRepository<CommodityCategory, Long> {
    CommodityCategory findByName(String name);


    @Override
    @Lock(LockModeType.PESSIMISTIC_READ)//悲观锁
    @QueryHints({@QueryHint(name = "javax.persistence.lock.timeout", value = "3000")})
//超时时间
    Optional<CommodityCategory> findById(Long id);
}
