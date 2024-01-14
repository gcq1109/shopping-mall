package com.admin.service;

import com.admin.pojo.*;
import com.admin.processor.RedisCommonProcessor;
import com.admin.repo.CommodityBrandRepository;
import com.admin.repo.CommodityCategoryBrandRepository;
import com.admin.repo.CommodityCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author gcq1109
 * @email gcq1109@126.com
 */
@Service
public class CommodityCategoryService {


    @Autowired
    private RedisCommonProcessor redisCommonProcessor;

    @Autowired
    private CommodityCategoryRepository commodityCategoryRepository;

    @Autowired
    private CommodityCategoryBrandRepository commodityCategoryBrandRepository;

    @Autowired
    private CommodityBrandRepository commodityBrandRepository;

    /**
     * 设计模式：组合模式
     *
     * @return
     */
    public CommodityCategoryComposite getCateGoryList() {
        Object cacheCategory = redisCommonProcessor.get("category");
        if ((cacheCategory != null)) {
            return (CommodityCategoryComposite) cacheCategory;
        }
        List<CommodityCategory> categoryList = commodityCategoryRepository.findAll();
        CommodityCategoryComposite items = generateCategoryTree(categoryList);
        //添加缓存
        redisCommonProcessor.set("category", items);
        return items;
    }

    //组装类目
    private CommodityCategoryComposite generateCategoryTree(List<CommodityCategory> categoryList) {
        List<CommodityCategoryComposite> composites = new ArrayList<>(categoryList.size());
        categoryList.forEach(dbItem -> composites.add(
                CommodityCategoryComposite.
                        builder()
                        .id(dbItem.getId())
                        .name(dbItem.getName())
                        .parentId(dbItem.getParentId())
                        .build()
        ));
        Map<Long, List<CommodityCategoryComposite>> groupList =
                composites.stream().collect(Collectors.groupingBy(CommodityCategoryComposite::getParentId));
        composites.stream().forEach(composite -> {
            composite.setChild(groupList.get(composite.getId()) == null ? new ArrayList<>() : groupList.get(composite.getId()));
        });
        return composites.size() == 0 ? null : composites.get(0);
    }

    @Transactional
    public void createCategory(CommodityCategory commodityCategory) {
        CommodityCategory category = commodityCategoryRepository.findByName(commodityCategory.getName());
        if (category != null) {
            throw new RuntimeException("already exists!");
        }
        commodityCategory.setCreateTime(new Date());
        commodityCategory.setUpdateTime(new Date());
        commodityCategoryRepository.save(commodityCategory);
        //清除缓存
        redisCommonProcessor.remove("category");
    }

    @Transactional
    public void deleteCategory(Long id) {
        commodityCategoryRepository.deleteById(id);
        commodityCategoryBrandRepository.deleteByCategoryId(id);
        //清除缓存
        redisCommonProcessor.remove("category");
    }

    @Transactional
    public void createBrandRelation(Long brandId, Long categoryId) {
        Optional<CommodityCategory> category = commodityCategoryRepository.findById(categoryId);
        if (!category.isPresent()) {
            throw new RuntimeException("categoryId is invalid");
        }

        Optional<CommodityBrand> brandOptional = commodityBrandRepository.findById(brandId);
        if (!brandOptional.isPresent()) {
            throw new RuntimeException("categoryId is invalid");
        }
        CommodityCategoryBrand categoryBrand = CommodityCategoryBrand.builder()
                .brandId(brandId)
                .categoryId(categoryId).build();
        commodityCategoryBrandRepository.save(categoryBrand);
    }

    public void deleteBrandRelation(Long brandId, Long categoryId) {
        CommodityCategoryBrandPrimaryKey categoryBrandPrimaryKey = CommodityCategoryBrandPrimaryKey.builder()
                .categoryId(categoryId)
                .brandId(brandId).build();
        commodityCategoryBrandRepository.deleteById(categoryBrandPrimaryKey);
    }
}
