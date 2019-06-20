package com.rod.halo.utils

import android.annotation.SuppressLint
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.content.Context
import android.content.IntentFilter
import android.net.*
import android.os.Build
import android.support.annotation.RequiresApi
import com.rod.halo.BaseApp
import com.rod.halo.NetworkReceiver

/**
 *
 * @author Rod
 * @date 2018/12/15
 */
object NetworkUtil {

    private const val TAG = "NetworkUtil"

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
        if (NetworkInfo.State.CONNECTED == info.state && info.isAvailable) {
            if (info.type == ConnectivityManager.TYPE_WIFI || info.type == ConnectivityManager.TYPE_MOBILE) {
                mIsNetworkEnable.value = true
                HL.d(TAG, "onNetworkStateChanged, network enable")
            }
        } else {
            mIsNetworkEnable.value = false
            HL.d(TAG, "onNetworkStateChanged, network disable")
        }
    }

    internal fun listenNetworkState(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            listenNetworkStateWithNetworkCallback(context)
        } else {
            listenNetworkStateWithBroadcastReceiver(context)
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun listenNetworkStateWithNetworkCallback(context: Context) {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE)
        if (connectivityManager is ConnectivityManager) {
            connectivityManager.requestNetwork(NetworkRequest.Builder().build(),
                    object : ConnectivityManager.NetworkCallback() {
                        /**
                         * 网络可用的回调
                         */
                        override fun onAvailable(network: Network?) {
                            super.onAvailable(network)
                            BaseApp.gMainHandler.post { mIsNetworkEnable.value = true }
                            HL.d(TAG, "NetworkCallback, onAvailable")
                        }

                        /**
                         * 网络丢失的回调
                         */
                        override fun onLost(network: Network?) {
                            BaseApp.gMainHandler.post { mIsNetworkEnable.value = false }
                            HL.d(TAG, "NetworkCallback, onLost")
                        }

                        /**
                         * 按照官方的字面意思是，当我们的网络的某个能力发生了变化回调，那么也就是说可能会回调多次
                         */
                        override fun onCapabilitiesChanged(network: Network?, networkCapabilities: NetworkCapabilities?) {
                            HL.d(TAG, "NetworkCallback, onCapabilitiesChanged")
                        }

                        /**
                         * 当建立网络连接时，回调连接的属性
                         */
                        override fun onLinkPropertiesChanged(network: Network?, linkProperties: LinkProperties?) {
                            HL.d(TAG, "NetworkCallback, onLinkPropertiesChanged")
                        }

                        /**
                         * 按照官方注释的解释，是指如果在超时时间内都没有找到可用的网络时进行回调
                         */
                        override fun onUnavailable() {
                            super.onUnavailable()
                            HL.d(TAG, "NetworkCallback, onUnavailable")
                        }

                        /**
                         * 在网络失去连接的时候回调，但是如果是一个生硬的断开，他可能不回调
                         */
                        override fun onLosing(network: Network?, maxMsToLive: Int) {
                            super.onLosing(network, maxMsToLive)
                            HL.d(TAG, "NetworkCallback, onLosing")
                        }
                    })
        } else {
            HL.d(TAG, "get connectivityManager fail")
            listenNetworkStateWithBroadcastReceiver(context)
        }
    }

    private fun listenNetworkStateWithBroadcastReceiver(context: Context) {
        val intentFilter = IntentFilter("android.net.conn.CONNECTIVITY_CHANGE")
        context.registerReceiver(NetworkReceiver(), intentFilter)
    }

    @SuppressLint("MissingPermission")
    private fun isNetworkEnable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return connectivityManager.activeNetworkInfo?.isConnected ?: false
    }
}