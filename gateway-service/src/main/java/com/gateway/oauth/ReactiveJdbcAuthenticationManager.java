//package com.gateway.oauth;
//
//import org.springframework.security.authentication.ReactiveAuthenticationManager;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.oauth2.common.OAuth2AccessToken;
//import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
//import org.springframework.security.oauth2.provider.OAuth2Authentication;
//import org.springframework.security.oauth2.provider.token.TokenStore;
//import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
//import org.springframework.security.oauth2.server.resource.BearerTokenAuthenticationToken;
//import reactor.core.publisher.Mono;
//
//import javax.sql.DataSource;
//
///**
// * @author gcq1109
// * @description: jdbc响应式权限校验管理器
// * @email gcq1109@126.com
// */
//public class ReactiveJdbcAuthenticationManager implements ReactiveAuthenticationManager {
//
//    private TokenStore tokenStore;
//
//    public ReactiveJdbcAuthenticationManager(DataSource dataSource) {
//        this.tokenStore = new JdbcTokenStore(dataSource);
//    }
//
//    @Override
//    public Mono<Authentication> authenticate(Authentication authentication) {
//        return Mono.justOrEmpty(authentication)
//                //通过将token放入header Authentication中进行访问，并且会携带bearer前缀
//                .filter(a -> a instanceof BearerTokenAuthenticationToken)
//                .cast(BearerTokenAuthenticationToken.class)
//                .map(BearerTokenAuthenticationToken::getToken)
//                .flatMap(accessToken -> {
//                    OAuth2AccessToken oAuth2AccessToken = this.tokenStore.readAccessToken(accessToken);
//                    if (oAuth2AccessToken == null) {
//                        //token不存在db
//                        return Mono.error(new InvalidTokenException("InvalidTokenException"));
//                    } else if (oAuth2AccessToken.isExpired()) {
//                        //token过期
//                        return Mono.error(new InvalidTokenException("InvalidTokenException, isExpired"));
//                    }
//                    //校验是否oauth2的token
//                    OAuth2Authentication oAuth2Authentication = this.tokenStore.readAuthentication(accessToken);
//                    if (oAuth2AccessToken == null) {
//                        return Mono.error(new InvalidTokenException("fake token"));
//                    }
//                    return Mono.justOrEmpty(authentication);
//                });
//    }
//}
