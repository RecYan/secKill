package org.seckill.dto;

/**
 * 将所有的ajax请求返回类型，全部封装成json数据
 * Created by Yan_Jiang on 2018/7/14.
 */
public class SeckillResult<T> {

    private boolean success;

    private T data;

    private String error;

    //成功 输出数据
    public SeckillResult(boolean flag, T data) {
        this.success = flag;
        this.data = data;
    }

    //错误 输出错误信息
    public SeckillResult(boolean flag, String error) {
        this.success = flag;
        this.error = error;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }


}
