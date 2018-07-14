package org.seckill.service.ServiceImpl;

import org.seckill.dao.SeckillDao;
import org.seckill.dao.SuccessKilledDao;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.entity.SuccessKilled;
import org.seckill.enums.SeckillStatEnum;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;
import org.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;


/**
 * SeckillService接口的实现类
 * Created by Yan_Jiang on 2018/7/13.
 */
@Service
public class SeckillServiceImpl implements SeckillService {

    //bug1: 应统一slf4j日志api 导包需要注意
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Mybatis与spring整合之后，所有的Dao对象都会通过mapper方式初始化好，并放入spring容器中
     * 所以我们只需要对其进行依赖注入即可
     * 依赖注入：@Autowired、@Resource
    */
    @Autowired
    private SeckillDao seckillDao;

    @Autowired
    private SuccessKilledDao successKilledDao;

    //定义一个混淆的“盐值” 用于混淆MD5 ==> 防止用户根据md5逆向破解 ,尽量复杂
    private final String slat = "s~`d-=!dwqfGJHG@UY45faada?}{&%";


    public List<Seckill> getSecKillList() {

        return  seckillDao.queryAll(0,4);
    }

    public Seckill getById(long seckillId) {
        return seckillDao.queryById(seckillId);
    }

    public Exposer exportSecKillUrl(long seckillId) {

        //根据id查询秒杀商品
        Seckill seckill = seckillDao.queryById(seckillId);
        //秒杀商品记录为空
        if (seckill == null) {
            return new Exposer(false, seckillId);
        }

        //秒杀开始时间
        Date startTime = seckill.getStartTime();
        Date endTime = seckill.getEndTime();
        //系统当前时间
        Date nowTime = new Date();
        if (nowTime.getTime() < startTime.getTime()
                || nowTime.getTime() > endTime.getTime()) {
            return new Exposer(false, seckillId, nowTime.getTime(),
                    startTime.getTime(), endTime.getTime());
        }

        String md5 = getMD5(seckillId);
        //秒杀成功
        return new Exposer(true, md5, seckillId);
    }

    //md5加密过程 自定义拼接规则和“盐值”扩充
    private String getMD5 (long seckillId) {
        String base = seckillId + "/" +slat;
        //使用spring的util类来进行md5加密
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }

    /**
     * 使用注解控制方法的优点：
     *  1.开发团队约定明确标注事务方法的风格；
     *  2.保证事务方法的执行时间尽可能短，不要穿插其他的网络操作:RPC/HTTP请求、或剥离到事务方法外部；
     *  3.不是所有的方法都需要事务，如果只有一条修改操作，只读操作不需要事务控制。
     */
    @Transactional
    public SeckillExecution executeSecKill(long seckillId, long userPhone, String md5)
            throws SeckillException, RepeatKillException, SeckillCloseException {
        //md5为空 或者 与之前注册生成的md5值不匹配
        if (md5 == null || !md5.equals(getMD5(seckillId))) {
            throw new SeckillException("seckill data are rewrited");
        }
        //执行秒杀逻辑：减库存+记录购买行为
        //1 记录当前时间为创建的秒杀时间
        Date nowTime = new Date();
        try {
            //减库存
            int updateCount = seckillDao.reduceNumber(seckillId, nowTime);
            //2 判断更新的记录
            if (updateCount <= 0) {
                //2.1 没有更新记录，秒杀结束
                throw new SeckillCloseException("seckill is closed");
            } else {
                //2.2 有更新记录，记录购买行为
                int insertCount = successKilledDao.insertSuccessKilled(seckillId, userPhone);
                //2.2.1 秒杀验证
                if (insertCount <= 0) {
                    throw new RepeatKillException("seckill is repeated");
                } else {
                    //2.2.2 秒杀成功  ==> 传入枚举类型,
                    SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId, userPhone);
                    return new SeckillExecution(seckillId, SeckillStatEnum.SUCCESS, successKilled);
                }
            }
        } catch (SeckillCloseException seckillCloseException) {
            throw seckillCloseException;
        } catch (RepeatKillException repeatKillException) {
            throw repeatKillException;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            //将所有的编译期异常转换为运行期异常 ==> 方便spring执行rollback
            throw new SeckillException("seckill inner error: " + e.getMessage());
        }
    }
}
