# 项目名称：BDP-NUTS

当前版本：`V 4.3.13`

### 下个版本添加：



### BDP-Nuts-4.3.13 -------------- 2024-04-08
1. 修复使用`Nginx`前`HTTPS`转发到后端的`HTTP`请求时回调地址变成`http`问题。（20240407）
2. 新增`nuts.config-home-top=true`配置提升`local.home`的优先级，默认`false`配置优先级低于多域名。（20240407）
3. 新增`BDP`菜单改动通知脚手架。（20240407）
4. 升级`bdp-static-5.1.1`版本。（20240408）
5. 升级`bdp-openapi-client-5.1.1`版本。（20240408）

### BDP-Nuts-4.3.12 -------------- 2024-03-22
1. 解决漏洞`spring`升级至`5.3.32`版本。（20240313）
2. 解决漏洞`spring-security`升级至`5.8.10`版本。（20240313）
3. 解决漏洞`spring-data-commons`升级至`2.7.17`版本。（20240313）
4. 新增打印请求头信息`nuts.print-headers-enable=false`用于排查问题默认关闭。（20240314）
5. 升级`bdp-static-4.6.7`版本。（20240314）
6. 升级`bdp-openapi-client-4.6.7`版本。（20240314）
7. 新增`LogoutSuccessCallback`登出的回调函数，业务直接继承放入容器即可。（20240315）
8. 修复刷菜单功能新增菜单没有刷入问题。（20240318）
9. 修复getLocalHome配置了local.home获取失败问题。（20240321）
10. 解决不配置local.home多网卡获取当前地址不准问题。（20240321）
11. 防刷支持配置内存保留请求过期时间nuts.check-request-save-times=单位为秒，最大为半个小时可以通过配置改小
    半小时之前的请求直接判为重复请求不允许访问，半小时只内的请求需要与内存中请求校验重复性，客户端电脑不能比服务端快超过半个小时。（20240322）

### BDP-Nuts-4.3.11 -------------- 2024-01-11
1. 优化当电脑有多网卡获取ip不准问题（20231212）
2. 在不对接BDP登录时默认访问支持多域名（20231212）
3. 刷出菜单的`xxx-menu.xml`配置文件里支持配置删除的菜单（20231213）
4. 新增删除菜单的方法`PlatformAPI.deleteMenus(List<DelMenu> delMenuList)`（20231213）
5. 新增设置用户附属信息的方法`PlatformAPI.saveUserAddInfo(List<UserAddInfo> userAddInfoList)`（20231213）
6. 版本升级`bdp-static`和`bdp-openapi-client`至`4.6.6`（20231213）
7. 脚手架适配`bdp3.x`对接第三方从`url`中获取`token`的`key`配置文件中默认为不配置（20231229）

### BDP-Nuts-4.3.10 -------------- 2023-11-15
1. 升级`bdp-static`至`4.6.5`版本（20231102）
2. 升级`bdp-openapi-client`至`4.6.5`版本解决`PlatformAPI.listUnitUsers(String unitCode)`返回数据不正确问题(20231102）
3. 解决漏洞`spring`升级至`5.3.30`版本。（20231102）
4. 解决漏洞`spring-security`升级至`5.8.8`版本。（20231102）
5. 优化防刷功能超过一分钟清楚缓存之后也不能重复请求。（20231115）

### BDP-Nuts-4.3.9 -------------- 2023-09-25
1. 将`swagger`相关的放行配置移动到配置文件中（20230617）
2. 开启`debug`日志等级可以打印所有请求头信息（20230917）
3. 跨网访问时`local.home`配置外网地址优先取，不配置从哪里来访问回到哪里（20230925）
4. 跨网访问时`SystemProperties.getLocalHome()` 从页面发请求获取配置可以自动识别内外网（20230925）

### BDP-Nuts-4.3.8 -------------- 2023-07-28
1. 优化关于设置响应头相关的配置（20230613）
2. 脚手架文档中新增常见问题及解决方式（20230613）
3. 解决漏洞`spring`升级至`5.3.29`版本。（20230728）
4. 解决漏洞`spring-security`升级至`5.8.5`版本。（20230728）
5. 脚手架文档中新增FAQ章节记录常见问题答疑。（20230728）
6. 新增`login.return.index.enable`配置控制`token`超时后登录是否始终会跳到项目首页，默认关闭。（20230808）
7. 新增`login.delegation.url`如果配置有值非`Ajax`请求就会转发到当前请求上，业务可以在这个请求中处理跳转登录逻辑。（20230808）

### BDP-Nuts-4.3.7 -------------- 2023-05-30
1. 优化配置中心访问慢的问题（20230522）
2. 优化登录成功的回调解决`Authentication`一直为空问题（20230530）
3. 优化静态资源请求直接放行不做任何业务功能（20230530）
4. 解决`loginSuccessCallback`为空时无法返回用户token问题
5. 优化`nuts.access-control-allow-methods`配置不配置时返回`GET,POST,OPTIONS,DELETE,PUT`配置时返回`GET,POST,OPTIONS, + 配置的`

### BDP-Nuts-4.3.6 -------------- 2023-05-09
1. 解决漏洞`activemq`升级至`5.16.6`版本。（20230428）
2. 升级`velocity`版本至`2.1.9`去除空白处理：如果变量 $param 不存在，则原样字符输出（20230508）
3. 新增取`token`相关的参数优先从请求头中的`cookie`中获取（20230508）

### BDP-Nuts-4.3.5 -------------- 2023-04-26
1. 完善测试类的接口(20230412）
2. 优化`ratelimiter`限流功能`nuts.sla.enable=true`为开启配置(20230424）
3. 解决漏洞`spring`升级至`5.3.27`版本。（20230424）
4. 解决漏洞`spring-security`升级至`5.8.3`版本。（20230424）
5. 解决依赖冲突`spring-data-commons`升级至`2.7.11`版本。（20230424）
6. 解决依赖冲突`org.slf4j`升级至`1.7.32`版本。（20230424）
7. 升级`bdp-openapi-client`至`4.5.8`版本。（20230426）

### BDP-Nuts-4.3.4 -------------- 2023-04-19
1. 解决关闭登录token过期没清空上下文还可以获取到用户信息的问题(20230412）
2. 升级`bdp-openapi-client`至`4.5.7`版本去除对低版本的`commons-httpclient`依赖(20230419）

### BDP-Nuts-4.3.3 -------------- 2023-04-04
1. 解决关闭登录开关携带userToken无法获取到用户信息的问题(20230329）

### BDP-Nuts-4.3.2 -------------- 2023-03-29
1. 由于已经优化了`bdp-common.js`的逻辑修改参数处理的默认值为`nuts.url-args-filter-enable=false`（20230329）
2. 去除`authCode`和`userToken`的关系存储存在安全问题（20230329）

### BDP-Nuts-4.3.1 -------------- 2023-03-27
1. 修复漏洞升级fh-ui至2.5.0版本(20230324)
2. 修复漏洞spring版本升级至5.3.26(20230327)
3. 修复漏洞spring-data-commons版本升级至2.7.10(20230327)
4. velocity升级后特殊符号出现转义取值方式有所更改${}可以使用$#{}(20230327)
5. 修复漏洞apollo-client版本升级至2.1.0-bdp.01(20230327)

### BDP-Nuts-4.3.0 -------------- 2023-03-15
1. 防刷功能放行OPTIONS请求，跨域时需要发送OPTIONS请求。
2. 和跨网使用相同配置`request.out.ips`支持多个域名访问当前项目。
3. 因接口冲突修改名称根据字段查询用户和机构 queryUsers()->queryUsersByField()、queryUnits()->queryUnitsByField()
4. 本部的重大升级是修复漏洞，升级方式不支持替换jar包方式，将业务的代码复制到完成的开发包中调试
5. 漏洞修复后剩余的漏洞记录看document目录下的漏洞备案文档
6. 去除tomcat的安装包，去除redis相关的依赖及封装，如需使用业务自行根据业务情况封装
7. 公司推荐使用postgresql，修改漏洞时将mysql依赖去掉业务如需使用自行引入
8. 升级Apollo客户端到2.1.0版本

### BDP-Nuts-4.2.3 -------------- 2023-03-06
1. 解决登录回来后跳转最后一个请求的问题
2. 升级`bdp.openapi.client-4.5.5`支持内部https请求
3. 升级`bdp-common.js`使用注意事项

### BDP-Nuts-4.2.2 -------------- 2023-02-14
1. 解决设置特殊字符到`cookie`中报错问题
2. 升级`bdp.openapi.client-4.5.4`和`bdp-static-4.5.4`
3. 新增设置`Cookie`相关的配置属性
```properties
   # 开启后端设置Cookie前端js无法使用document.cookie访问 默认：false-关闭 true-开启
   nuts.cookie.http-only=true
   # cookie的过期时间 默认：-1 永久
   nuts.cookie.max-age=-1
   # 开启后只能https请求携带Cookie 默认：false-关闭 true-开启
   nuts.cookie.secure=false
```
4. 升级公司前端`UI`组件`fh-ui`至`2.4.2`版本
5. 将`md5.js`、`sm3.js`、`uuid.js`逻辑全部移植到`bdp-common.js`中减少业务对js文件的引用
6. 简化业务对接BDP认证，跳转登录处理token操作在`bdp-common.js`中完成。
   需要注意的是`bdp-common.js`要在项目初始化文件和`JQuery`文件之后加载。
7. 完善`BDP_Nuts脚手架开发手册.doc`开发文档
8. `webapp/assets/demo/index.html`提供简单的对接登录示例根据业务情况修改。

### BDP-Nuts-4.2.1 -------------- 2022-12-06
1. 升级bdp.openapi.client-4.5.3版本使用授权码换取token信息失败时异常改为警告
2. GlobalSession提供设置和获取多个全局属性方法（需要对接4.4.11及以上版本BDP）

### BDP-Nuts-4.2.0 -------------- 2022-11-16
1. 新增`apollo.namespaces`配置业务可以指定自己的命名空间，多个使用逗号分隔，不配置默认命名空间为`fiberhome.common`
2. 修复在Apollo配置中心配置的值中有等号获取失败问题
3. bdp-static 升级至4.5.2版本
4. 新增token认证成的回调，继承`LoginSuccessCallback`方式重新`whenLoginSuccess`方法（使用bdp4.x必须开启authCode模式，默认都是开）

### BDP-Nuts-4.1.17 -------------- 2022-10-17
1. 修复桌面系统快速退出登录然后刷新业务500问题
2. 新增NutsUrlArgsFilter去除bdp-login登录成功回来后携带的参数

### BDP-Nuts-4.1.16 -------------- 2022-10-09
1. GlobeSession提供设置`putAllAttribute()`和获取多个属性的方法`getAllAttribute()`
2. bdp-openapi-client 升级4.5.2版本
3. 当前用户获取不到时清除security的上下文，防止出现旧用户去做鉴权操作
4. 当前用户获取不到时清除token缓存信息

### BDP-Nuts-4.1.15 -------------- 2022-09-20
1. 新增以下请求头支持配置，后面是对应的配置项名称
2. Access-Control-Allow-Origin -> nuts.access-control-allow-origin
3. X-FRAME-OPTIONS -> nuts.x-frame-options
4. Access-Control-Allow-methods -> nuts.access-control-allow-methods
5. Access-Control-Allow-headers -> nuts.access-control-allow-headers
6. Access-Control-Allow-credentials -> nuts.access-control-allow-credentials
7. bdp-openapi-client 升级4.5.1版本

### BDP-Nuts-4.1.14 -------------- 2022-09-06
1. 完善jar包依赖问题

### BDP-Nuts-4.1.13 -------------- 2022-09-01
1. 新增配置项是否开启校验appToken，nuts.check-app-token-enable=false默认关闭不校验
2. 优化功能url中协带相同的auth_code只请求一次服务端
3. 修改找不到poi相关jar包问题

### BDP-Nuts-4.1.11 -------------- 2022-08-30
1. 添加鉴权只校验刷入的菜单控制

### BDP-Nuts-4.1.10 -------------- 2022-08-04
1. 去除fastjson依赖改为使用Jackson新增jackson-datatype-jsr310.jar
2. 优化接口文档，去除fastjson，token相关接口有所变动
3. 去除centit-util工具包依赖
4. 升级bdp-static-4.5.0，升级bdp-openapi-client-4.5.0版本
5. 新增配置 Access-Control-Allow-headers 头信息可以通过 nuts.access-control-allow-headers 在配置文件中配置使用逗号分隔多个
6. 更新Tomcat包，Tomcat包中自带jdk业务无需单独安装
7. 将base-runtime-package依赖包打入开发包
8. 修改log4j.xml直接分割catalina.out日志文件，发布时需要将<appender-ref ref="Console"/>注释掉

### BDP-Nuts-4.1.9 -------------- 2022-07-06
1. log4j升级至2.17.1版本
2. bdp-openapi-client版本升级至4.4.1，bdp-static升级至4.4.3
3. 解决fastjson安全漏洞问题版本升级至1.2.83
4. 去除bdp-fql的依赖
5. 新增基于url配置sla规则的服务降级功能
6. 新增内外网跨网访问功能。

### BDP-Nuts-4.1.8 -------------- 2021-09-15
1. 将部标默认的应用令牌的请求头中的键名称RZZX-TOKEN配置到配置文件中
2. 去除旧版login-cas不配置时报空的日志提示
3. 部标默认的用户和应用的令牌键名称配置到阿波罗中
4. 修改maven开发包中找不到工具类问题

### BDP-Nuts-4.1.7 -------------- 2021-08-23
1. 适配对接bdp3.x版本
2. 优化token缓存移除机制
3. 更改token重复校验间隔10秒改为15秒支持可配置
4. 对接bdp3.x从脚手架退出暂时有问题可以从3.x版本的脚手架退出

### BDP-Nuts-4.1.6 -------------- 2021-07-15
1. 登录新增授权码换取token功能
2. 修复ajax跳转refer获取不到报错问题
3. 修复mq里适配globalsession问题

### BDP-Nuts-4.1.5 -------------- 2021-06-25
1. 简化pom依赖配置，解决jar包重复和依赖冲突问题版本有如下变动
   - `spring-data-commons.version` 1.12.10.RELEASE -> 1.13.8.RELEASE
   - `spring-oxm` 4.2.9.RELEASE -> 4.3.12.RELEASE
   - `hibernate-validator` 6.0.7.Final -> 6.0.13.Final
   - `log4j.version` 2.9.1 -> 2.11.1
   - `druid` 1.0.31 -> 1.2.1
   - `httpclient、httpmime` 4.5.3 -> 4.5.5
   - `httpcore` 4.4.6 -> 4.4.9
   - `commons-logging` 1.1.1 -> 1.2
   - `jboss-logging` 3.3.0 -> 3.3.2
   - `jcl-over-slf4` 1.7.13 -> 1.7.25
   - `slf4j-api` 1.7.7 -> 1.7.25
2. 优化readme.md文档便于阅读
3. 升级bdp.openapi.client到4.1.5 解决获取用户个别环境加解密报错问题
4. 修复推送日志接口

### BDP-Nuts-4.1.4 -------------- 2021-06-10
1. 修改或略请求获取不到用户信息报错问题
2. 优化WebOptUtils.getLoginUser()多次调用多次请求问题

### BDP-Nuts-4.1.3 -------------- 2021-06-08
1. 版本升级到4.1.3和bdp版本保持一致，作为4.x版本的第一个稳定版本
2. 优化请求过多导致慢的问题
3. token校验返回用户信息，同一个用户token在10秒内不做重复校验
4. 退出是先清理token的所有缓存
5. 忽略的请求如果是静态资源不绑定用户token信息
6. 去除Redis、activeMq、tomcat_Htpps版本安装包

### BDP-Nuts-4.1.0 -------------- 2021-05-28
1. WebOptUtils类型增加当前用户GlobalSession、ip、mac的获取方式
2. 删除无用的类

### BDP-Nuts-4.0.5 -------------- 2021-05-18 
1. 需要鉴权的URL去掉请求里问号后面的参数

### BDP-Nuts-4.0.3 -------------- 2021-05-12
1. 升级内部bdp客户端包
2. 删除对接第三方的开关配置

### BDP-Nuts-4.0.1 -------------- 2021-04-30
1. 吉林对接时发现吉林日志用户ID为long时转换问题，新增接口适配
2. 不开启第三方时不适应配置的token的键名称

### BDP-Nuts-4.0.0 -------------- 2021-04-27
1. 对接第三方传参增加appToken而且支持键名称配置
2. 去除cas登录 使用token机制
3. 将PlatformApi接口完善
4. 请求退出时跟参数returnUrl可配置退出后跳转的URL
5. 添加防刷机制，前提不能关闭原有的security开关

### BDP-Nuts-3.3.5 -------------- 2021-01-14
1. 升级bdp-static、bdp-openapi-client 3.4.2 版本jar包
2. 优化修复已知bug

### BDP-Nuts-3.3.4 -------------- 2020-09-14
1. 新增接口根据用户、角色、父菜单 查询有权限的子菜单
2. 获取用户的某些角色拥有的按钮权限
3. 升级bdp-static、bdp-openapi-client 3.4.1 版本jar包
4. 升级 fastjson 到 1.2.73 版本jar包
5. 去除lombok依赖

### BDP-Nuts-3.3.3 -------------- 2020-08-18
1. 去除ignoreUrl获取当前用户的过滤器，集成到DelegatingFilterProxy里，globe语法不匹配的问题
2. 添加session统一管理拦截器

### BDP-Nuts-3.3.2 -------------- 2020-07-29
1. 增加CryptoUtil加解密类，给当前用户的userPwd字段设置用户标识
2. 解决重复刷apptoken过期的问题

### BDP-Nuts-3.3.0 -------------- 2020-07-09
1. 新增文件前端文件校验默认关闭
2. 新增redis支持默认关闭
3. 脚手架新增对外接口，详细见document目录下的接口文档
4. 修改配置了ignore.url里的请求在登陆的情况下也可以获取当前登陆信息

### BDP-Nuts-3.2.4 -------------- 2020-06-09
1. maven-resources-plugin版本改为2.7适配低版本的开发工具本地打包 

### BDP-Nuts-3.2.3 -------------- 2020-05-26
1. cookie增加非空判断

### BDP-Nuts-3.2.2 -------------- 2020-05-19
1. ReDefinedCasEntryPoint支持从system.properties中读取token自定义名称
2. ReDefinedCasEntryPoint支持从cookie读取token自定义名称

### BDP-Nuts-3.2.1 -------------- 2020-05-07
1. 升级Tomcat至8.0.53版本
2. 完善文档以及刷菜单配置
3. maven格式的开发包支持打包
4. 角色的接口新增根据roleKey角色编码查询数据
5. 修改不填写请求类型默认刷get请求方式

### BDP-Nuts-3.2.0 -------------- 2020-04-08
1. 修复Tomcat关于AJP的bug
2. 百万数据除全量脚手架接口优化
3. 特别注意 使用客户端jar包 bdp-openapi-client-3.2.0.jar版本的脚手架
   要对接优化过百万数据接口的 bdp-openapi-server 对应的bdp界面版本3.2.0及以后版本
4. 新增用户和机构分页查询接口以后慢慢替代用户和机构的全量接口
5. 新增根据用户ID集合、机构ID集合查询用户和机构信息接口

### BDP-Nuts-3.1.1 -------------- 2020-01-06
1. 增加默认ignore-urls配置项，解决swagger页面无法访问的问题
2. 请求头增加Access-Control-Allow-credentials，解决跨域问题
3. 增加swagger demo，见CurdController类
4. 调用修改密码接口时，脚手架3.1.1及以后版本需要配合，
   bdp-openapi-client-3.1.1.jar、bdp-static-3.1.1.jar、bdp-3.1.5及以后版本
5. globe.common.conf和install.sh文件中3.x中新加的配置app.id和local.citycode

### BDP-Nuts-3.1.0 -------------- 2019-11-29
1. guava19升级到guava20，解决swagger启动报错问题
2. 升级fql jar包，从bdp-fql-1.0.0.jar升级到bdp-fql-1.0.2.jar,  
   nuts-spring-context.xml 14行增加com.bdp.fql的包扫描，
   nuts-spring-mvc.xml 13行增加com.bdp.fql的包扫描
3. Tomcat新增支持https版. 
4. 新增根据字典类型获取字典list的接口
5. 新增获取职务字典的接口
6. 刷菜单时增强对Button的OptUrl判断，有非法的直接抛异常不让刷 
7. 新增根据unitCode获取当前机构下用户
8. 更新bdp-openapi-client-3.10.jar、bdp-static-3.1.0.jar、cas-client-core-3.5.1.mod.jar
   修改了一些bug,增加了https的请求和登录
9. 刷菜单时menu没有配置isInToolbar时默认给Y，button不配置optReq默认给CRUD

### BDP-Nuts-3.0.4 -------------- 2019-09-28
1. 解决报错PlatformAPI初始化失败的问题

### BDP-Nuts-3.0.3 -------------- 2019-09-19
1. FQL增加非空查询语法
2. 解决请求路径后添加".xxx"时，本来没权限的请求也可以访问的问题

### BDP-Nuts-3.0.2 -------------- 2019-09-15
1. 适配吉大的授权中心时，因无法获取全量菜单数据，导致LogAPI在推送日志前检查数据时，报模块名不存在的问题

### BDP-Nuts-3.0.1 -------------- 2019-09-09（需强制升级到该版本，解决现场联调问题）
1. 增加通用查询API，支持现场对OPENAPI的定制查询功能
2. 替换最新GDK的jar包V3.3.1，详见http://gdk.njsecnet.com/gdk-db3/index.html
3. 解决吉大正元联调过程中，用户token有多个空格的问题

### BDP-Nuts-3.0.0 -------------- 2019-08-15 
Changelog：
1. 增加BDP-FQL组件，改变常规web端开发思路，节省你的开发时间，让你早点下班；
2. 生命周期中增加了usertoken及apptoken，方便与第三方认证，鉴权对接
3. 增加API接口层，更加灵活适配；
4. 对接BDP-3.0版本,脚手架请求BDP-OpenApi服务时请求头上增加了token的校验；
5. 解决脚手架权限数据缓存问题，由客户端缓存修改为服务端缓存；
6. menu.xml 增加 ischecked 配置，标识将该按钮或操作默认赋给已经有此菜单权限的角色；
7. 刷菜单时，-menu.xml文件不再自动备份，支持重复刷菜单，通过system.properties中的开关控制是否刷菜单
8. 增加源码包、javadoc包
9. 优化脚手架开发包打包脚本，简化打包流程，打包效率大幅提升
10. 替换apollo-client-1.0.0-Devops-Fh.jar,解决从同一配置获取中文乱码问题

详细使用文档请参见 document/devDoc/index.html 查看。

## V1.x -> V3.x 升级
### 准备工作：
1、准备好BDP3.0的环境，3.x的脚手架必须依赖3.0的BDP
2、升级前，先将所有的代码提交至gitlab或svn。以便检查升级过程中改动的文件
3、建议新建一个工程来进行升级，以免升级过程中遇到问题时无法及时对现有版本打包
升级步骤：
1、删除部分原有的jar包：nuts-\*.jar，bdp-\*.jar，centit-\*.jar，framework-\*.jar
2、将脚手架开发包解压，覆盖到已有的文件上,覆盖！与gitlab或svn上的代码对比，看是否影响业务原有的配置项
3、修改system.properties中的配置，与gitlab或svn对比，恢复业务自己加上的配置项