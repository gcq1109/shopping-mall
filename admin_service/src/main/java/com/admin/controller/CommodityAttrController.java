package com.admin.controller;

import com.admin.pojo.CommodityAttr;
import com.admin.pojo.CommodityAttrGroup;
import com.admin.service.CommodityAttrService;
import com.common.response.CommonResponse;
import com.common.response.ResponseCode;
import com.common.response.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/commodity/attr")
public class CommodityAttrController {

    @Autowired
    private CommodityAttrService commodityAttrService;

    @RequestMapping(value = "/listGroup/{categoryId}", method = RequestMethod.GET)
    public CommonResponse getGroupListByCategoryId(@PathVariable(value = "categoryId") Long categoryId) {
        try {
            List<CommodityAttrGroup> attrGroupList = commodityAttrService.geAttrGroupListByCategoryId(categoryId);
            return ResponseUtils.successResponse(attrGroupList);
        } catch (Exception e) {
            return ResponseUtils.failResponse(ResponseCode.FAULT_REQUEST.getCode(), null, e.getMessage());
        }
    }

    @RequestMapping(value = "/list/{groupId}", method = RequestMethod.GET)
    public CommonResponse getListByGroupId(@PathVariable(value = "groupId") Long groupId) {
        try {
            List<CommodityAttr> attrList = commodityAttrService.getAttrListByGroupId(groupId);
            return ResponseUtils.successResponse(attrList);
        } catch (Exception e) {
            return ResponseUtils.failResponse(ResponseCode.FAULT_REQUEST.getCode(), null, e.getMessage());
        }
    }
}
