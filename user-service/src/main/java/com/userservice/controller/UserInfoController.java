package com.userservice.controller;

import com.common.response.CommonResponse;
import com.userservice.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author gcq1109
 * @description: 用户信息
 * @date 2023/7/9 13:37
 * @email gcq1109@126.com
 */
@RestController
@RequestMapping("/user/info")
public class UserInfoController {

    @Autowired
    private UserInfoService userInfoService;
    @RequestMapping("/check-phone-bind-status")
    public CommonResponse checkPhoneBindStatus(@RequestHeader String personId){
        return userInfoService.checkPhoneBindStatus(personId);
    }

    @RequestMapping("/bind-phone-number")
    public CommonResponse bindPhoneNumber(@RequestParam String personId,
                                          @RequestParam String phoneNumber,
                                          @RequestParam String code){
        return userInfoService.bindPhoneNumber(personId,phoneNumber,code);
    }
}
