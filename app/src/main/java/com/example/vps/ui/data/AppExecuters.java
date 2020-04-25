package com.example.vps.ui.data;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Global executor pools for the whole application.
 * <p>
 * Grouping tasks like this avoids the effects of task starvation (e.g. disk reads don't wait behind
 * webservice requests).
 */
public class AppExecuters {
    private static final int THREAD_COUNT = 3;

    Executor diskIo;
    Executor networkIo;
    Executor mainThread;



     AppExecuters(Executor diskIo, Executor netwirkIo, Executor mainThread) {
        this.diskIo = diskIo;
        this.networkIo = netwirkIo;
        this.mainThread = mainThread;
    }

    public AppExecuters(){
         this(Executors.newSingleThreadExecutor(),
                 Executors.newFixedThreadPool(THREAD_COUNT), new MainThreadExecuter());
    }


    public Executor diskIO(){
        return diskIo;
    }

    public Executor networkIO(){
        return networkIo;
    }

    public Executor mainThread(){
        return mainThread;
    }

    private static class MainThreadExecuter implements Executor{
        private final Handler handler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(Runnable command) {
         handler.post(command);
        }
    }

}

