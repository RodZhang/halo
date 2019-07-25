package com.rod.halo.statusview

import android.os.Bundle
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import com.rod.halo.utils.UIUtil

/**
 *
 * @author Rod
 * @date 2019/5/5
 */
class StatusHost(private val mParent: ViewGroup, val mPos: Int) {

    private val mTypeView = SparseArray<View>()

    fun switchStatusView(oldStatusView: StatusView?, newStatusView: StatusView?, data: Bundle?) {
        oldStatusView?.run {
            getView()?.visibility = View.GONE
            onVisibleChange(false)
        }

        initStatusViewIfNeed(newStatusView)
        newStatusView?.run {
            val view = getView()
                    ?: throw IllegalStateException("$this is inited bug getView return null")
            with(view) {
                if (parent != mParent) {
                    UIUtil.removeFromParent(this)
                    mParent.addView(this, mPos)
                }
                visibility = View.VISIBLE
            }
            onVisibleChange(true, data)
        }
    }

    private fun initStatusViewIfNeed(statusView: StatusView?) {
        statusView?.run {
            if (isInited()) {
                return
            }
            if (getViewType() == StatusView.VIEW_TYPE_UN_REUSEABLE) {
                initView(mParent)
                return
            }
            val cacheView = mTypeView[getViewType()]
            if (cacheView == null) {
                initView(mParent)
                val view = getView()
                        ?: throw IllegalStateException("$this is inited bug getView return null")
                mTypeView.append(getViewType(), view)
            } else {
                setView(cacheView)
            }
        }
    }
}