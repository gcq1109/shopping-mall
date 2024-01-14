//package com.gateway.oauth;
//
//import com.alibaba.cloud.commons.lang.StringUtils;
//import org.springframework.security.authorization.AuthorizationDecision;
//import org.springframework.security.authorization.ReactiveAuthorizationManager;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.oauth2.provider.OAuth2Authentication;
//import org.springframework.security.web.server.authorization.AuthorizationContext;
//import org.springframework.stereotype.Component;
//import org.springframework.util.AntPathMatcher;
//import org.springframework.web.server.ServerWebExchange;
//import reactor.core.publisher.Mono;
//
//import java.util.Set;
//import java.util.concurrent.ConcurrentSkipListSet;
//
///**
// * @author gcq1109
// * @description: 响应式验证管理器
// * @email gcq1109@126.com
// */
//@Component
//public class AccessManager implements ReactiveAuthorizationManager<AuthorizationContext> {
//
//    //白名单
//    private Set<String> permitAll = new ConcurrentSkipListSet<>();
//
//    //正则校验
//    private static final AntPathMatcher antPathMatcher = new AntPathMatcher();
//
//    public AccessManager() {
//        permitAll.add("/**/oauth/**");
//    }
//
//    @Override
//    public Mono<AuthorizationDecision> check(Mono<Authentication> authentication, AuthorizationContext authorizationContext) {
//        //exchange包含request信息，根据访问路径判断是否放行，若不放行，则进行校验
//        ServerWebExchange exchange = authorizationContext.getExchange();
//
//        return authentication.map(auth -> {
//            String path = exchange.getRequest().getURI().getPath();
//            //白名单直接放行
//            if (checkPermit(path)) {
//                return new AuthorizationDecision(true);
//            }
//
//            //是否是oauth2,若不是直接拦截
//            if (auth instanceof OAuth2Authentication) {
//                OAuth2Authentication oAuth2Authentication = (OAuth2Authentication) auth;
//                String clientId = oAuth2Authentication.getOAuth2Request().getClientId();
//                if (StringUtils.isNotEmpty(clientId)) {
//                    return new AuthorizationDecision(true);
//                }
//            }
//            return new AuthorizationDecision(false);
//        });
//
//    }
//
//    private boolean checkPermit(String path) {
//        return permitAll.stream().anyMatch(p -> antPathMatcher.match(p, path));
//    }
//}
