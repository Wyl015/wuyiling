package com.wuyiling.worktest.Utils.thread.threadpool;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author: lingjun.jlj
 * @date: 2019/8/20 14:54
 * @description: 线程名字工厂类
 */
public class ThreadNameFactory implements ThreadFactory {

    private String prefix;
    private ThreadGroup group;
    private boolean isDaemon;
    private AtomicInteger tNo;

    public ThreadNameFactory(String prefix) {
        this(prefix, null, false);
    }

    public ThreadNameFactory(String prefix, ThreadGroup group, boolean isDaemon) {
        tNo = new AtomicInteger(0);
        this.prefix = prefix;
        if (null != group) {
            this.group = group;
        } else {
            SecurityManager sm = System.getSecurityManager();
            group = (sm != null) ? sm.getThreadGroup() : Thread.currentThread().getThreadGroup();
        }
        this.isDaemon = isDaemon;
    }

    @Override
    public Thread newThread(Runnable runnable) {
        Thread thread = new Thread(group, runnable, new StringBuilder(prefix).append("-Thread-").append(tNo.getAndIncrement()).toString());
        thread.setDaemon(isDaemon);
        if (thread.getPriority() != Thread.NORM_PRIORITY) {
            thread.setPriority(Thread.NORM_PRIORITY);
        }
        return thread;
    }
}
