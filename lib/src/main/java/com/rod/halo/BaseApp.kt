package com.rod.halo

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

/**
 *
 * @author Rod
 * @date 2018/12/15
 */
class BaseApp : Application() {

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var gContext: Context
    }

    override fun onCreate() {
        super.onCreate()
        gContext = this
    }
}