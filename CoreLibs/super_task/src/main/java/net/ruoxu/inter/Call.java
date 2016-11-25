package net.ruoxu.inter;

import net.ruoxu.bean.Request;
import net.ruoxu.bean.Response;

import java.io.IOException;

/**
 * Created by Administrator on 2016/11/25.
 */
public interface Call {
    Request request();
    Response response();
    void enqueue(CallBack responseCallback);
    void cancel();
    boolean isCanceled();

    interface Factory {
        Call newCall(Request request);
    }
}
