package net.ruoxu;

import net.ruoxu.bean.Request;
import net.ruoxu.bean.Response;
import net.ruoxu.inter.Call;
import net.ruoxu.inter.CallBack;

/**
 * Created by Administrator on 2016/11/25.
 */
public class RealCall implements Call {

    @Override
    public Request request() {

        return null;
    }

    @Override
    public Response response() {

        return null;
    }

    @Override
    public void enqueue(CallBack responseCallback) {

    }

    @Override
    public void cancel() {

    }

    @Override
    public boolean isCanceled() {
        return false;
    }

}
