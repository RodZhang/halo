package com.rod.halo.utils

import android.annotation.SuppressLint
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import com.rod.halo.BaseApp

/**
 *
 * @author Rod
 * @date 2018/12/15
 */
object NetworkUtil {

    private val mIsNetworkEnable = MutableLiveData<Boolean>()

    init {
        mIsNetworkEnable.value = isNetworkEnable(BaseApp.gContext)
    }

    fun isNetworkEnable(): Boolean {
        return mIsNetworkEnable.value ?: isNetworkEnable(BaseApp.gContext)
    }

    fun bindNetworkEnable(owner: LifecycleOwner, observer: Observer<Boolean>) {
        mIsNetworkEnable.observe(owner, observer)
    }

    fun unbindNetworkEnable(observer: Observer<Boolean>) {
        mIsNetworkEnable.removeObserver(observer)
    }

    internal fun onNetworkStateChanged(info: NetworkInfo) {
        //如果当前的网络连接成功并且网络连接可用
        if (NetworkInfo.State.CONNECTED == info.getState() && info.isAvailable()) {
            if (info.getType() == ConnectivityManager.TYPE_WIFI || info.getType() == ConnectivityManager.TYPE_MOBILE) {
                mIsNetworkEnable.value = true
            }
        } else {
            mIsNetworkEnable.value = false
        }
    }

    @SuppressLint("MissingPermission")
    private fun isNetworkEnable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return connectivityManager.activeNetworkInfo?.isConnected ?: false
    }
}