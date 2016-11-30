package net.ruoxu.task.bean;

/**
 * Created by Administrator on 2016/11/25.
 */
public class Response{

    private Object t;

    public void params(Object object){
        this.t = object;
    }

    public <T> T params(Class<T> clazz){
        return (T) t;
    }


}
