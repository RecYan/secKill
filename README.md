# SSM整合开发

>**java并发秒杀业务实战** <br/>

>7/12/2018  V_01

- 完成了数据库表的设计，建立了`secKill`：秒杀商品的表和`successKilled：`秒杀成功的表，其中`successKilled`用了了联合主键检验，
    以保证每一用户或注册的手机号，只能参与一次秒杀活动。<br/>
- 完成了数据表的映射、`Mybatis`和`Spring`的整合配置。<br/>
- 编写了`DAO`层相关的实现代码，并通过了对应的单元测试。<br/>
- 出现的问题：*已记录，最后补充*<br/>
- 总结：*已记录，最后补充*

> 7/13/2018  V_02

- 完了`service`层的编码，新建了枚举包，用于表述常量数据字段，规范了编码，易于后端封装`json`传值。设计`DTO`包，用于`service`层和`web`层数据交互。<br/>
- 定义一个`md5`的加密“盐值”和自定义拼接规则 ，用于混淆`MD5`加密过程。保证了`md5`秘钥不易被逆向破解。<br/>
- 实现了当用户对同一个商品进行二次秒杀时，提前秒杀或超时秒杀，系统作出对应的响应。<br/>
- `DTO`层设计了`Exposer`类，当秒杀开启时，才暴露秒杀接口的地址，没开启时，只输出系统的时间和秒杀时间，一定程度上规避了浏览器插件作弊风险。
    同时`SeckillExecution`类，用来封装秒杀执行后的结果。<br/>
- 配置了`spring-service.xml`文件，将`service`对象交由`spring`托管，使用了基于注解的声明式事务，实现对`executeSecKill`方法相关提交和回滚操作。<br/>
- 通过了`service`层对应的单元测试。<br/>
- 出现的问题：*已记录，最后补充*<br/>
- 总结：*已记录，最后补充*

> 7/15/2018  V_03

- 完成前端交互设计过程，`流程图`参见博客。<br/>
- 秒杀接口的`api`使用了Restful风格。<br/>

    | 提交方式 |                url                   | 具体    |
    |:-: | :-: | :-: |
    |    GET  | /seckill/list                        |  秒杀列表|
    |    GET  | /seckill/{seckillId}/detail          |  秒杀页  |
    |    GET  | /seckill/time/now | 234              |  系统时间|
    |   POST  | /seckill/{seckillId}/exposer         | 暴露秒杀  |
    |   POST  | /seckill/{seckillId}/{md5}/execution | 执行秒杀  |
<br/>

- 完成了`springMVC`的整合，使用注解方式映射驱动，使用`DTO`层的`seckillResult`实体，将所有的ajax请求返回类型，全部封装成json数据，与前端交互。<br/>
- 前端页面使用`jQuery&&plugin`、`Bootstrap`技术完成。

> 7/15/2018  V_04

+ 对项目中的并发点，进行了分析优化
	[1] 详情页暴露秒杀按钮，且采取了按钮防重复机制，减轻请求重复提交的压力。
	[2] 采用了Redis缓存服务器，进一步减轻后台压力，同时采用了超时穿透<本处缓存1小时>来维护一致性,请求路径：请求-->Redis-->mysql
	[3] 对于sql操作处理，在原有sql基础上编写了`seckill.sql`存储过程,减少了等待行级锁时间，整个sql操作过程都放到mysql服务端进行。
	[4] 分离了动静态数据，静态页面-->CDN缓存，后端缓存-->Redis
	[5] 整个项目通过了完整测试。

+ 详细业务逻辑、优化分析，参见博客。
+ 最终项目效果图如图所示：
 **列表页**<br/>
![](https://i.imgur.com/pemkcqo.jpg)
**详情页**  `采用cookie值+md5值作为登录凭证`<br/>
![](https://i.imgur.com/htIKrwH.jpg)
![](https://i.imgur.com/n8QXM4P.jpg)
**秒杀结果**<br/>
![](https://i.imgur.com/U6GDqMr.jpg)
![](https://i.imgur.com/LrccpQx.jpg)
**数据表**<br/>
![](https://i.imgur.com/Um3npPI.jpg)<br/>
**Redis**<br/>
![](https://i.imgur.com/tsmv5nn.jpg)
**测试**<br/>
![](https://i.imgur.com/4NxZpKX.jpg)
![](https://i.imgur.com/TJwjJVU.jpg)
![](https://i.imgur.com/XCwsQWX.jpg)
![](https://i.imgur.com/ZW1BLvY.jpg)
