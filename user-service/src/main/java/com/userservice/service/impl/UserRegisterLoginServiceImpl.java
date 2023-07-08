package com.userservice.service.impl;

import com.alibaba.cloud.commons.lang.StringUtils;
import com.common.response.CommonResponse;
import com.common.response.ResponseCode;
import com.common.response.ResponseUtils;
import com.userservice.pojo.AuthGrantType;
import com.userservice.pojo.Oauth2Client;
import com.userservice.pojo.RegisterType;
import com.userservice.pojo.User;
import com.userservice.processor.RedisCommonProcessor;
import com.userservice.repo.OauthClientRepository;
import com.userservice.repo.UserRepository;
import com.userservice.service.UserRegisterLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author gcq1109
 * @description: TODO
 * @date 2023/7/5 21:29
 * @email gcq1109@126.com
 */
@Service
public class UserRegisterLoginServiceImpl implements UserRegisterLoginService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OauthClientRepository oauthClientRepository;

    @Autowired
    private RedisCommonProcessor redisCommonProcessor;

    @Autowired
    private RestTemplate restTemplate;

    @Resource(name = "transactionManager")
    private JpaTransactionManager transactionManager;

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
    @Override
//    @Transactional(propagation = Propagation.REQUIRED)
    public CommonResponse namePasswdRegister(User user) {
        if (userRepository.findByUserName(user.getUserName()) == null
                && oauthClientRepository.findByClientId(user.getUserName()) == null) {
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            String passwd = user.getPasswd();
            String encodePasswd = bCryptPasswordEncoder.encode(passwd);
            user.setPasswd(encodePasswd);

            Oauth2Client oauth2Client = Oauth2Client.builder()
                    .clientId(user.getUserName())
                    .clientSecret(encodePasswd)
                    .resourceIds(RegisterType.USER_PASSWORD.name())
                    .authorizedGranTypes(AuthGrantType.refresh_token.name().concat(",").concat(AuthGrantType.password.name()))
                    .scope("web")
                    .authorities(RegisterType.USER_PASSWORD.name())
                    .build();

            Integer uid = saveUserAndOauthClient(user, oauth2Client);

            String personId = uid + 10000000 + "";
            redisCommonProcessor.set(personId, user);

            return ResponseUtils.successResponse(
                    formatResponseContent(user,
                            generateOauthToken(AuthGrantType.password,
                                    user.getUserName(), passwd, user.getUserName(), encodePasswd)));
        }
        return ResponseUtils.failResponse(ResponseCode.FAULT_REQUEST.getCode(), null, "user is exist");
    }

    @Override
    public CommonResponse phoneCodeRegister(String phoneNumber, String code) {
        String cacheCode = String.valueOf(redisCommonProcessor.get(phoneNumber));
        if (StringUtils.isEmpty(cacheCode)) {
            return ResponseUtils.failResponse(ResponseCode.FAULT_REQUEST.getCode(), null, "code is expired");
        }
        if (!cacheCode.equalsIgnoreCase(code)) {
            return ResponseUtils.failResponse(ResponseCode.FAULT_REQUEST.getCode(), null, "code not equals");
        }

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encodePasswd = bCryptPasswordEncoder.encode(code);
        User user = userRepository.findByUserPhone(phoneNumber);
        if (user == null) {
            //随机用户名
            String userName = getSystemDefinedUserName(phoneNumber);
            user = User.builder()
                    .userName(userName)
                    .passwd("")
                    .userPhone(phoneNumber)
                    .userRole(RegisterType.PHONE_NUMBER.name())
                    .build();
            Oauth2Client oauth2Client = Oauth2Client.builder()
                    .clientId(phoneNumber)
                    .clientSecret(encodePasswd)
                    .resourceIds(RegisterType.PHONE_NUMBER.name())
                    .authorizedGranTypes(AuthGrantType.refresh_token.name().concat(",").concat(AuthGrantType.client_credentials.name()))
                    .scope("web")
                    .authorities(RegisterType.PHONE_NUMBER.name())
                    .build();
            Integer userId = this.saveUserAndOauthClient(user, oauth2Client);
            String personId = userId + 10000000 + "";
            redisCommonProcessor.set(personId, user);
        } else {
            //二次登录
            oauthClientRepository.updateSecretByClientId(encodePasswd, phoneNumber);
        }
        return ResponseUtils.successResponse(formatResponseContent(user
                , generateOauthToken(AuthGrantType.password, user.getUserName(), encodePasswd, user.getUserName(), encodePasswd)));
    }

    private String getSystemDefinedUserName(String phoneNumber) {
        //前缀 MALL + 当前时间戳 + 手机号后四位
        return "MALL" + System.currentTimeMillis() + phoneNumber.substring(phoneNumber.length() - 4);
    }

    private Integer saveUserAndOauthClient(User user, Oauth2Client oauth2Client) {
        DefaultTransactionDefinition defaultTransactionDefinition = new DefaultTransactionDefinition();
        defaultTransactionDefinition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        defaultTransactionDefinition.setTimeout(30);
        TransactionStatus status = transactionManager.getTransaction(defaultTransactionDefinition);
        try {
            user = userRepository.save(user);
            oauthClientRepository.save(oauth2Client);
            transactionManager.commit(status);
        } catch (Exception e) {
            if (!status.isCompleted()) {
                transactionManager.rollback(status);
            }
            throw new UnsupportedOperationException("DB save failed");
        }

        return user.getId();
    }

    private Map generateOauthToken(AuthGrantType authGrantType,
                                   String username,
                                   String passwd,
                                   String clientId,
                                   String clientSecret) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", authGrantType.name());
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        if (authGrantType == AuthGrantType.password) {
            params.add("username", username);
            params.add("password", passwd);
        }

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, httpHeaders);
        return restTemplate.postForObject("http://oauth2-service/oauth/token", requestEntity, Map.class);
    }

    private Map formatResponseContent(User user, Map authClient) {
        Map<String, Object> result = new HashMap<>();
        result.put("user", user);
        result.put("oauth", authClient);
        return result;
    }

}
