package net.ruoxu.task;

import net.ruoxu.task.bean.Request;
import net.ruoxu.task.inter.Call;
import net.ruoxu.task.inter.CallBack;

/**
 * Created by Administrator on 2016/11/25.
 */
public class Dispatcher {

    private static Dispatcher instance;

    public static Dispatcher getInstance() {
        if (instance == null) {
            synchronized (Dispatcher.class) {
                if (instance == null) {
                    instance = new Dispatcher();
                }
            }
        }

        return instance;
    }



    public void executeTask(Call realCall, CallBack responseCallback){
        // 调用SuperTask执行
        Request request = realCall.request();
        SuperTask superTask = request.task();

        superTask.callback(responseCallback);
        superTask.request(request);

        superTask.execute();
    }

}
