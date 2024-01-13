package com.admin.repo;

import com.admin.pojo.CommodityCategoryBrand;
import com.admin.pojo.CommodityCategoryBrandPrimaryKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author gcq1109
 * @description: 无主键表查询需要将两个平等的category_id和brand_id组装查询
 * @date 2024/1/13 15:08
 * @email gcq1109@126.com
 */
@Repository
public interface CommodityCategoryBrandRepository extends JpaRepository<CommodityCategoryBrand, CommodityCategoryBrandPrimaryKey> {
    void deleteByCategoryId(Long id);
}
