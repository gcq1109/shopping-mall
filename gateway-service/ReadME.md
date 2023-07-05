1.springcloud gateway -->注册到nacos注册中心

2.权限校验两种实现

(1)集成oauth2，不推荐使用这种，gateway只是网关，只用作转发限流

(2)通过oauth的check_token接口进行token验证，推荐使用

=================================

一.通过gateway集成oauth实现权限校验 

1.设置权限校验AccessManager（白名单功能）

2.创建securityConfig类，融入dataSource以及AccessManager
    
    2.1 禁用httpBasic

    2.2 对于ajax请求，会先进发送options，应当放行

3.db交互

二.通过feign进行check token接口的调用，校验token

1.创建feignClient调用oauth_service

2.创建global filter，在filter接收token调用check token接口；
也有白名单功能，转发添加headers。


