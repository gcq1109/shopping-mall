package com.sms.service;

import com.sms.config.TencentSmsConfig;
import com.sms.processor.RedisCommonProcessor;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.sms.v20210111.SmsClient;
import com.tencentcloudapi.sms.v20210111.models.DescribePhoneNumberInfoRequest;
import com.tencentcloudapi.sms.v20210111.models.DescribePhoneNumberInfoResponse;
import com.tencentcloudapi.sms.v20210111.models.PhoneNumberInfo;
import com.tencentcloudapi.sms.v20210111.models.SendSmsRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author gcq1109
 * @date 2023/7/7 15:52
 * @email gcq1109@126.com
 */
@Service
public class SmsService {

    @Autowired
    private RedisCommonProcessor redisCommonProcessor;

    @Autowired
    private TencentSmsConfig tencentSmsConfig;

    public void sendSms(String phoneNumber) {
        try {
            Credential credential = new Credential(tencentSmsConfig.getSecretId(), tencentSmsConfig.getSecretKey());

            //选择性配置
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setConnTimeout(60);
            httpProfile.setReqMethod("POST");

            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);

            SmsClient client = new SmsClient(credential, tencentSmsConfig.getRegion(), clientProfile);
            SendSmsRequest request = new SendSmsRequest();
            request.setSmsSdkAppId(tencentSmsConfig.getAppId());
            request.setSignName(tencentSmsConfig.getSignName());
            request.setTemplateId(tencentSmsConfig.getTemplateId().getPhoneCode());

            String code = getRandomPhoneCode();
            String[] templateParamsSet = {code};
            request.setTemplateParamSet(templateParamsSet);

            String[] phoneNumbers = {phoneNumber};
            request.setPhoneNumberSet(phoneNumbers);

            request.setSenderId(getNationalCode(client, phoneNumber));
            client.SendSms(request);
            redisCommonProcessor.setTimeout(phoneNumber, code, 300);
        } catch (TencentCloudSDKException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取国家编号，一般不需使用
     *
     * @param client
     * @param phoneNumber
     * @return
     */
    private String getNationalCode(SmsClient client, String phoneNumber) {
        DescribePhoneNumberInfoRequest request = new DescribePhoneNumberInfoRequest();
        String[] phoneNumbers = {phoneNumber};
        request.setPhoneNumberSet(phoneNumbers);
        DescribePhoneNumberInfoResponse response
                = null;
        try {
            response = client.DescribePhoneNumberInfo(request);
            PhoneNumberInfo[] phoneNumberInfoSet = response.getPhoneNumberInfoSet();
            if (phoneNumberInfoSet.length == 1) {
                return phoneNumberInfoSet[0].getNationCode();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getRandomPhoneCode() {
        return String.valueOf((Math.random() * 9 + 1) * 100000);
    }
}
