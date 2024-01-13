package com.admin.controller;

import com.admin.pojo.CommodityCategory;
import com.admin.service.CommodityCategoryService;
import com.common.response.CommonResponse;
import com.common.response.ResponseCode;
import com.common.response.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author gcq1109
 * @date 2024/1/13 15:24
 * @email gcq1109@126.com
 */

@RestController
@RequestMapping("/commodity/category")
public class CommodityCategoryController {

    @Autowired
    private CommodityCategoryService commodityCategoryService;

    //商品类目查询
    @RequestMapping("/list")
    public CommonResponse getCategory() {
        return ResponseUtils.successResponse(commodityCategoryService.getCateGoryList());
    }

    //新增类目
    @RequestMapping("/create")
    public CommonResponse createCategory(@RequestBody CommodityCategory commodityCategory) {
        if (commodityCategory.getLevel() > 3 || commodityCategory.getLevel() <= 0) {
            return ResponseUtils.failResponse(ResponseCode.FAULT_REQUEST.getCode(), null, "0<level<=3!");
        }
        try {
            commodityCategoryService.createCategory(commodityCategory);
            return ResponseUtils.successResponse("success");
        } catch (Exception e) {
            return ResponseUtils.failResponse(ResponseCode.FAULT_REQUEST.getCode(), null, e.getMessage());
        }
    }

    //删除类目
    @RequestMapping("/delete/{id}")
    public CommonResponse deleteCategory(@PathVariable Long id) {
//        commodityCategoryService.deleteCategory(id);
        return ResponseUtils.successResponse("success");
    }
}
