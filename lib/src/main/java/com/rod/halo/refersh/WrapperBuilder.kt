package com.rod.halo.refersh

import android.view.View
import com.rod.halo.refersh.abs.RefreshCallback
import com.rod.halo.refersh.abs.RefreshLayoutAdapter
import com.rod.halo.refersh.abs.RefreshWrapper
import com.rod.halo.refersh.scene.RefreshScene
import com.rod.halo.statusview.StatusView

/**
 *
 * @author Rod
 * @date 2018/12/15
 */
class WrapperBuilder private constructor(
        private val mRefreshView: View,
        private val mRefreshLayoutAdapterClass: Class<out RefreshLayoutAdapter>
) {

    companion object {
        fun newInstance(refreshView: View, cls: Class<out RefreshLayoutAdapter>) = WrapperBuilder(refreshView, cls)
    }

    private val mRefreshSceneList = ArrayList<RefreshScene>()
    private val mStatusViewList = ArrayList<StatusView>()
    private var mRefreshCallback: RefreshCallback? = null

    fun putRefreshScene(refreshScene: RefreshScene): WrapperBuilder {
        mRefreshSceneList.add(refreshScene)
        return this
    }

    fun putStatusView(statusView: StatusView): WrapperBuilder {
        mStatusViewList.add(statusView)
        return this
    }

    fun setRefreshCallback(callback: RefreshCallback?): WrapperBuilder {
        mRefreshCallback = callback
        return this
    }

    fun <T : RefreshWrapper> build(cls: Class<T>): T = cls.newInstance().apply {
        wrapper(mRefreshLayoutAdapterClass.newInstance(), mRefreshView)
        setRefreshScene(mRefreshSceneList)
        setStatusView(mStatusViewList)
        setRefreshCallback(mRefreshCallback)
    }
}