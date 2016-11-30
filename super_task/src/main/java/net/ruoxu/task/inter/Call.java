package net.ruoxu.task.inter;

import net.ruoxu.task.bean.Request;

/**
 * Created by Administrator on 2016/11/25.
 */
public interface Call {
    Request request();
    void enqueue(CallBack responseCallback);
    void cancel();
    boolean isCanceled();

    interface Factory {
        Call newCall(Request request);
    }
}
