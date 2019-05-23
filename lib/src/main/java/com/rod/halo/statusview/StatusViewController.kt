package com.rod.halo.statusview

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

    fun putStatusView(statusView: StatusView) {
        mStatusViews[statusView.getId()] = statusView
    }

    /**
     * @see ViewStatus
     */
    fun showStatusView(id: String) {
        val host = mHost ?: throw IllegalStateException("mContainerInfo is null, call setHost first")

        val nextStatusView = mStatusViews[id]
        if (mCurStatusView == nextStatusView || nextStatusView == null) {
            return
        }

        with(host) {
            removeChild(mCurStatusView)
            addChild(nextStatusView)
        }
        mCurStatusView = nextStatusView
    }

    fun setHost(parent: ViewGroup, pos: Int) {
        mHost = StatusHost(parent, pos)
    }
}