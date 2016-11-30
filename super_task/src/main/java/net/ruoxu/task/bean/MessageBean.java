package net.ruoxu.task.bean;

import net.ruoxu.task.SuperTask;

/**
 * Created by wangli on 16/11/24.
 */
public class MessageBean {
     public SuperTask mTask;
     public Response response;
    // TODO: other params canbe here

    public MessageBean(SuperTask resultSuperTask, Response response) {
        this.mTask = resultSuperTask;
        this.response = response;
    }
}
