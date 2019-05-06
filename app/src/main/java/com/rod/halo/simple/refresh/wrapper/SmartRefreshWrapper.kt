package com.rod.halo.simple.refresh.wrapper

import android.util.Log
import android.view.View
import android.view.ViewGroup
import com.rod.halo.refersh.abs.RefreshCallback
import com.rod.halo.refersh.abs.RefreshWrapper
import com.rod.halo.refersh.scene.RefreshScene
import com.rod.halo.statusview.ContentView
import com.rod.halo.statusview.StatusView
import com.rod.halo.statusview.StatusViewController
import com.rod.halo.statusview.ViewStatus
import com.scwang.smartrefresh.layout.SmartRefreshLayout

/**
 *
 * @author Rod
 * @date 2018/12/15
 */
class SmartRefreshWrapper internal constructor() : RefreshWrapper {

    companion object {
        private const val TAG = "SmartRefreshWrapper"
    }

    /**
     * 刷新场景
     * 每种类型的刷新场景应该最多只有一个
     */
    private val mRefreshScenes: ArrayList<RefreshScene> = ArrayList()
    private lateinit var mRefreshLayout: SmartRefreshLayout
    private var mRefreshCallback: RefreshCallback? = null

    private val mStatusViewController = StatusViewController()

    override fun wrapper(viewNeedRefresh: View) {
        val refreshLayout = initRefreshLayout(viewNeedRefresh)
        replaceView(viewNeedRefresh, refreshLayout)
        mRefreshLayout = refreshLayout
    }

    private fun initRefreshLayout(viewNeedRefresh: View): SmartRefreshLayout {
        val refreshLayout = SmartRefreshLayout(viewNeedRefresh.context)
        refreshLayout.setDisableContentWhenLoading(true)
        refreshLayout.setDisableContentWhenRefresh(true)
//        refreshLayout.setOnRefreshListener { refresh(true) }
        return refreshLayout
    }

    private fun replaceView(viewNeedRefresh: View, refreshLayout: SmartRefreshLayout) {
        val parent = viewNeedRefresh.parent as ViewGroup
        val indexOfRefreshView = parent.indexOfChild(viewNeedRefresh)
        parent.removeView(viewNeedRefresh)

        refreshLayout.setRefreshContent(viewNeedRefresh)
        with(mStatusViewController) {
            setHost(parent, indexOfRefreshView)
            putStatusView(ContentView(refreshLayout))
        }
    }

    override fun getWrapperView(): View {
        checkNotNull(mRefreshLayout) { "call wrapper at first" }
        return mRefreshLayout
    }

    override fun setRefreshScene(refreshScenes: ArrayList<RefreshScene>) {
//        mRefreshScenes.forEach { it.setRefreshAble(null) }
//        mRefreshScenes.clear()
//        mRefreshScenes.addAll(refreshScenes)
//        mRefreshScenes.forEach { it.setRefreshAble(this) }
    }

    override fun setStatusView(statusViews: ArrayList<StatusView>) {
        statusViews.asSequence()
                .forEach { mStatusViewController.putStatusView(it) }
    }

//    override fun refresh(manual: Boolean) {
//        // TODO: if is refreshing, do something
//        if (canRefresh(manual)) {
//            mRefreshCallback?.startRefresh()
//        } else {
//            // TODO: find out why can not refresh and show specific view
//        }
//    }

    override fun setRefreshCallback(refreshCallback: RefreshCallback?) {
        mRefreshCallback = refreshCallback
    }

    override fun showStatusView(id: String) {
        mStatusViewController.showStatusView(id)
    }

    override fun onRefreshSuccess() {
        mRefreshScenes.forEach { it.onRefreshSuccess() }
        showStatusView(ViewStatus.CONTENT)
        mRefreshLayout.finishRefresh()
    }

    private fun canRefresh(manual: Boolean): Boolean {
        Log.d(TAG, "canRefresh, manual=$manual")
        if (mRefreshScenes.isEmpty()) {
            Log.d(TAG, "canRefresh, return true, scenes is empty")
            return true
        }
        for (scene in mRefreshScenes) {
            if (scene.needRefresh(manual)) {
                Log.d(TAG, "canRefresh needRefresh=true, scene:$scene")
                return true
            }
            Log.d(TAG, "canRefresh, needRefresh=false, scene:$scene")
        }
        Log.d(TAG, "canRefresh, return false")
        return false
    }
}