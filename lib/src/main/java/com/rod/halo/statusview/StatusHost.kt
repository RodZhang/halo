package com.rod.halo.statusview

import android.view.ViewGroup

/**
 *
 * @author Rod
 * @date 2019/5/5
 */
class StatusHost(private val parent: ViewGroup, val pos: Int) {

    fun removeChild(statusView: StatusView?) {
        statusView?.run {
            parent.removeView(getView())
            onDetach(parent)
        }
    }

    fun addChild(statusView: StatusView?) {
        statusView?.run {
            parent.addView(getView(), pos)
            onAttach(parent)
        }
    }
}