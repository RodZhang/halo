package com.rod.halo.mulittask;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Process;
import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Rod
 * @date 2019/3/5
 */
public class MultiTask<R> {
    private static final ExecutorService EXECUTOR = Executors.newFixedThreadPool(10);
    private static final Handler WORK_HANDLER = newWorkHandler();
    private static final Handler UI_HANDLER = new Handler(Looper.getMainLooper());

    private final R mRsp;
    private final List<AbsTaskUnit<?, R>> mTasks = new ArrayList<>();

    public MultiTask(@NonNull R rsp) {
        mRsp = rsp;
    }

    @NonNull
    public MultiTask<R> addTask(@NonNull AbsTaskUnit<?, R> task) {
        mTasks.add(task);
        return this;
    }

    public void start(@NonNull final OnTaskFinishCallback<R> callback) {
        final long start = System.currentTimeMillis();
        for (AbsTaskUnit task : mTasks) {
            task.doTaskInner(EXECUTOR);
        }
        WORK_HANDLER.post(new Runnable() {
            @Override
            public void run() {
                long transformStart = System.currentTimeMillis();
                for (AbsTaskUnit<?, R> task : mTasks) {
                    task.transformInner(mRsp);
                }

                UI_HANDLER.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onTaskFinish(mRsp);
                    }
                });
            }
        });
    }

    private static Handler newWorkHandler() {
        HandlerThread handlerThread = new HandlerThread("MultiTaskThread", Process.THREAD_PRIORITY_LOWEST);
        handlerThread.start();
        return new Handler(handlerThread.getLooper());
    }

    public interface OnTaskFinishCallback<Rsp> {

        void onTaskFinish(Rsp rsp);
    }
}
