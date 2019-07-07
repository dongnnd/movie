package com.example.movie.uitil;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadExecutor {

    private int corePoolSizeForThumbnail = 4;
    private int corePoolSizeForLoadMovie = 1;
    private int maxPoolSize = 100;
    private long keepAlive = 5000;
    private TimeUnit unit = TimeUnit.SECONDS;
    private BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<Runnable>(corePoolSizeForThumbnail, true);
    private RejectedExecutionHandler handler = new ThreadPoolExecutor.CallerRunsPolicy();
    private ExecutorService mServiceLoadMovie;
    private ExecutorService mServiceLoadThumbnail;

    private static ThreadExecutor mInstance;

    public static ThreadExecutor getInstance(){
        if(mInstance == null){
            mInstance = new ThreadExecutor();
        }

        return mInstance;
    }

    public ThreadExecutor(){
        mServiceLoadMovie = Executors.newFixedThreadPool(corePoolSizeForLoadMovie);
        mServiceLoadThumbnail = new ThreadPoolExecutor(corePoolSizeForThumbnail, maxPoolSize, keepAlive, unit, workQueue, handler);
    }

    public void addTaskLoadMovie(Callable callable){
        mServiceLoadMovie.submit(callable);
    }

    public void addTaskLoadThumbnail(Callable callable){
        mServiceLoadThumbnail.submit(callable);
    }
}
