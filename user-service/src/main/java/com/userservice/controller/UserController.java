package com.userservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author gcq1109
 * @description: TODO
 * @date 2023/7/4 16:07
 * @email gcq1109@126.com
 */
@RestController
public class UserController {

    @GetMapping
    public String test() {
        return "hello";
    }
}
