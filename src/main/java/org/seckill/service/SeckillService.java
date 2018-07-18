package org.seckill.service;

import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;

import java.util.List;

/**
 * 业务接口设计：站在"使用者"角度设计接口
 *      方法定义粒度，参数[简单],返回类型[类型友好]
 *
 * Created by Yan_Jiang on 2018/7/13.
 */
public interface SeckillService {

    /**
     * 查询所有的秒杀商品列表
     * @return 秒杀商品列表
     */
    List<Seckill> getSecKillList();

    /**
     * 根据商品id查询对应的秒杀商品
     * @param seckillId 商品id
     * @return 商品id对应的秒杀商品
     */
    Seckill getById(long seckillId);

    /**
     * 秒杀开启时，才输出秒杀接口的地址
     * 没开启时，只输出系统的时间和秒杀时间
     *      ==> 预防浏览器插件作弊
     * @param seckillId 商品id
     * @return 返回DTO实体
     */
    Exposer exportSecKillUrl(long seckillId);

    /**
     * 执行秒杀操作
     * @param seckillId 商品id
     * @param userPhone 注册的手机号
     * @param md5 加密方式
     * @return 秒杀执行后的结果 且通过抛出不同的异常 提醒使用者
     * @throws SeckillException RepeatKillException SeckillCloseException
     */
    SeckillExecution executeSecKill(long seckillId, long userPhone, String md5) throws SeckillException,RepeatKillException,SeckillCloseException;

    /**
     * 执行秒杀操作by存储过程
     * @param seckillId 商品id
     * @param userPhone 注册的手机号
     * @param md5 加密方式
     * @return 秒杀执行后的结果 且通过抛出不同的异常 提醒使用者
     * @throws SeckillException RepeatKillException SeckillCloseException
     */
    SeckillExecution executeSecKillprocedure(long seckillId, long userPhone, String md5);
}
