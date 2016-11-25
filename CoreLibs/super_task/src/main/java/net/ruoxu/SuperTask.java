package net.ruoxu;

import android.os.Binder;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.util.Log;

import net.ruoxu.bean.MessageBean;
import net.ruoxu.utils.ExecutorUtils;

import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by wangli on 16/11/24.
 */
public  class SuperTask<Result> {

    private final String TAG = getClass().getSimpleName();


    private static final int MESSAGE_POST_RESULT = 0x1;
    private static final int MESSAGE_POST_PROGRESS = 0x2;


    private static Handler sHandler;


    private final AtomicBoolean mCancelled = new AtomicBoolean(); //是否取消了任务
    private final AtomicBoolean mTaskInvoked = new AtomicBoolean(); //任务是否被成功调用


    private volatile Status mStatus = Status.PENDING; //默认状态，等待

    private final FutureTask<Result> mFuture;  //泛型Result是Callable创建的线程返回的结果


    private CallBack mCallBack;



    public interface CallBack<T> {

        void before();//UI
        T doInBackgroud(); //线程执行
        void after();//UI

        void cancel();


    }


    private static Handler getHandler() {
        synchronized (SuperTask.class) {
            if (sHandler == null) {
                sHandler = new ToggleHandler();
            }
            return sHandler;
        }
    }


    public SuperTask(CallBack callBack){
        this.mCallBack = callBack;

        getHandler();//确保handler在UI线程创建，SuperTask必须在UI线程创建

        mFuture = new FutureTask<Result>(new Callable<Result>() {
            @Override
            public Result call() throws Exception {
                mTaskInvoked.set(true);
                Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);

                Result result = doInBackgroud();
                Binder.flushPendingCommands();

                sendMsgToUIThread(result);

                return result;  //这个返回结果 仅用于 done()方法 mFuture.get()获取
            }
        }){
            @Override
            protected void done() {
                try {
                    boolean wasTaskInvoked = mTaskInvoked.get();  // TODO: 16/11/25 nothing to do
                    Log.d("SuperTask", "wasTaskInvoked:" + wasTaskInvoked);

                    if (!wasTaskInvoked) {
                        sendMsgToUIThread(mFuture.get());
                    }

                } catch (InterruptedException e) {
                    Log.d(TAG, "e:" + e);
                } catch (ExecutionException e) {

                    throw new RuntimeException("An error occurred while executing doInBackground()",e.getCause());

                } catch (CancellationException e) {
                    Log.d(TAG, "e:" + e);
                    boolean wasTaskInvoked = mTaskInvoked.get();
                    if (!wasTaskInvoked) {
                        sendMsgToUIThread(null);
                    }

                }
            }
        };
    }

    private void sendMsgToUIThread(Result result) {
        MessageBean messageBean = new MessageBean(this);
        Message message = getHandler().obtainMessage(MESSAGE_POST_RESULT, messageBean);
        message.sendToTarget();

    }

    private  Result doInBackgroud(){

        Result result = (Result) mCallBack.doInBackgroud();
        return result;
    };

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

        ExecutorUtils.defaultExecutor().execute(mFuture);

        return this;

    }

    public void finish() {
        //UI线程

        boolean isCancelled = mCancelled.get();
        if (isCancelled) {
            mCallBack.cancel();
        } else {
            mCallBack.after();
        }
        mStatus = Status.FINISHED;


    }


    public final boolean cancel(boolean mayInterruptIfRunning) {
        mCancelled.set(true);  //设置标志位 取消Task
        return mFuture.cancel(mayInterruptIfRunning);
    }






}
