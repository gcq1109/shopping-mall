package com.userservice.config;

import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.List;

/**
 * @author gcq1109
 * @description: restTemplate底层使用的是url connection，对于一些复杂场景不太适用，
 * 所以使用httpClient替换
 * @email gcq1109@126.com
 */
@Configuration
public class RestTemplateConfig {

    @Autowired
    private CloseableHttpClient httpClient;

    @Bean(name = "innerRestTemplate")
    @LoadBalanced //springcloud读取nacos注册中心添加此注解，springcloud以外的请求不可使用
    public RestTemplate getRestTemplate() {
        RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory());
        List<HttpMessageConverter<?>> messageConverters = restTemplate.getMessageConverters();
        Iterator<HttpMessageConverter<?>> iterator = messageConverters.iterator();
        while (iterator.hasNext()) {
            HttpMessageConverter<?> converter = iterator.next();
            if (converter instanceof StringHttpMessageConverter) {
                ((StringHttpMessageConverter) converter).setDefaultCharset(StandardCharsets.UTF_8);
            }
        }
        return restTemplate;
    }

    @Bean(name = "outerRestTemplate")
    public RestTemplate getOutRestTemplate() {
        RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory());
        List<HttpMessageConverter<?>> messageConverters = restTemplate.getMessageConverters();
        Iterator<HttpMessageConverter<?>> iterator = messageConverters.iterator();
        while (iterator.hasNext()) {
            HttpMessageConverter<?> converter = iterator.next();
            if (converter instanceof StringHttpMessageConverter) {
                ((StringHttpMessageConverter) converter).setDefaultCharset(StandardCharsets.UTF_8);
            }
        }
        return restTemplate;
    }

    /**
     * 使用CloseableHttpClient替换原有底层
     */
    @Bean
    public HttpComponentsClientHttpRequestFactory clientHttpRequestFactory() {
        HttpComponentsClientHttpRequestFactory httpComponentsClientHttpRequestFactory =
                new HttpComponentsClientHttpRequestFactory();
        httpComponentsClientHttpRequestFactory.setHttpClient(httpClient);
        return httpComponentsClientHttpRequestFactory;
    }

}
