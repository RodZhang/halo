package com.rod.halo.utils

import android.util.Log

/**
 *
 * @author Rod
 * @date 2018/12/30
 */
object RL {
    const val LEVEL_VERBOSE = 1
    const val LEVEL_DEBUG = 2
    const val LEVEL_INFO = 3
    const val LEVEL_WARN = 4
    const val LEVEL_ERROR = 5

    private var level = LEVEL_DEBUG

    fun v(tag: String, msg: String) {
        if (level >= LEVEL_VERBOSE) {
            Log.v(tag, msg)
        }
    }

    fun d(tag: String, msg: String) {
        if (level >= LEVEL_DEBUG) {
            Log.d(tag, msg)
        }
    }

}