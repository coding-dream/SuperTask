package net.ruoxu.task.bean;

import net.ruoxu.task.SuperTask;

/**
 * Created by Administrator on 2016/11/25.
 */
public class Request {

    private SuperTask task;
    private Object param;

    public void params(Object t) {
        this.param = t;
    }


    public <T> T params(Class<T> clazz){
        return (T) param;
    }





    public Request(Builder builder) {
        this.task = builder.task;
        this.param = builder.params;

    }

    public SuperTask task(){
        return task;
    }


    public static final class Builder{
        private SuperTask task ;
        private Object  params;

        public Builder task(SuperTask superTask) {
            task = superTask;
            return this;
        }

        public Builder params(Object b){
            params = b;
            return this;
        }



        public Request build(){
            return new Request(this);
        }

    }


}
