package com.admin.repo;

import com.admin.pojo.CommodityAttr;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommodityAttrRepository extends JpaRepository<CommodityAttr, Long> {

    List<CommodityAttr> getByGroupId(Long groupId);

}
