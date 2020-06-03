package com.rod.halo.simple.refresh

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rod.halo.refersh.abs.IARefreshMode
import com.rod.halo.refersh.abs.RefreshLayoutAdapter
import com.rod.halo.refersh.abs.RefreshMode
import com.rod.halo.simple.R
import com.scwang.smartrefresh.layout.SmartRefreshLayout

/**
 *
 * @author Rod
 * @date 2019/5/7
 */
class SmartRefreshLayoutAdapter : RefreshLayoutAdapter {

    private var mSmartRefreshLayout: SmartRefreshLayout? = null
    private var mRefreshMode = RefreshMode.DISABLE

    @SuppressLint("InflateParams")
    override fun getRefreshLayout(context: Context): ViewGroup {
        if (mSmartRefreshLayout == null) {
            mSmartRefreshLayout = LayoutInflater.from(context)
                    .inflate(R.layout.common_smart_refresh_layout, null) as SmartRefreshLayout
            setMode(mRefreshMode)
        }
        return mSmartRefreshLayout as ViewGroup
    }

    override fun getRefreshLayout() = mSmartRefreshLayout

    override fun setContentView(view: View) {
        if (view.parent != null && view.parent != mSmartRefreshLayout) {
            throw IllegalStateException("remove view from parent first")
        }

        if (view.parent == null) {
            mSmartRefreshLayout?.setRefreshContent(view)
        }
    }

    override fun setMode(@IARefreshMode refreshMode: String) {
        mRefreshMode = refreshMode
        if (mSmartRefreshLayout == null) {
            return
        }
        when (refreshMode) {
            RefreshMode.DISABLE -> setEnableMode(false, false)
            RefreshMode.PULL_DOWN_ONLY -> setEnableMode(true, false)
            RefreshMode.PULL_UP_ONLY -> setEnableMode(false, true)
            RefreshMode.BOTH -> setEnableMode(true, true)
        }
    }

    override fun disableContentWhenRefresh(disable: Boolean) {
        mSmartRefreshLayout?.apply {
            setDisableContentWhenRefresh(disable)
            setDisableContentWhenLoading(disable)
        }
    }

    override fun finishRefresh(success: Boolean, hasMore: Boolean) {
        mSmartRefreshLayout?.also {
            it.finishRefresh(success)
            it.setEnableLoadMore(hasMore)
        }
    }

    override fun finishLoadMore(success: Boolean, hasMore: Boolean) {
        mSmartRefreshLayout?.also {
            it.finishLoadMore(success)
            it.setEnableLoadMore(hasMore)
        }
    }

    private fun setEnableMode(refreshEnable: Boolean, loadMoreEnable: Boolean) {
        mSmartRefreshLayout?.apply {
            setEnableRefresh(refreshEnable)
            setEnableLoadMore(loadMoreEnable)
        }
    }
}