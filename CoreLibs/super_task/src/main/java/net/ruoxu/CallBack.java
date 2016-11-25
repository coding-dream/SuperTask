package net.ruoxu;

public interface CallBack {

    void before();//UI
    void doInBackgroud(); //线程执行

    void after();//UI
    void cancel();//UI
}
