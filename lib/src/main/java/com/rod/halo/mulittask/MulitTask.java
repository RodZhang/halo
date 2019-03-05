package com.rod.halo.mulittask;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Rod
 * @date 2019/3/5
 */
public class MulitTask<R> {

    private final R mRsp;
    private final List<TaskItem> mTasks = new ArrayList<>();

    public MulitTask(@NonNull R rsp) {
        mRsp = rsp;
    }

    public void subscribe(@NonNull OnTaskFinishCallback<R> callback) {

    }

    @NonNull
    public MulitTask addTask(@NonNull TaskItem task) {
        mTasks.add(task);
        return this;
    }

    interface OnTaskFinishCallback<Rsp> {

        void onTaskFinish(Rsp rsp);
    }
}
