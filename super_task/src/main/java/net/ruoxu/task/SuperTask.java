package net.ruoxu.task;

import android.os.Binder;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.util.Log;

import net.ruoxu.task.bean.MessageBean;
import net.ruoxu.task.bean.Request;
import net.ruoxu.task.bean.Response;
import net.ruoxu.task.inter.CallBack;
import net.ruoxu.task.utils.ExecutorFactory;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by wangli on 16/11/24.
 */
public  class SuperTask {

    private final String TAG = getClass().getSimpleName();
    private static final int MESSAGE_POST_RESULT = 0x1;
    private static final int MESSAGE_POST_PROGRESS = 0x2;
    private static Handler sHandler;

    private final AtomicBoolean mCancelled = new AtomicBoolean();
    private final AtomicBoolean mTaskInvoked = new AtomicBoolean();

    private volatile Status mStatus = Status.PENDING;
    private final FutureTask<Void> mFuture;  //泛型Void是Callable创建的线程返回的结果,这里弃用

    private CallBack mCallBack;
    private Request mRequest;


    private static Handler getHandler() {
        synchronized (SuperTask.class) {
            if (sHandler == null) {
                sHandler = new ToggleHandler();
            }
            return sHandler;
        }
    }


    public static SuperTask create() {
        return new SuperTask();
    }

    public SuperTask(){
        this(null,null);
        Log.d(TAG, "create a new task");
    }

    public SuperTask(CallBack callBack,  Request request){

        if (callBack != null) {
            this.mCallBack = callBack;
        }
        if (request != null) {
            this.mRequest = request;
        }


        getHandler();//确保handler在UI线程创建，SuperTask必须在UI线程创建

        mFuture = new FutureTask<Void>(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                mTaskInvoked.set(true);
                Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);

                Response response = mCallBack.doInBackgroud(mRequest);

                Binder.flushPendingCommands();
                sendMsgToUIThread(response);
                return null;  //这个返回结果 仅用于 done()方法 mFuture.get()获取
            }
        }){
            @Override
            protected void done() {
                boolean wasTaskInvokedValue = mTaskInvoked.get();
                if (!wasTaskInvokedValue) { // mTaskInvoked.set(true);未调用即被cancel掉
                    sendMsgToUIThread(null);
                }

            }
        };
    }



    private void sendMsgToUIThread(Response response) {

        MessageBean messageBean = new MessageBean(this,response);
        Message message = getHandler().obtainMessage(MESSAGE_POST_RESULT, messageBean);
        message.sendToTarget();

    }

    public final SuperTask execute() {

        if (mStatus != Status.PENDING) {

            switch (mStatus) {
                case RUNNING:
                    throw new IllegalStateException("Cannot execute task: the task is already running.");
                case FINISHED:
                    throw new IllegalStateException("Cannot execute task: the task has already been executed (a task can be executed only once)");
            }
        }

        mStatus = Status.RUNNING;

        mCallBack.before();

        ExecutorFactory.defaultExecutor().execute(mFuture);

        return this;

    }

    public void finish(Response response) {
        //UI线程

        if (isCancelled()) {
            mCallBack.cancel();
        } else {
            mCallBack.after(response);
        }

        mStatus = Status.FINISHED;


    }

    public boolean isCancelled() {
        return mCancelled.get();
    }


    public final boolean cancel(boolean mayInterruptIfRunning) {
        mCancelled.set(true);  //设置标志位 取消Task
        return mFuture.cancel(mayInterruptIfRunning);
    }


    public void callback(CallBack responseCallback) {
        this.mCallBack = responseCallback;
    }
    public void request(Request request){
        this.mRequest = request;
    }


}
