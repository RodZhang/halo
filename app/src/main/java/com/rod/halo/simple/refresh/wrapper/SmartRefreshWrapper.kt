package com.rod.halo.simple.refresh.wrapper

import android.util.Log
import android.view.View
import android.view.ViewGroup
import com.rod.halo.refersh.abs.RefreshCallback
import com.rod.halo.refersh.abs.RefreshWrapper
import com.rod.halo.refersh.scene.RefreshScene
import com.rod.halo.refersh.statusview.ContainerInfo
import com.rod.halo.refersh.statusview.StatusView
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
    private val mStatusView: ArrayList<StatusView> = ArrayList()
    private var mCurrentStatusView: StatusView? = null
    private lateinit var mRefreshLayout: SmartRefreshLayout
    private var mRefreshCallback: RefreshCallback? = null
    private var mContainerInfo: ContainerInfo? = null

    override fun wrapper(viewNeedRefresh: View) {
        val refreshLayout = initRefreshLayout(viewNeedRefresh)
        replaceView(viewNeedRefresh, refreshLayout)
        mRefreshLayout = refreshLayout
    }

    private fun initRefreshLayout(viewNeedRefresh: View): SmartRefreshLayout {
        val refreshLayout = SmartRefreshLayout(viewNeedRefresh.context)
        refreshLayout.setDisableContentWhenLoading(true)
        refreshLayout.setDisableContentWhenRefresh(true)
        refreshLayout.setOnRefreshListener { refresh(true) }
        return refreshLayout
    }

    private fun replaceView(viewNeedRefresh: View, refreshLayout: SmartRefreshLayout) {
        val parent = viewNeedRefresh.parent as ViewGroup
        val indexOfRefreshView = parent.indexOfChild(viewNeedRefresh)
        mContainerInfo = ContainerInfo(parent, indexOfRefreshView)
        parent.removeView(viewNeedRefresh)
        refreshLayout.setRefreshContent(viewNeedRefresh)
        parent.addView(refreshLayout, indexOfRefreshView)
    }

    override fun getWrapperView(): View {
        checkNotNull(mRefreshLayout) { "call wrapper at first" }
        return mRefreshLayout
    }

    override fun setRefreshScene(refreshScenes: ArrayList<RefreshScene>) {
        mRefreshScenes.forEach { it.setRefreshAble(null) }
        mRefreshScenes.clear()
        mRefreshScenes.addAll(refreshScenes)
        mRefreshScenes.forEach { it.setRefreshAble(this) }
    }

    override fun setStatusView(statusViews: ArrayList<StatusView>) {
        // TODO: 需要判断
        // TODO: 需要考虑下这里的container和index 应该如何合理的获取到
        val containerInfo = mContainerInfo
        if (containerInfo != null) {
            statusViews.asSequence().forEach { it.setContainerInfo(containerInfo) }
        }
        mStatusView.addAll(statusViews)
    }

    override fun refresh(manual: Boolean) {
        // TODO: if is refreshing, do something
        if (canRefresh(manual)) {
            showStatus(StatusView.StatusFlag.LOADING)
            mRefreshCallback?.startRefresh()
        } else {
            // TODO: find out why can not refresh and show specific view
        }
    }

    override fun setRefreshCallback(refreshCallback: RefreshCallback?) {
        mRefreshCallback = refreshCallback
    }

    override fun onRefreshSuccess() {
        mRefreshScenes.forEach { it.onRefreshSuccess() }
        showStatus(StatusView.StatusFlag.NORMAL)
        mRefreshLayout?.finishRefresh()
    }

    override fun onRefreshError(status: StatusView.StatusFlag, reason: Int) {
        // TODO: check current view and decide to switch view
        showStatus(status)
    }

    override fun showStatus(statusFlag: StatusView.StatusFlag) {
        mCurrentStatusView?.detachFromContainer()
        mCurrentStatusView = mStatusView.find { it.getFlag() == statusFlag }
        mCurrentStatusView?.attachToContainer()
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