package com.wuyiling.worktest.Utils.thread.threadpool;

import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.yuuwei.faceview.util.thread.task.TaskFunction;
import com.yuuwei.faceview.util.thread.task.TaskRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author: lingjun.jlj
 * @date: 2019/8/20 14:56
 * @description:
 */
@Slf4j
@Component
public class FastThreadPool {
    /**
     * 60s
     */
    private static final long DEFAULT_THREAD_PROCESS_TIME_OUT = 60000L;
    /**
     * 20s
     */
    private static final long DEFAULT_FUTURE_GET_TIME_OUT = 20000L;

    /**
     * 线程池维护线程的最少数量，IO型任务，CPU资源消耗不是很大
     */
    private static final int corePoolSize = Runtime.getRuntime().availableProcessors() * 4;

    /**
     * 线程池维护线程的最大数量
     * 核心线程数 * 2
     */
    private static final int maximumPoolSize = corePoolSize * 4;

    /**
     * 线程池维护非核心线程所允许的空闲时间
     */
    private static final int keepAliveTime = 60;

    /**
     * 线程池所使用的缓冲队列的最大数量,用于创建有界的缓冲队列
     */
    private static final int queueSize = 256;

    /**
     * 线程池所使用的缓冲队列
     */
    private BlockingQueue<Runnable> workQueue;

    /**
     * 线程池对拒绝任务的处理策略
     */
    private RejectedExecutionHandler handler;

    private ThreadFactory threadFactory;

    private ThreadPoolExecutor threadPoolExecutor;

    private ListeningExecutorService service;

    /**
     * @PostConstruct 在spring容器初始化时执行该方法
     */
    @PostConstruct
    public void initialize() {
        //基于数组结构的有界阻塞队列，按FIFO排序任务
        workQueue = new ArrayBlockingQueue(queueSize);
        //创建线程的工厂，通过自定义的线程工厂可以给每个新建的线程设置一个具有识别度的线程名
        threadFactory = new ThreadNameFactory("Parallel-Processor", null, true);
        //用调用者所在的线程来执行任务
        handler = new ThreadPoolExecutor.CallerRunsPolicy();
        threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime,
                TimeUnit.SECONDS, workQueue, threadFactory, handler);
        service = MoreExecutors.listeningDecorator(threadPoolExecutor);
    }


    @PreDestroy
    public void stop() {
        service.shutdownNow();
        workQueue.clear();
    }

    /**
     * 提交并发请求
     *
     * @param taskRequest
     */
    public <V> List<V> execute(final TaskRequest taskRequest) {
        long threadProcessTimeout = DEFAULT_THREAD_PROCESS_TIME_OUT;
        return execute(taskRequest, threadProcessTimeout);
    }

    /**
     * 提交并发请求
     *
     * @param taskRequest          请求
     * @param threadProcessTimeout 线程处理时间
     */
    public <V> List<V> execute(final TaskRequest taskRequest, long threadProcessTimeout) {
        if (log.isDebugEnabled()) {
            log.debug("Try to parallel process Parallel process task count :" + taskRequest.getTaskCount());
        }
        //将任务数量进行计数
        final CountDownLatch latch = new CountDownLatch(taskRequest.getTaskCount());
        log.info("当前执行的任务数量为：【{}】",latch.getCount());

        List<ListenableFuture<V>> futureList = new ArrayList(taskRequest.getTaskCount());
        for (int i = 0; i < taskRequest.getTaskCount(); i++) {
            final int index = i;
            ListenableFuture<V> futureTaskResult = service.submit(() -> {
                V result = null;
                try {
                    long startTime = System.currentTimeMillis();
                    TaskFunction<V> taskFunction = taskRequest.getTaskList().get(index);
                    result = taskFunction.apply();
                    long endTime = System.currentTimeMillis();
                    if (log.isDebugEnabled()) {
                        log.debug("Try to parallel process Parallel process task totalTime :" + (endTime - startTime));
                    }
                    return result;
                } catch (Throwable e) {
                    log.error("thread pool process future task failed:", e);
                    throw new RuntimeException(e);
                } finally {
                    //计数器减一
                    latch.countDown();
                }
            });
            futureList.add(futureTaskResult);
        }
        ListenableFuture<List<V>> finalFuture =
                taskRequest.getIgnoreError() ? Futures.successfulAsList(futureList) : Futures.allAsList(futureList);
        if (taskRequest.getCallback() != null) {
            Futures.addCallback(finalFuture, taskRequest.getCallback(), threadPoolExecutor);
        }
        try {
            //计数器减到0时返回
            latch.await(threadProcessTimeout, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            log.error("latch.await parallel process failed, maybe timeout:", e);
        }
        List<V> taskResultList;
        try {
            taskResultList = finalFuture.get(DEFAULT_FUTURE_GET_TIME_OUT, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            log.error("thread pool get result exception:", e);
            if (finalFuture.cancel(true)) {
                log.warn("task execution time out, thread pool cancel task success!");
            } else {
                log.error("thread pool cancel task failed!");
            }
            throw new RuntimeException("thread pool execute error", e);
        }
        return taskResultList;
    }

}
