package org.seckill.exception;

/**
 * 重复秒杀异常（运行期异常）
 *      Spring只支持运行期异常的事务回滚
 * Created by Yan_Jiang on 2018/7/13.
 */
public class RepeatKillException extends SeckillException{

    public RepeatKillException(String message) {
        super(message);
    }

    public RepeatKillException(String message, Throwable cause) {
        super(message, cause);
    }
}
