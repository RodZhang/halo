package com.rod.halo.statusview

import android.os.Bundle
import android.view.ViewGroup

/**
 *
 * @author Rod
 * @date 2019/5/4
 */
class StatusViewController {

    private val mStatusViews = HashMap<String, StatusView>()
    private var mCurStatusView: StatusView? = null
    private var mHost: StatusHost? = null

    internal fun putContentView(statusView: StatusView) {
        putStatusView(statusView)
        showStatusView(statusView.getId())
    }

    fun putStatusView(statusView: StatusView) {
        mStatusViews[statusView.getId()] = statusView
    }

    /**
     * @see ViewStatus
     */
    fun showStatusView(id: String, data: Bundle? = null) {
        val host = mHost ?: throw IllegalStateException("mContainerInfo is null, call setHost first")
        val nextStatusView = mStatusViews[id] ?: throw IllegalStateException("do not have state view of id: $id")

        if (mCurStatusView == nextStatusView) {
            return
        }

        host.switchStatusView(mCurStatusView, nextStatusView, data)
        mCurStatusView = nextStatusView
    }

    fun setHost(parent: ViewGroup, pos: Int) {
        mHost = StatusHost(parent, pos)
    }
}