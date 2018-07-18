package org.seckill.dao.cache;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.dao.SeckillDao;
import org.seckill.entity.Seckill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;



/**
 * Created by Yan_Jiang on 2018/7/17.
 *
 */
//junit启动时加载springIOC容器
@RunWith(SpringJUnit4ClassRunner.class)
//告诉junit spring配置文件
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class RedisDaoTest {

    private long id = 1000;

    @Autowired
    private RedisDao redisDao;

    @Autowired
    private SeckillDao seckillDao;

    @Test
    public void testSeckill() throws Exception {
        //测试get、put方法
        //redis中去取seckill对象 --get
        Seckill seckill = redisDao.getSeckill(id); //初始为空
        //判断是否为空
        if(seckill == null) {
             seckill = seckillDao.queryById(id);
            if(seckill != null) {
                //put
                String result = redisDao.putSeckill(seckill);
                System.out.println("result: "+result);
                seckill = redisDao.getSeckill(id);
                System.out.println("seckill: "+seckill);
            }
        }
    }


}