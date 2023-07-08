package com.userservice.service;

import com.common.response.CommonResponse;
import com.userservice.pojo.User;

import javax.servlet.http.HttpServletRequest;

/**
 * @author gcq1109
 * @description: TODO
 * @date 2023/7/7 15:32
 * @email gcq1109@126.com
 */
public interface UserRegisterLoginService {

    /**
     * 用户注册：
     * 1.db不存在该用户
     * 2.设置对象主体
     * 3.db存储
     * 4.redis存储
     * 5.验证登陆状态，通过oauth和db查询该用户
     * <p>
     * 注：若此时事务在方法主体控制，执行第五步的时候事务未提交，导致验证失败，需手动开启事务
     * TODO 思考：只在save方法中添加注解是否可行？
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

    /**
     * 三方账号登录
     *
     * @param request
     * @return
     */
    CommonResponse thirdPartyGiteeRegister(HttpServletRequest request);
}
