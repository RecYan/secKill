package org.seckill.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.entity.Seckill;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import javax.annotation.Resource;
import java.util.Date;
import java.util.List;


/**
 * 配置spring和junit整合 ， junit启动时加载springIOC容器
 * Created by Yan_Jiang on 2018/7/12.
 */
//junit启动时加载springIOC容器
@RunWith(SpringJUnit4ClassRunner.class)
//告诉junit spring配置文件
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SeckillDaoTest {

    //注入Dao实现类依赖
    @Resource
    private SeckillDao seckillDao;

    @Test
    public void testQueryById() throws Exception {

        long id = 1000;
        //**bug1 seckillDao.testQueryById() 写错了
        Seckill seckill = seckillDao.queryById(id);
        System.out.println(seckill.getName());
        System.out.println(seckill);
        /**
         * 输出结果
         * 1000元秒杀iPhoneX
         Seckill{seckillId=1000,
         name='1000元秒杀iPhoneX',
         number=100,
         createTime=Thu Jul 12 15:07:17 CST 2018,
         startTime=Thu Jul 12 00:00:00 CST 2018,
         endTime=Fri Jul 13 00:00:00 CST 2018}
         */
    }

    @Test
    public void testQueryAll() throws Exception {

        /** Caused by: org.apache.ibatis.binding.BindingException: Parameter 'offset' not found.
         * java没有保存行参的记录，会把List<Seckill> queryAll(int offset,int limit);中的参数变成这样:queryAll(int arg0,int arg1),
         * 即 List<Seckill> queryAll(int offset,int limit) --> queryAll(int arg0,int arg1)
         * 这样我们就没有办法去传递多个参数。
         * &&& 解决方法：@Param("offset") 来说明具体对应的参数 &&&&
         */
        List<Seckill> seckills = seckillDao.queryAll(0, 100);
        for(Seckill seckill : seckills) {
            System.out.println(seckill);
        }
    }

    @Test
    public void testReduceNumber() throws Exception {

        Date killTime = new Date();
        int updateCount = seckillDao.reduceNumber(1000L, killTime);
        System.out.println("updateCount="+updateCount);

    }

}