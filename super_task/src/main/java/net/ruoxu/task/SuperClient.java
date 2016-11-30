package net.ruoxu.task;

import net.ruoxu.task.bean.Request;
import net.ruoxu.task.inter.Call;

/**
 * Created by Administrator on 2016/11/25.
 */
public class SuperClient implements Call.Factory {
    private static SuperClient instance;

    public static SuperClient getInstance(){
        if (instance == null) {
            synchronized (SuperClient.class) {
                if (instance == null) {
                    instance = new SuperClient();
                }
            }
        }
        return instance;
    }

    @Override
    public Call newCall(Request request) {
        return new RealCall(this,request);
    }


    public Dispatcher dispatcher() {

        return Dispatcher.getInstance();

    }
}
