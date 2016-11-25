package net.ruoxu;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import net.ruoxu.bean.MessageBean;

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
                result.mTask.finish();// 线程结束
                break;
            case MESSAGE_POST_PROGRESS:
            
                break; // TODO: 进度回调
            


        }

    }
}
