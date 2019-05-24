package com.rod.halo.statusview

import android.view.View
import android.view.ViewGroup
import com.rod.halo.utils.UIUtil

/**
 *
 * @author Rod
 * @date 2019/5/5
 */
class StatusHost(private val mParent: ViewGroup, val mPos: Int) {

    fun switchStatusView(oldStatusView: StatusView?, newStatusView: StatusView?) {
        oldStatusView?.run {
            getView().visibility = View.GONE
            onDetach(mParent)
        }

        newStatusView?.run {
            with(getView()) {
                if (parent != mParent) {
                    UIUtil.removeFromParent(this)
                    mParent.addView(this, mPos)
                }
                visibility = View.VISIBLE
            }
            onAttach(mParent)
        }
    }
}