package com.ruoxu.supertask;

import android.os.Handler;
import android.os.Message;

import com.ruoxu.supertask.bean.MessageBean;

/**
 * Created by wangli on 16/11/24.
 */
public class ToggleHandler extends Handler {

    private static final int MESSAGE_POST_RESULT = 0x1;
    private static final int MESSAGE_POST_PROGRESS = 0x2;


    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);

        MessageBean result = (MessageBean) msg.obj;
        switch (msg.what) {
            case MESSAGE_POST_RESULT:
                // There is only one result
                result.mTask.finish(result);// 线程结束
                break;


        }

    }
}
