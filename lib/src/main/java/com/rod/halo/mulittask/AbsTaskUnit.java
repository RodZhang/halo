package com.rod.halo.mulittask;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * @author Rod
 * @date 2019/3/5
 */
public abstract class AbsTaskUnit<I, O> {

    private final byte[] mLock = new byte[0];
    private volatile I mResult;
    private Future<I> mFuture;

    public abstract void doTask();

    public abstract void transform(@Nullable I input, @NonNull O out);

    void doTaskInner(ExecutorService executor) {
        mFuture = executor.submit(new Callable<I>() {
            @Override
            public I call() throws Exception {
                doTask();
                synchronized (mLock) {
                    mLock.wait();
                }
                return mResult;
            }
        });
    }

    void transformInner(O rsp) {
        transform(get(), rsp);
    }

    private I get() {
        if (mFuture == null) {
            throw new IllegalStateException("call doTaskInner first");
        }
        try {
            return mFuture.get();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void taskEnd(@Nullable I input) {
        mResult = input;
        synchronized (mLock) {
            mLock.notify();
        }
    }
}
