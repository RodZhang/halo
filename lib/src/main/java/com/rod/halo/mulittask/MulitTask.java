package com.rod.halo.mulittask;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Rod
 * @date 2019/3/5
 */
public class MulitTask<R> {
    private static final ExecutorService EXECUTOR = Executors.newFixedThreadPool(10);

    private final R mRsp;
    private final List<TaskItem<?, R>> mTasks = new ArrayList<>();

    public MulitTask(@NonNull R rsp) {
        mRsp = rsp;
    }

    @NonNull
    public MulitTask<R> addTask(@NonNull TaskItem<?, R> task) {
        mTasks.add(task);
        return this;
    }

    public void start(@NonNull OnTaskFinishCallback<R> callback) {
        long start = System.currentTimeMillis();
        for (TaskItem task : mTasks) {
            task.doTaskInner(EXECUTOR);
        }
        // TODO: 2019/3/5 这里会阻塞调用线程，需要增加线程切换方法
        for (TaskItem<?, R> task : mTasks) {
            task.transformInner(mRsp);
        }
        System.out.println("task cost " + (System.currentTimeMillis() - start) + "ms");
        callback.onTaskFinish(mRsp);
    }

    public interface OnTaskFinishCallback<Rsp> {

        void onTaskFinish(Rsp rsp);
    }
}
