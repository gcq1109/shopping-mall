package com.userservice.service;

import com.common.response.CommonResponse;
import com.userservice.pojo.User;

/**
 * @author gcq1109
 * @description: TODO
 * @date 2023/7/7 15:32
 * @email gcq1109@126.com
 */
public interface UserRegisterLoginService {

    /**
     * 用户名密码注册
     *
     * @param user
     * @return
     */
    CommonResponse namePasswdRegister(User user);

    /**
     * 手机号验证码注册
     *
     * @param phoneNumber
     * @param code
     * @return
     */
    CommonResponse phoneCodeRegister(String phoneNumber, String code);
}
