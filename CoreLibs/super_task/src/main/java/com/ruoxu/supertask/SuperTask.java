package com.ruoxu.supertask;

import android.os.Binder;
import android.os.Handler;
import android.os.Process;
import android.util.Log;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by wangli on 16/11/24.
 */
public class SuperTask<Result> {

    private final String TAG = getClass().getSimpleName();


    private static final int MESSAGE_POST_RESULT = 0x1;
    private static final int MESSAGE_POST_PROGRESS = 0x2;


    private static Handler sHandler;


    private final AtomicBoolean mCancelled = new AtomicBoolean(); //是否取消了任务
    private final AtomicBoolean mTaskInvoked = new AtomicBoolean(); //任务是否被成功调用

    
    private volatile Status mStatus = Status.PENDING;

    public SuperTask(){

    }







    private static Handler getHandler() {
        synchronized (SuperTask.class) {
            if (sHandler == null) {
                sHandler = new ToggleHandler();
            }
            return sHandler;
        }
    }



}
