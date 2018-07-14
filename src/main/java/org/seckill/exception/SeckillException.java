package org.seckill.exception;

/**
 * 所有的秒杀相关业务异常
 * Created by Yan_Jiang on 2018/7/13.
 */
public class SeckillException extends RuntimeException  {

    public SeckillException(String message) {
        super(message);
    }

    public SeckillException(String message, Throwable cause) {
        super(message, cause);
    }
}
