package com.admin.service;

import com.admin.pojo.CommodityBrand;
import com.admin.repo.CommodityBrandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author gcq1109
 * @description: 品牌服务
 * @email gcq1109@126.com
 */
@Service
public class CommodityBrandService {

    @Autowired
    private CommodityBrandRepository commodityBrandRepository;

    public void createCategory(CommodityBrand commodityBrand) {
        CommodityBrand brand = commodityBrandRepository.findByName(commodityBrand.getName());
        if (brand != null) {
            throw new RuntimeException("already exists!");
        }
        brand.setCreateTime(new Date());
        brand.setUpdateTime(new Date());
        commodityBrandRepository.save(brand);
    }

    public List<CommodityBrand> getBrandListByCategoryId(Long categoryId) {
        List<CommodityBrand> brandList = commodityBrandRepository.getBrandListByCategoryId(categoryId);
        return brandList;
    }
}
