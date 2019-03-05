package com.rod.halo.mulittask;

import android.support.annotation.NonNull;

/**
 * @author Rod
 * @date 2019/3/5
 */
public abstract class TaskItem<I, O> {

    public abstract void doTask();

    public abstract void transform(@NonNull O out);
}
