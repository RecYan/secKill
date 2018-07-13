package org.seckill.dao;

import org.apache.ibatis.annotations.Param;
import org.seckill.entity.SuccessKilled;

/**
 * SuccessKilled实体相关的业务逻辑
 * Created by Yan_Jiang on 2018/7/12.
 */
public interface SuccessKilledDao {

    /**
     * 插入购买明细，可数据表中的联合主键 过滤重复用户
     * @param seckillId 秒杀商品的id
     * @param userPhone 注册的手机号
     * @return 返回插入结果集数量--行数
     */
    int insertSuccessKilled(@Param("seckillId") long seckillId, @Param("userPhone") long userPhone);

    /**
     * 根据id查询SuccessKilled实体 并携带Seckill对象实体
     * @param seckillId 秒杀商品的id
     * @param userPhone 注册的手机号
     * @return 返回秒杀成功对象实体并携带秒杀商品实体
     */
    SuccessKilled queryByIdWithSeckill(@Param("seckillId") long seckillId, @Param("userPhone") long userPhone);

}
