package com.rod.halo.refersh

import android.util.Log
import android.view.View
import android.view.ViewGroup
import com.rod.halo.refersh.abs.RefreshScene
import com.rod.halo.refersh.abs.RefreshWrapper
import com.rod.halo.refersh.abs.RefreshWrapperCallback
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
    private var mRefreshLayout: SmartRefreshLayout? = null
    private var mRefreshCallback: RefreshWrapperCallback? = null

    override fun wrapper(viewNeedRefresh: View) {
        if (mRefreshLayout != null && mRefreshLayout!!.childCount > 0) {
            throw IllegalStateException("view($viewNeedRefresh) has added")
        }

        mRefreshLayout = SmartRefreshLayout(viewNeedRefresh.context)

        val parent = viewNeedRefresh.parent as ViewGroup
        val indexOfRefreshView = parent.indexOfChild(viewNeedRefresh)

        parent.removeView(viewNeedRefresh)
        mRefreshLayout!!.addView(viewNeedRefresh)
        parent.addView(mRefreshLayout, indexOfRefreshView)
    }

    override fun setRefreshScene(refreshScenes: ArrayList<RefreshScene>) {
        mRefreshScenes.forEach { it.setRefreshAble(null) }
        mRefreshScenes.clear()
        mRefreshScenes.addAll(refreshScenes)
        mRefreshScenes.forEach { it.setRefreshAble(this) }
    }

    override fun refresh(manual: Boolean) {
        if (canRefresh(manual)) {
            mRefreshCallback?.startRefresh()
        }
    }

    override fun setRefreshCallback(refreshWrapperCallback: RefreshWrapperCallback?) {
        mRefreshCallback = refreshWrapperCallback
    }

    override fun onRefreshSuccess() {
        mRefreshScenes.forEach { it.onRefreshSuccess() }
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