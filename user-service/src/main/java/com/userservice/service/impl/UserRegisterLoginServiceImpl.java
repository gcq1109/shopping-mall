package com.userservice.service.impl;

import com.alibaba.cloud.commons.lang.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.common.response.CommonResponse;
import com.common.response.ResponseCode;
import com.common.response.ResponseUtils;
import com.userservice.config.GiteeConfig;
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
import javax.servlet.http.HttpServletRequest;
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
    private RestTemplate innerRestTemplate;

    @Autowired
    private RestTemplate outerRestTemplate;

    @Resource(name = "transactionManager")
    private JpaTransactionManager transactionManager;

    @Autowired
    private GiteeConfig giteeConfig;

    @Override
//    @Transactional(propagation = Propagation.REQUIRED)
    public CommonResponse namePasswdRegister(User user) {
        //用户已存在
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
            redisCommonProcessor.setTimeoutDays(personId, user, 30);

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
            redisCommonProcessor.setTimeoutDays(personId, user, 30);
        } else {
            //二次登录
            oauthClientRepository.updateSecretByClientId(encodePasswd, phoneNumber);
        }
        return ResponseUtils.successResponse(formatResponseContent(user
                , generateOauthToken(AuthGrantType.client_credentials, null, null, phoneNumber, encodePasswd)));
    }

    @Override
    public CommonResponse thirdPartyGiteeRegister(HttpServletRequest request) {
        String code = request.getParameter("code");
        String state = request.getParameter("state");
        if (!giteeConfig.getState().equalsIgnoreCase(state)) {
            throw new UnsupportedOperationException("Invalid state");
        }
        String tokenUrl = String.format(giteeConfig.getTokenUrl(), giteeConfig.getClientId()
                , giteeConfig.getClientSecret(), giteeConfig.getCallBack(), code);
        JSONObject tokenResult = outerRestTemplate.postForObject(tokenUrl, null, JSONObject.class);
        String token = String.valueOf(tokenResult.get("access_token"));

        String userUrl = String.format(giteeConfig.getUserUrl(), token);
        JSONObject userInfo = outerRestTemplate.getForObject(userUrl, JSONObject.class);

        //确保用户名唯一
        String userName = giteeConfig.getState().concat("_" + userInfo.get("name"));
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encodePasswd = bCryptPasswordEncoder.encode(userName);

        User user = userRepository.findByUserName(userName);
        if (user == null) {
            user = User.builder()
                    .userName(userName)
                    .passwd("")
                    .userRole(RegisterType.THIRTY_PARTY.name())
                    .build();
            Oauth2Client oauth2Client = Oauth2Client.builder()
                    .clientId(userName)
                    .clientSecret(encodePasswd)
                    .resourceIds(RegisterType.THIRTY_PARTY.name())
                    .authorizedGranTypes(AuthGrantType.refresh_token.name().concat(",").concat(AuthGrantType.client_credentials.name()))
                    .scope("web")
                    .authorities(RegisterType.THIRTY_PARTY.name())
                    .build();
            Integer userId = this.saveUserAndOauthClient(user, oauth2Client);
            String personId = userId + 10000000 + "";
            redisCommonProcessor.setTimeoutDays(personId, user, 30);
        }

        return ResponseUtils.successResponse(formatResponseContent(user
                , generateOauthToken(AuthGrantType.client_credentials, null, null, userName, userName)));
    }

    @Override
    public CommonResponse login(String userName, String password) {
        User user = userRepository.findByUserName(userName);
        if (user == null) {
            return ResponseUtils.failResponse(ResponseCode.FAULT_REQUEST.getCode(), null, "user not exist");
        }

        Map content = formatResponseContent(user,
                generateOauthToken(AuthGrantType.password, userName, password, userName, password));
        String personId = user.getId() + 10000000 + "";
        redisCommonProcessor.setTimeoutDays(personId, user, 30);
        return ResponseUtils.successResponse(content);
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
        return innerRestTemplate.postForObject("http://oauth2-service/oauth/token", requestEntity, Map.class);
    }

    private Map formatResponseContent(User user, Map authClient) {
        Map<String, Object> result = new HashMap<>();
        result.put("user", user);
        result.put("oauth", authClient);
        return result;
    }

}
