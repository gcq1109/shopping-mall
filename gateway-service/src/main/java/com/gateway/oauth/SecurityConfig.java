//package com.gateway.oauth;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.authentication.ReactiveAuthenticationManager;
//import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
//import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
//import org.springframework.security.config.web.server.ServerHttpSecurity;
//import org.springframework.security.oauth2.server.resource.web.server.ServerBearerTokenAuthenticationConverter;
//import org.springframework.security.web.server.SecurityWebFilterChain;
//import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
//
//import javax.sql.DataSource;
//
///**
// * @author gcq1109
// * @description: 配置类
// * @email gcq1109@126.com
// */
//@Configuration
//@EnableWebFluxSecurity
//public class SecurityConfig {
//
//    @Autowired
//    private DataSource dataSource;
//
//    @Autowired
//    private AccessManager accessManager;
//
//    //网络安全过滤
//    @Bean
//    public SecurityWebFilterChain webFluxSecurityWebFilterChain(ServerHttpSecurity serverHttpSecurity) {
//
//        //由权限管理器创建web过滤器,权限管理器需要db支持
//        ReactiveAuthenticationManager reactiveAuthenticationManager = new ReactiveJdbcAuthenticationManager(dataSource);
//        AuthenticationWebFilter authenticationWebFilter = new AuthenticationWebFilter(reactiveAuthenticationManager);
//        authenticationWebFilter.setServerAuthenticationConverter(new ServerBearerTokenAuthenticationConverter());
//
//        serverHttpSecurity.httpBasic().disable()
//                .csrf().disable()
//                .authorizeExchange()
//                //options请求放行
//                .pathMatchers(HttpMethod.OPTIONS).permitAll()
//                .anyExchange().access(accessManager)
//                //校验token
//                .and().addFilterAt(authenticationWebFilter, SecurityWebFiltersOrder.AUTHENTICATION);
//        return serverHttpSecurity.build();
//    }
//}
