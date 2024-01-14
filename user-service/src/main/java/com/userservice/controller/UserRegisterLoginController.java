package com.userservice.controller;

import com.common.response.CommonResponse;
import com.userservice.pojo.User;
import com.userservice.service.UserRegisterLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author gcq1109
 * @description: 注册
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
    public CommonResponse phoneCodeRegister(@RequestParam String phoneNumber,
                                            @RequestParam String code) {
        return userRegisterLoginService.phoneCodeRegister(phoneNumber, code);
    }

    @RequestMapping("/gitee")
    public CommonResponse thirdPartyGiteeRegister(@RequestBody HttpServletRequest request) {
        return userRegisterLoginService.thirdPartyGiteeRegister(request);
    }

    @PostMapping("/login")
    public CommonResponse login(@RequestParam String userName,
                                @RequestParam String password) {
        return userRegisterLoginService.login(userName, password);
    }
}
