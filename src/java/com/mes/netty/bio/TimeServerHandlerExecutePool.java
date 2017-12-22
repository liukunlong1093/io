package com.mes.netty.bio;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 时间服务处理线程池
 *
 * @author liukl
 * @date 2017/12/19
 */
public class TimeServerHandlerExecutePool {
    private ExecutorService executor;
    public TimeServerHandlerExecutePool(int maxPoolSize,int queueSize){
        executor=new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(),maxPoolSize,120L, TimeUnit.SECONDS,new ArrayBlockingQueue <Runnable>(queueSize));
    }
    public void execute(java.lang.Runnable task){
        executor.execute(task);
    }
}
