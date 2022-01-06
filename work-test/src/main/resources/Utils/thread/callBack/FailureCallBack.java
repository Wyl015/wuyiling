package com.wuyiling.worktest.Utils.thread.callBack;


import com.google.common.util.concurrent.FutureCallback;

/**
 * @author: lingjun.jlj
 * @date: 2019/8/20 14:58
 * @description: 失败回调
 */
public interface FailureCallBack<V> extends FutureCallback<V> {

    default void onFailure(Throwable throwable) {
    }

}
