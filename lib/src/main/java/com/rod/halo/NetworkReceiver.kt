package com.rod.halo

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.wifi.WifiManager
import com.rod.halo.utils.NetworkUtil
import com.rod.halo.utils.RL


/**
 *
 * @author Rod
 * @date 2018/12/16
 */
class NetworkReceiver : BroadcastReceiver() {
    companion object {
        const val TAG = "NetworkReceiver"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent == null) {
            return
        }
        if (WifiManager.WIFI_STATE_CHANGED_ACTION == intent.action) {// 监听wifi的打开与关闭，与wifi的连接无关
            val wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0)
            RL.e(TAG, "wifiState:$wifiState")
            when (wifiState) {
                WifiManager.WIFI_STATE_DISABLED -> {
                }
                WifiManager.WIFI_STATE_DISABLING -> {
                }
            }
        }
        // 监听网络连接，包括wifi和移动数据的打开和关闭,以及连接上可用的连接都会接到监听
        if (ConnectivityManager.CONNECTIVITY_ACTION == intent.action) {
            //获取联网状态的NetworkInfo对象
            val info = intent.getParcelableExtra<NetworkInfo>(ConnectivityManager.EXTRA_NETWORK_INFO)
            if (info != null) {
                NetworkUtil.onNetworkStateChanged(info)
            }
        }
    }
}