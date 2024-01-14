package com.userservice.service.impl;

import com.alibaba.cloud.commons.lang.StringUtils;
import com.common.response.CommonResponse;
import com.common.response.ResponseCode;
import com.common.response.ResponseUtils;
import com.userservice.pojo.User;
import com.userservice.processor.RedisCommonProcessor;
import com.userservice.repo.UserRepository;
import com.userservice.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author gcq1109
 * @email gcq1109@126.com
 */
@Service
public class UserInfoServiceImpl implements UserInfoService {

    @Autowired
    private RedisCommonProcessor redisCommonProcessor;

    @Autowired
    private UserRepository userRepository;

    @Override
    public CommonResponse checkPhoneBindStatus(String personId) {
        User user = (User) redisCommonProcessor.get(personId);
        boolean isBind = false;
        if (user != null) {
            isBind = user.getUserPhone() != null;
            return ResponseUtils.successResponse(isBind);
        }

        Integer userId = Integer.parseInt(personId) - 10000000;
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            isBind = userOptional.get().getUserPhone() != null;
            redisCommonProcessor.setTimeoutDays(personId, userOptional.get(), 30);
            return ResponseUtils.successResponse(isBind);
        }
        return ResponseUtils.failResponse(ResponseCode.FAULT_REQUEST.getCode(), null, "user not exist");
    }

    @Override
    public CommonResponse bindPhoneNumber(String personId, String phoneNumber, String code) {
        try {
            String cacheCode = String.valueOf(redisCommonProcessor.get(phoneNumber));
            if (StringUtils.isEmpty(cacheCode)) {
                return ResponseUtils.failResponse(ResponseCode.FAULT_REQUEST.getCode(), null, "code is expired");
            }
            if (!cacheCode.equalsIgnoreCase(code)) {
                return ResponseUtils.failResponse(ResponseCode.FAULT_REQUEST.getCode(), null, "code not equals");
            }
            Integer id = Integer.parseInt(personId) - 10000000;
            Optional<User> userOptional = userRepository.findById(id);
            if (userOptional.isPresent()) {
                userRepository.updateUserPhoneById(phoneNumber, id);
                //原始的延时双删
                redisCommonProcessor.remove(personId);
                return ResponseUtils.successResponse(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //原始的延时双删
            try {
                Thread.sleep(3000);
                redisCommonProcessor.remove(personId);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return ResponseUtils.failResponse(ResponseCode.FAULT_REQUEST.getCode(), null, "bind user failed");
    }
}
