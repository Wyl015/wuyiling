package com.wuyiling.worktest.Utils.thread.task;

/**
 * @author: lingjun.jlj
 * @date: 2019/8/20 14:57
 * @description:
 */
public interface TaskFunction<T> {

    T apply();

    @Override
    boolean equals(Object object);
}
