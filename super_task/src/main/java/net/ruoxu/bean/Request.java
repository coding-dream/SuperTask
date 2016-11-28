package net.ruoxu.bean;

import net.ruoxu.SuperTask;

/**
 * Created by Administrator on 2016/11/25.
 */
public class Request {
    private SuperTask task;

    public Request(Builder builder) {
        this.task = builder.task;
    }


    public SuperTask task(){
        return task;
    }


    public static final class Builder{
        private SuperTask task ;
        public Builder task(SuperTask superTask) {
            task = superTask;
            return this;
        }

        public Request build(){
            return new Request(this);
        }

    }


}
