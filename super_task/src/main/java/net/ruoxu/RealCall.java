package net.ruoxu;

import net.ruoxu.bean.Request;
import net.ruoxu.inter.Call;
import net.ruoxu.inter.CallBack;

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
//        mSuperClient.dispatcher().executed(this); // TODO: 2016/11/26 todo
        
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
