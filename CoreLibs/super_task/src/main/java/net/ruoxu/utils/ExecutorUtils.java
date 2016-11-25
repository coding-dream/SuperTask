package net.ruoxu.utils;

import android.util.Log;

import java.util.ArrayDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by wangli on 16/11/24.
 */
public class ExecutorUtils {


    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int CORE_POOL_SIZE = CPU_COUNT + 1;
    private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;
    private static final int KEEP_ALIVE = 1;

    private static final BlockingQueue<Runnable> sPoolWorkQueue = new LinkedBlockingQueue<Runnable>(128);



    private static final ThreadFactory sThreadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        public Thread newThread(Runnable r) {
            return new Thread(r, "AsyncTask #" + mCount.getAndIncrement());
        }
    };

    //构建一个线程池
    public static final Executor THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE, TimeUnit.SECONDS, sPoolWorkQueue, sThreadFactory);


    //构建一个串行线程池（SerialExecutor按其作用来讲，并不能算是一个真正的线程池，仅仅间接调用--> THREAD_POOL_EXECUTOR来执行线程）
    public static final Executor SERIAL_EXECUTOR = new SerialExecutor();

    //默认线程池 SERIAL_EXECUTOR
    private static volatile Executor sDefaultExecutor = SERIAL_EXECUTOR;



    private static class SerialExecutor implements Executor {
        final ArrayDeque<Runnable> mTasks = new ArrayDeque<Runnable>();
        Runnable mActive;

        public synchronized void execute(final Runnable r) {

            mTasks.offer(new Runnable() {
                public void run() {
                    try {
                        r.run();
                    } finally {
                        scheduleNext();
                    }
                }
            });

            if (mActive == null) {
                scheduleNext();
            }
        }

        protected synchronized void scheduleNext() {
            if ((mActive = mTasks.poll()) != null) {
                THREAD_POOL_EXECUTOR.execute(mActive);
            }
        }
    }



    /**
     * @return 返回一个可用的线程池
     */
    public static Executor pool_executor(){
        return THREAD_POOL_EXECUTOR;
    }

    /**
     *
     * @return 返回一个串行串行线程池(利用队列ArrayDeque)
     */
    public static Executor executor(){
        return SERIAL_EXECUTOR;
    }

    /**
     *
     */

    /**
     * @return 返回默认线程池
     */

    public static Executor defaultExecutor(){
        return sDefaultExecutor;
    }







}
