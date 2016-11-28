package net.ruoxu;

import net.ruoxu.bean.Request;
import net.ruoxu.inter.Call;

/**
 * Created by Administrator on 2016/11/25.
 */
public class SuperClient implements Call.Factory {



    @Override
    public Call newCall(Request request) {
        return new RealCall(this,request);
    }


}
