package com.rod.halo

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.os.Handler
import android.os.Looper
import com.rod.halo.utils.NetworkUtil

/**
 *
 * @author Rod
 * @date 2018/12/15
 */
class BaseApp : Application() {

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var gContext: Context
        val gMainHandler = Handler(Looper.getMainLooper())
    }

    override fun onCreate() {
        super.onCreate()
        gContext = this

        NetworkUtil.listenNetworkState(this)
    }
}