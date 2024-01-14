package com.admin.repo;

import com.admin.pojo.CommodityAttrGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommodityAttrGroupRepository extends JpaRepository<CommodityAttrGroup, Long> {

    List<CommodityAttrGroup> getByCategoryId(Long categoryId);
}
