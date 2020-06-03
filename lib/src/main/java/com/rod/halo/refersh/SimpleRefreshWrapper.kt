package com.rod.halo.refersh

import android.view.View
import android.view.ViewGroup
import com.rod.halo.refersh.abs.RefreshLayoutAdapter
import com.rod.halo.refersh.abs.RefreshMode
import com.rod.halo.refersh.abs.RefreshWrapper
import com.rod.halo.statusview.ContentView
import com.rod.halo.statusview.StatusView
import com.rod.halo.statusview.StatusViewController

/**
 *
 * @author Rod
 * @date 2018/12/15
 */
class SimpleRefreshWrapper internal constructor() : RefreshWrapper {

    companion object {
        private const val TAG = "SimpleRefreshWrapper"
    }

    private lateinit var mRefreshLayoutAdapter: RefreshLayoutAdapter

    private val mStatusViewController = StatusViewController()

    override fun wrapper(refreshLayoutAdapter: RefreshLayoutAdapter, contentView: View) {
        mRefreshLayoutAdapter = refreshLayoutAdapter
        val refreshLayout = refreshLayoutAdapter.getRefreshLayout(contentView.context)
        mRefreshLayoutAdapter.setMode(RefreshMode.DISABLE)
        replaceView(contentView, refreshLayout)
    }


    private fun replaceView(contentView: View, refreshLayout: ViewGroup) {
        val parent = contentView.parent as ViewGroup
        val index = parent.indexOfChild(contentView)
        parent.removeView(contentView)

        mRefreshLayoutAdapter.setContentView(contentView)
        with(mStatusViewController) {
            setHost(parent, index)
            putContentView(ContentView(refreshLayout))
        }
    }

    override fun getWrapperView(): View {
        checkNotNull(mRefreshLayoutAdapter) { "call wrapper at first" }
        return mRefreshLayoutAdapter.getRefreshLayout()!!
    }

    override fun setStatusView(statusViews: ArrayList<StatusView>) {
        statusViews.forEach { mStatusViewController.putStatusView(it) }
    }

    override fun showStatusView(id: String) {
        mStatusViewController.showStatusView(id)
    }

}