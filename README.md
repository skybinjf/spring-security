# spring-security

集成 spring securit, spring security oauth 和 spring social，实现 用户名密码登录，手机验证码登录，社交账号登录，基于jwt的sso，集群session管理等功能。

## 开发步骤

1.引入依赖(pom.xml)

```
<dependency>
	<groupId>com.imooc.security</groupId>
	<artifactId>imooc-security-browser</artifactId>
	<version>1.0.0-SNAPSHOT</version>
</dependency>
```

2.配置系统(参见 application-example.properties)

3.增加 UserDetailsService 接口实现

4.如果需要记住我功能，需要创建数据库表(参见 db.sql)

5.如果需要社交登录功能，需要以下额外的步骤
1).配置 appId 和 appSecret
2).创建并配置用户注册页面，并实现注册服务(需要配置访问权限)，注意在服务中要调用 ProviderSignInUtils 的doPostSignUp方法。
3).添加 SocialUserDetailsService 接口实现
4).创建社交登录用的表 (参见 db.sql)

## application-example.properties

```
#数据库连接配置
spring.datasource.driver-class-name = com.mysql.jdbc.Driver
spring.datasource.url= jdbc:mysql://127.0.0.1:3306/test?useUnicode=yes&characterEncoding=UTF-8&useSSL=false
spring.datasource.username = root
spring.datasource.password = 123456
#是否自动生成/修改数据库表
spring.jpa.generate-ddl=true
#是否在控制台打印sql语句
spring.jpa.show-sql=true
#打印sql语句时是否格式化
spring.jpa.properties.hibernate.format_sql=true
#数据库表和字段命名策略
spring.jpa.hibernate.naming.implicit-strategy = com.imooc.security.rbac.repository.support.ImoocImplicitNamingStrategy
#连接池及重试配置，防止mysql如果8小时自动断开连接问题
spring.datasource.max-active=100
spring.datasource.min-idle=10
spring.datasource.min-evictable-idle-time-millis = 1800000
spring.datasource.test-on-borrow=true
spring.datasource.test-on-return=true
spring.datasource.test-while-idle=true
spring.datasource.validation-query=select 1

#集群session存储方式
spring.session.store-type = none
#session超时时间，单位秒
server.session.timeout = 600

#security.basic.enabled = false

server.port = 8080

#浏览器环境配置项，参见BrowserProperties
#imooc.security.browser.signInPage = /demo-signIn.html
#imooc.security.browser.signInResponseType = REDIRECT
#imooc.security.browser.singInSuccessUrl = /manage.html
#imooc.security.browser.rememberMeSeconds = 294000
#imooc.security.browser.signOutUrl = /demo-logout.html
#imooc.security.browser.signUpUrl = /demo-signUp.html

#session管理相关配置，参见SessionProperties
#imooc.security.browser.session.maximumSessions = 1
#imooc.security.browser.session.maxSessionsPreventsLogin = false
#imooc.security.browser.session.sessionInvalidUrl = /imooc-session-invalid.html

#图片验证码配置项，参见ImageCodeProperties
#imooc.security.code.image.length = 4
#imooc.security.code.image.width = 100
#imooc.security.code.image.height = 30
#imooc.security.code.image.expireIn = 30
#imooc.security.code.image.url = /user/*

#短信验证码配置项，参见SmsCodeProperties
#imooc.security.code.sms.length = 6
#imooc.security.code.sms.expireIn = 60
#imooc.security.code.sms.url = /user/*

#社交登录功能拦截的url,参见SocilaProperties
#imooc.security.social.filterProcessesUrl = /qqLogin

#QQ登录配置，参见QQProperties
#imooc.security.social.qq.app-id = 
#imooc.security.social.qq.app-secret = 
#imooc.security.social.qq.providerId = callback.do

#微信登录配置，参见WeixinProperties
#imooc.security.social.weixin.app-id = wxd99431bbff8305a0
#imooc.security.social.weixin.app-secret = 60f78681d063590a469f1b297feff3c4
#imooc.security.social.weixin.providerId = weixin

#认证服务器注册的第三方应用配置项，参见OAuth2ClientProperties
#imooc.security.oauth2.clients[0].clientId = imooc
#imooc.security.oauth2.clients[0].clientSecret = imoocsecret
#imooc.security.oauth2.clients[0].accessTokenValidateSeconds = 3600
#imooc.security.oauth2.clients[1].clientId = test
#imooc.security.oauth2.clients[1].clientSecret = test

#OAuth2认证服务器的tokenStore的类型，可选值为redis和jwt，值为jwt时发出的token为jwt
#imooc.security.oauth2.tokenStore = redis
#使用jwt时为token签名的秘钥
#imooc.security.oauth2.jwtSigningKey = imooc

```

## SQL

```SQL
-- 记住我功能用的表
create table persistent_logins (username varchar(64) not null,
								series varchar(64) primary key,
								token varchar(64) not null,
								last_used timestamp not null);
-- 社交登录用的表
create table imooc_UserConnection (userId varchar(255) not null,
	providerId varchar(255) not null,
	providerUserId varchar(255),
	rank int not null,
	displayName varchar(255),
	profileUrl varchar(512),
	imageUrl varchar(512),
	accessToken varchar(512) not null,
	secret varchar(512),
	refreshToken varchar(512),
	expireTime bigint,
	primary key (userId, providerId, providerUserId));
create unique index UserConnectionRank on imooc_UserConnection(userId, providerId, rank);
```

## 扩展

向spring容器注册以下接口的实现，可以替换默认的处理逻辑

1.密码加密解密策略
org.springframework.security.crypto.password.PasswordEncoder

2.表单登录用户信息读取逻辑
org.springframework.security.core.userdetails.UserDetailsService

3.社交登录用户信息读取逻辑
org.springframework.social.security.SocialUserDetailsService

4.Session失效时的处理策略
org.springframework.security.web.session.InvalidSessionStrategy

5.并发登录导致前一个session失效时的处理策略配置
org.springframework.security.web.session.SessionInformationExpiredStrategy

6.退出时的处理逻辑
org.springframework.security.web.authentication.logout.LogoutSuccessHandler

7.短信发送的处理逻辑
com.imooc.security.core.validate.code.sms.SmsCodeSender

8.向spring容器注册名为imageValidateCodeGenerator的bean，可以替换默认的图片验证码生成逻辑,bean必须实现以下接口
com.imooc.security.core.validate.code.ValidateCodeGenerator

9.如果spring容器中有下面这个接口的实现，则在社交登录无法确认用户时，用此接口的实现自动注册用户，不会跳到注册页面
org.springframework.social.connect.ConnectionSignUp