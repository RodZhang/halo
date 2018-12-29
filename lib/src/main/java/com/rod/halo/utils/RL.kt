package com.rod.halo.utils

import android.util.Log

/**
 *
 * @author Rod
 * @date 2018/12/30
 */
object RL {
    const val LEVEL_DISABLE = 0
    const val LEVEL_VERBOSE = 1
    const val LEVEL_DEBUG = 2
    const val LEVEL_INFO = 3
    const val LEVEL_WARN = 4
    const val LEVEL_ERROR = 5

    private var level = LEVEL_DEBUG

    fun v(tag: String, msg: String, throwable: Throwable? = null) {
        log(tag, msg, throwable, LEVEL_VERBOSE,
                { t, m -> Log.v(t, m) },
                { t, m, tr -> Log.v(t, m, tr) })
    }

    fun d(tag: String, msg: String, throwable: Throwable? = null) {
        log(tag, msg, throwable, LEVEL_DEBUG,
                { t, m -> Log.d(t, m) },
                { t, m, tr -> Log.d(t, m, tr) })
    }

    fun i(tag: String, msg: String, throwable: Throwable? = null) {
        log(tag, msg, throwable, LEVEL_INFO,
                { t, m -> Log.i(t, m) },
                { t, m, tr -> Log.i(t, m, tr) })
    }

    fun w(tag: String, msg: String, throwable: Throwable? = null) {
        log(tag, msg, throwable, LEVEL_WARN,
                { t, m -> Log.w(t, m) },
                { t, m, tr -> Log.w(t, m, tr) })
    }

    fun e(tag: String, msg: String, throwable: Throwable? = null) {
        log(tag, msg, throwable, LEVEL_ERROR,
                { t, m -> Log.e(t, m) },
                { t, m, tr -> Log.e(t, m, tr) })
    }

    private fun log(tag: String, msg: String, throwable: Throwable?,
                    level: Int, log: (t: String, m: String) -> Int,
                    logWithThrowable: (t: String, m: String, tr: Throwable?) -> Int) {
        if (this.level < level) {
            return
        }
        if (throwable == null) log(tag, msg) else logWithThrowable(tag, msg, throwable)
    }
}