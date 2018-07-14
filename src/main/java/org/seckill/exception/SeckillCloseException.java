package org.seckill.exception;

/**
 * 秒杀关闭异常
 * Created by Yan_Jiang on 2018/7/13.
 */
public class SeckillCloseException extends SeckillException {

    public SeckillCloseException(String message) {
        super(message);
    }

    public SeckillCloseException(String message, Throwable cause) {
        super(message, cause);
    }
}
