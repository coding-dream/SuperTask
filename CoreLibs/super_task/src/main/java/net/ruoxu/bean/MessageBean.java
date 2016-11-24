package net.ruoxu.bean;

import net.ruoxu.SuperTask;

/**
 * Created by wangli on 16/11/24.
 */
public class MessageBean {
     public SuperTask mTask;
    // TODO: other params canbe here

    public MessageBean(SuperTask resultSuperTask) {
        this.mTask = resultSuperTask;
    }
}
