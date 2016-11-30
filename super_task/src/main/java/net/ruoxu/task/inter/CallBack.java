package net.ruoxu.task.inter;

import net.ruoxu.task.bean.Request;
import net.ruoxu.task.bean.Response;

public interface CallBack {

    void before();//UI
    Response doInBackgroud(Request request); //线程执行

    void after(Response response);//UI
    void cancel();//UI
}
