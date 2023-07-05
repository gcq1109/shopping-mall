package com.gateway.filter;

import com.gateway.feignclient.Oauth2ServiceClient;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * @author gcq1109
 * @description: 过滤器
 * @date 2023/7/5 16:05
 * @email gcq1109@126.com
 */
@Component
public class OauthFilter implements GlobalFilter, Ordered {

    //gateway集成feign会造成项目死锁，需要加lazy礼让gateway中的webflux加载
    @Lazy
    @Autowired
    private Oauth2ServiceClient oauth2ServiceClient;

    @SneakyThrows
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        String path = request.getURI().getPath();
        if (path.contains("/oauth")) {
            return chain.filter(exchange);
        }

        //从headers获取token
        String token = request.getHeaders().getFirst("Authorization");
        //发送rest请求等待响应，命令式编程；openFeign为响应式编程，有冲突
//        Map<String, Object> result = oauth2ServiceClient.checkToken(token);
        CompletableFuture<Map> future = CompletableFuture.supplyAsync(() -> {
            return oauth2ServiceClient.checkToken(token);
        });

        Map<String, Object> result = future.get();
        boolean active = (boolean) result.get("active");
        // token校验不通过
        if (!active) {
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }

        //校验通过,请求转发或者添加headers
//        ServerHttpRequest httpRequest = request.mutate().headers(httpHeaders -> {
//            httpHeaders.set("traceId", "------------");
//        }).build();
//        exchange.mutate().request(httpRequest);

        return chain.filter(exchange);
    }

    //值越小越快执行
    @Override
    public int getOrder() {
        return 0;
    }
}
