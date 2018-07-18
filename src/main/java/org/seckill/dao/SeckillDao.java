package org.seckill.dao;

import org.apache.ibatis.annotations.Param;
import org.seckill.entity.Seckill;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Seckill对应的业务逻辑
 * Created by Yan_Jiang on 2018/7/12.
 */
public interface SeckillDao {
    /**
     *减库存
     * @param seckillId 商品的秒杀id
     * @param killTime 秒杀创建的时间
     * @return 如果影响的行数>1, 表示更新的记录行数
     */
    int reduceNumber(@Param("seckillId") long seckillId, @Param("killTime") Date killTime);

    /**
     * 根据秒杀id来查询对应的秒杀商品
     * @param seckillId 秒杀商品的id
     * @return 返回对应的秒杀商品实体
     */
    Seckill queryById(long seckillId);

    /**
     * 根据偏移量查询秒杀商品的列表
     * @param offset 起始偏移id号
     * @param limit  显示的条数记录
     * @return 返回查询的秒杀商品列表
     */
    //传递多个参数时 需要@Param("")来说明具体是哪一个形参
    List<Seckill> queryAll(@Param("offset") int offset, @Param("limit")int limit);

    /**
     * 使用存储过程执行秒杀
     * @param paramMap 参数
     */
    void killByprocedure(Map<String, Object> paramMap);

}
