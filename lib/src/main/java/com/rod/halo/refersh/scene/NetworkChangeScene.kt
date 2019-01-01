package com.rod.halo.refersh.scene

import android.arch.lifecycle.*
import android.util.Log
import com.rod.halo.utils.NetworkUtil

/**
 *
 * @author Rod
 * @date 2018/12/15
 */
class NetworkChangeScene(private val mLifecycleOwner: LifecycleOwner)
    : BaseRefreshScene(), LifecycleObserver {

    companion object {
        private const val TAG = "NetworkChangeScene"
    }

    init {
        mLifecycleOwner.lifecycle.addObserver(this)
    }

    private val mNetworkObserver = Observer<Boolean> {
        it?.let { enable ->
            Log.d(TAG, "net enable change to $enable")
            if (enable) {
                refresh()
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        NetworkUtil.bindNetworkEnable(mLifecycleOwner, mNetworkObserver)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        NetworkUtil.unbindNetworkEnable(mNetworkObserver)
        mLifecycleOwner.lifecycle.removeObserver(this)
    }

    // Todo: 还需要判断当前列表是否有数据
    override fun needRefresh(manual: Boolean): Boolean {
        return NetworkUtil.isNetworkEnable()
    }
}