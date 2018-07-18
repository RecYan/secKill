package org.seckill.dao.cache;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import org.seckill.entity.Seckill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Created by Yan_Jiang on 2018/7/17.
 */
public class RedisDao {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final JedisPool jedisPool;

    //3 创建pojo对象的schma模型
    private RuntimeSchema<Seckill> schema = RuntimeSchema.createFrom(Seckill.class);

    //1 初始化缓存池
    public RedisDao(String ip, int port) {
        jedisPool = new JedisPool(ip, port);
    }

    //从redis中拿去缓冲数组,并反序列化成对应的对象
    public Seckill getSeckill(long seckillId) {
        //redis操作逻辑
        try {
            //2 获取jedis对象
            Jedis jedis = jedisPool.getResource();
            try {
                String key = "seckill:" + seckillId;
                //seckill对象未实现内部序列化
                //redis: get ->byte[]  我们:反序列化 ->Object(seckill)
                //自定义序列化 protostuff: 序列化的对象必须是pojo: 即具有get、set方法的普通java类
                byte[] bytes = jedis.get(key.getBytes());
                //4 缓存中获取
                if (bytes != null) {
                    //5 创建空对象 并按照schma约束进行属性赋值
                    Seckill seckill = schema.newMessage();
                    //按照schma约束将bytes中对应的属性复制到seckill对象对应的属性中
                    ProtostuffIOUtil.mergeFrom(bytes, seckill, schema);
                    //6 seckill已经被反序列化
                    return seckill;
                }
            } finally {
                jedis.close();
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    //将对象传递到redis中
    public String putSeckill(Seckill seckill) {
        // set Object(seckill) -> 序列化 ->byte[] ->发送给redis
        try {
            //1 获取jedis对象
            Jedis jedis = jedisPool.getResource();
            try {
                String key = "seckill:" + seckill.getSeckillId();
                /**2 对象转为二进制数组
                 * LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE) : 分配一个默认大小的缓存器
                 */
                byte[] bytes = ProtostuffIOUtil.toByteArray(seckill, schema,
                        LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
                //设置jedis超时缓存
                int timeout = 60 * 60; //一小时
                String result = jedis.setex(key.getBytes(), timeout, bytes);
                //正确返回：ok 错误返回: 错误信息
                return result;
            } finally {
                jedis.close();
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

}
