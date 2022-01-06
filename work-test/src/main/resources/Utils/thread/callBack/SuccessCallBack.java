package com.wuyiling.worktest.Utils.thread.callBack;

import com.google.common.util.concurrent.FutureCallback;

/**
 * @author: lingjun.jlj
 * @date: 2019/8/20 15:04
 * @description: 成功回调
 */
public interface SuccessCallBack<V> extends FutureCallback<V> {

    default void onSuccess(V result) {
    }
}