package com.example.movie.uitil;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadExecutor {

    private int corePoolSize = 50;
    private int maxPoolSize = 100;
    private long keepAlive = 5000;
    private TimeUnit unit = TimeUnit.SECONDS;
    private BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<Runnable>(corePoolSize, true);
    private RejectedExecutionHandler handler = new ThreadPoolExecutor.CallerRunsPolicy();
    private ExecutorService mService;

    private static ThreadExecutor mInstance;

    public static ThreadExecutor getInstance(){
        if(mInstance == null){
            mInstance = new ThreadExecutor();
        }

        return mInstance;
    }

    public ThreadExecutor(){
        mService = new ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAlive, unit, workQueue, handler);
    }
}
