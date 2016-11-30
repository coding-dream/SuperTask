package net.ruoxu.task;

import net.ruoxu.task.bean.Request;
import net.ruoxu.task.inter.Call;
import net.ruoxu.task.inter.CallBack;

/**
 * Created by Administrator on 2016/11/25.
 */
public class RealCall implements Call {

    private SuperClient mSuperClient;
    private Request mRequest;

    public RealCall(SuperClient superClient, Request request) {
        this.mSuperClient = superClient;
        this.mRequest = request;
    }

    @Override
    public Request request() {
        return mRequest;
    }


    @Override
    public void enqueue(CallBack responseCallback) {
        mSuperClient.dispatcher().executeTask(this,responseCallback);
    }

    @Override
    public void cancel() {
        mRequest.task().cancel(true);
    }

    @Override
    public boolean isCanceled() {
        return mRequest.task().isCancelled();
    }

}
