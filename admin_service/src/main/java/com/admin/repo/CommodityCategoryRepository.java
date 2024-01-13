package com.admin.repo;

import com.admin.pojo.CommodityCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author gcq1109
 * @description: TODO
 * @date 2024/1/13 15:08
 * @email gcq1109@126.com
 */
@Repository
public interface CommodityCategoryRepository extends JpaRepository<CommodityCategory, Long> {
    CommodityCategory findByName(String name);
}
