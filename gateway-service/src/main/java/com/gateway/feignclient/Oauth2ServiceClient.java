package com.gateway.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * @author gcq1109
 * @description: feign调用oauth2
 * @email gcq1109@126.com
 */
@FeignClient("oauth2-service")
public interface Oauth2ServiceClient {

    @RequestMapping("/oauth/check_token")
    Map<String, Object> checkToken(@RequestParam("token") String token);
}
