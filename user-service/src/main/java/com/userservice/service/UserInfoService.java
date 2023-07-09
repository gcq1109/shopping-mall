package com.userservice.service;

import com.common.response.CommonResponse;

/**
 * @author gcq1109
 * @description: TODO
 * @date 2023/7/9 13:41
 * @email gcq1109@126.com
 */
public interface UserInfoService {
    /**
     * check手机号是否绑定
     *
     * @param personId
     * @return
     */
    CommonResponse checkPhoneBindStatus(String personId);

    CommonResponse bindPhoneNumber(String personId, String phoneNumber, String code);
}
