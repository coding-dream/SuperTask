package com.ruoxu.supertask;

import android.os.Binder;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.util.Log;

import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by wangli on 16/11/24.
 */
public abstract class SuperTask<Result> {

    private final String TAG = getClass().getSimpleName();


    private static final int MESSAGE_POST_RESULT = 0x1;
    private static final int MESSAGE_POST_PROGRESS = 0x2;


    private static Handler sHandler;


    private final AtomicBoolean mCancelled = new AtomicBoolean(); //是否取消了任务
    private final AtomicBoolean mTaskInvoked = new AtomicBoolean(); //任务是否被成功调用


    private volatile Status mStatus = Status.PENDING; //默认状态，等待

    private final FutureTask<Result> mFuture;  //泛型Result是Callable创建的线程返回的结果

    public SuperTask(){
        mFuture = new FutureTask<Result>(new Callable<Result>() {
            @Override
            public Result call() throws Exception {

                mTaskInvoked.set(true);
                Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
                //noinspection unchecked
                Result result = doInBackgroud();
                Binder.flushPendingCommands();

                sendMsgToUIThread(result);

                return result;  //这个返回结果 仅用于 done()方法 mFuture.get()获取
            }
        }){
            @Override
            protected void done() {
                try {
                    boolean wasTaskInvoked = mTaskInvoked.get();
                    if (!wasTaskInvoked) {
                        sendMsgToUIThread(mFuture.get()); // TODO: =====>
                    }


                } catch (InterruptedException e) {
                    Log.d(TAG, "e:" + e);
                } catch (ExecutionException e) {

                    throw new RuntimeException("An error occurred while executing doInBackground()",e.getCause());

                } catch (CancellationException e) {

                    boolean wasTaskInvoked = mTaskInvoked.get();
                    if (!wasTaskInvoked) {
                        sendMsgToUIThread(null);
                    }

                }
            }
        };
    }

    private void sendMsgToUIThread(Result result) {
//        Message message = getHandler().obtainMessage(MESSAGE_POST_RESULT, new AsyncTaskResult<Result>(this, result));
//        message.sendToTarget();

    }

    public abstract Result doInBackgroud(); //交给子类实现




    private static Handler getHandler() {
        synchronized (SuperTask.class) {
            if (sHandler == null) {
                sHandler = new ToggleHandler();
            }
            return sHandler;
        }
    }



}
