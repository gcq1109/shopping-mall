权限验证

1.原始方式：用户名+密码+token验证

2.spring security + jwt 验证，一般内网用的比较多

3.oauth2权限验证，也需要spring security支持

使用方式：

(1) 用户名+密码进行验证，但是需要client id 和 client secret，后台根据用户名和密码利用一套规则自动生成client id 和 client secret

(2) 客户端验证，通过验证码方式登录，后台没有密码。因此，采取客户端验证为当前用户创建client id和client secret，以此来保证用户顺利登陆

(3) 授权码登录，比如第三方网站登录，二维码登录。具体流程，三方网站拥有oauth服务器，比如wx，当使用三方网站登录B网站时，B网站会向wx的 oauth服务器发送登陆请求进行验证，验证成功后，wx会回调B网站的接口，发送用户信息给B网站

(4)隐藏模式，简单模式，不建议使用

======================================
开发流程：
1.重写oauth的userDetailService，获取用户信息
2.实现验证服务配置 OauthConfig
3.实现web security ,WebSecurityConfig