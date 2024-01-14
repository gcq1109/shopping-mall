package com.admin.controller;

import com.admin.pojo.CommodityBrand;
import com.admin.service.CommodityBrandService;
import com.common.response.CommonResponse;
import com.common.response.ResponseCode;
import com.common.response.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author gcq1109
 * @email gcq1109@126.com
 */

@RestController
@RequestMapping("/commodity/brand")
public class CommodityBrandController {

    @Autowired
    private CommodityBrandService commodityBrandService;

    //商品类目查询
    @RequestMapping("/list")
    public CommonResponse getBrandListByCategoryId(@RequestParam(name = "categoryId") Long categoryId) {
        List<CommodityBrand> brandList = commodityBrandService.getBrandListByCategoryId(categoryId);
        return ResponseUtils.successResponse(brandList);
    }

    //新增品牌
    @RequestMapping("/createBrand")
    public CommonResponse createBrand(@RequestBody CommodityBrand commodityBrand) {
        try {
            commodityBrandService.createCategory(commodityBrand);
            return ResponseUtils.successResponse("success");
        } catch (Exception e) {
            return ResponseUtils.failResponse(ResponseCode.FAULT_REQUEST.getCode(), null, e.getMessage());
        }
    }

    //删除品牌
    @RequestMapping("/delete/{id}")
    public CommonResponse deleteBrand(@PathVariable Long id) {
//        commodityBrandService.deleteBrand(id);
        return ResponseUtils.successResponse("success");
    }

}
