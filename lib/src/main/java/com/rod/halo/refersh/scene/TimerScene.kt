package com.rod.halo.refersh.scene

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.OnLifecycleEvent
import java.util.concurrent.TimeUnit

/**
 *
 * @author Rod
 * @date 2018/12/15
 */
class TimerScene(private val mLifecycleOwner: LifecycleOwner,
                 private val mRefreshInterval: Long = DEFAULT_REFRESH_INTERVAL)
    : BaseRefreshScene(), LifecycleObserver {

    init {
        mLifecycleOwner.lifecycle.addObserver(this)
    }

    companion object {
        private val DEFAULT_REFRESH_INTERVAL = TimeUnit.MINUTES.toMillis(10)
    }

    private var mLastSuccessRefreshTime = 0L

    override fun needRefresh(manual: Boolean): Boolean {
        return if (manual) manual else System.currentTimeMillis() - mLastSuccessRefreshTime >= mRefreshInterval
    }

    override fun onRefreshSuccess() {
        mLastSuccessRefreshTime = System.currentTimeMillis()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        if (needRefresh(false)) {
            refresh()
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        mLifecycleOwner.lifecycle.removeObserver(this)
    }
}