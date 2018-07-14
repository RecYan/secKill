package org.seckill.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Created by Yan_Jiang on 2018/7/13.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
        "classpath:spring/spring-dao.xml",
        "classpath:spring/spring-service.xml"})
public class SeckillServiceTest {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SeckillService seckillService;

    @Test
    public void testGetSecKillList() throws Exception {

        List<Seckill> secKillList = seckillService.getSecKillList();
        //list={}表示占位符 将secKillList中的值插入进去
        logger.info("list={}", secKillList);

    }

    @Test
    public void testgetById() throws Exception {

        long seckillId = 1000;
        Seckill seckill = seckillService.getById(seckillId);
        logger.info("seckill={}",seckill);
    }

    //将下面两个测试代码集成完整逻辑，注意可以重复执行
    @Test
    public void testSeckillLogic() throws Exception {
        long seckillId = 1000;
        Exposer exposer = seckillService.exportSecKillUrl(seckillId);

        if (exposer.isExposed()) {
            logger.info("exposer={}", exposer);
            long userPhone = 19373658721L;
            String md5 = exposer.getMd5();
            try {
                SeckillExecution execution = seckillService.executeSecKill(seckillId, userPhone, md5);
                logger.info("result={}", execution);
            } catch (RepeatKillException e) {
                logger.error(e.getMessage());
            } catch (SeckillCloseException e) {
                logger.error(e.getMessage());
            }
        } else {
            //秒杀未开启
            logger.warn("exposer={}", exposer);
        }
    }


   /* @Test
    public void testExportSecKillUrl() throws Exception {

        long seckillId = 1000;
        Exposer exposer = seckillService.exportSecKillUrl(seckillId);
        logger.info("exposer={}",exposer);
        *//* Exposer{exposed=true,
                  md5='ecd72734a6019ef66ae22f4029bd3ea2',
                  secKillId=1000,
                  now=0,
                  start=0,
                  end=0}*//*
    }

    @Test
    public void testExecuteSecKill() throws Exception {

        long seckillId = 1000;
        long userPhone = 12377655721L;
        String md5 = "ecd72734a6019ef66ae22f4029bd3ea2";
        //处理其他两个业务允许的异常 不向上抛出
        try {
            SeckillExecution seckillExecution = seckillService.executeSecKill(seckillId, userPhone, md5);
            logger.info("seckillExecution={}",seckillExecution);
        } catch (RepeatKillException e) {
            logger.error(e.getMessage());
        } catch (SeckillCloseException e) {
            logger.error(e.getMessage());
        }
        *//*SeckillExecution{seckillId=1000,
                state=1, stateInfo='秒杀成功',
                successKilled=SuccessKilled{seckillId=1000,
                userPhone=12377655721,
                state=0
                createTime=Fri Jul 13 23:13:59 CST 2018}}*//*

    }*/
}