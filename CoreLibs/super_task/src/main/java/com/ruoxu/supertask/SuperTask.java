package com.ruoxu.supertask;

import android.os.AsyncTask;
import android.os.Handler;

/**
 * Created by wangli on 16/11/24.
 */
public class SuperTask {

    private final String TAG = getClass().getSimpleName();

    private static Handler sHandler;

    private static Handler getHandler() {
        synchronized (AsyncTask.class) {
            if (sHandler == null) {
                sHandler = new ToggleHandler();
            }
            return sHandler;
        }
    }



}
