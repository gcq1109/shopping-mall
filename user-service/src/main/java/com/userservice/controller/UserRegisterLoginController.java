package com.userservice.controller;

import com.common.response.CommonResponse;
import com.userservice.pojo.User;
import com.userservice.service.UserRegisterLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author gcq1109
 * @description: 注册
 * @date 2023/7/5 21:27
 * @email gcq1109@126.com
 */
@RestController
@RequestMapping("/user/register")
public class UserRegisterLoginController {

    @Autowired
    private UserRegisterLoginService userRegisterLoginService;

    @PostMapping("/name-passwd")
    public CommonResponse namePasswdRegister(@RequestBody User user) {
        return userRegisterLoginService.namePasswdRegister(user);
    }

    @PostMapping("/phone-code")
    public CommonResponse namePasswdRegister(@RequestParam String phoneNumber,
                                             @RequestParam String code) {
        return userRegisterLoginService.phoneCodeRegister(phoneNumber, code);
    }
}
