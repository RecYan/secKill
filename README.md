# SSM整合开发
>java并发秒杀业务实战 <br/>

----
> 7/12/2017  V_01
----
[1] 完成了数据库表的设计，建立了`secKill`：秒杀商品的表和`successKilled：`秒杀成功的表，其中`successKilled`用了了联合主键检验，
    以保证每一用户或注册的手机号，只能参与一次秒杀活动。<br/>
[2] 完成了数据表的映射、`Mybatis`和`Spring`的整合配置。<br/>
[3] 编写了`DAO`层相关的实现代码，并通过了对应的单元测试。<br/>
[4] 出现的问题：*已记录，最后补充*<br/>
[5] 总结：*已记录，最后补充*


----
> 7/13/2017  V_02
----

[1] 完了`service`层的编码，新建了枚举包，用于表述常量数据字段，规范了编码，易于后端封装`json`传值。设计`DTO`包，用于`service`层和`web`层数据交互.<br/>
[2] 定义一个`md5`的加密“盐值”和自定义拼接规则 ，用于混淆`MD5`加密过程。保证了`md5`秘钥不易被逆向破解。<br/>
[3] 实现了当用户对同一个商品进行二次秒杀时，提前秒杀或超时秒杀，系统作出对应的响应。<br/>
[4] `DTO`层设计了`Exposer`类，当秒杀开启时，才暴露秒杀接口的地址，没开启时，只输出系统的时间和秒杀时间，一定程度上规避了浏览器插件作弊风险。
    同时`SeckillExecution`类，用来封装秒杀执行后的结果。<br/>
[5] 配置了`spring-service.xml`文件，将`service`对象交由`spring`托管，使用了基于注解的声明式事务，实现对`executeSecKill`方法相关提交和回滚操作。<br/>
[7] 通过了`service`层对应的单元测试。<br/>
[8] 出现的问题：*已记录，最后补充*<br/>
[9] 总结：*已记录，最后补充*

----
> 7/15/2017  V_03
----
[1]

