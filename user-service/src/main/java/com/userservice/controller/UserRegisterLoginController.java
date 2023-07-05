package com.userservice.controller;

import com.userservice.service.UserRegisterLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author gcq1109
 * @description: TODO
 * @date 2023/7/5 21:27
 * @email gcq1109@126.com
 */
@RestController
@RequestMapping("/user/register")
public class UserRegisterLoginController {

    @Autowired
    private UserRegisterLoginService userRegisterLoginService;

    @PostMapping("/name-passwd")
    public void namePasswdRegister() {
//        userRegisterLoginService.namePasswdRegister();
    }
}
