package com.admin.service;

import com.admin.pojo.CommodityAttr;
import com.admin.pojo.CommodityAttrGroup;
import com.admin.repo.CommodityAttrGroupRepository;
import com.admin.repo.CommodityAttrRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommodityAttrService {

    @Autowired
    private CommodityAttrRepository commodityAttrRepository;

    @Autowired
    private CommodityAttrGroupRepository commodityAttrGroupRepository;

    public List<CommodityAttrGroup> geAttrGroupListByCategoryId(Long categoryId) {
        return commodityAttrGroupRepository.getByCategoryId(categoryId);
    }

    public List<CommodityAttr> getAttrListByGroupId(Long groupId) {
        return commodityAttrRepository.getByGroupId(groupId);
    }
}
