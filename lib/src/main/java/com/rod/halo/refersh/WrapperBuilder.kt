package com.rod.halo.refersh

import android.view.View
import com.rod.halo.refersh.abs.RefreshCallback
import com.rod.halo.refersh.abs.RefreshWrapper
import com.rod.halo.refersh.scene.RefreshScene
import com.rod.halo.refersh.statusview.ContentView
import com.rod.halo.refersh.statusview.StatusView

/**
 *
 * @author Rod
 * @date 2018/12/15
 */
class WrapperBuilder private constructor(private val mRefreshView: View) {

    companion object {
        fun newInstance(refreshView: View) = WrapperBuilder(refreshView)
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

    fun <T: RefreshWrapper> build(cls: Class<T>): T {
        val refreshWrapper = cls.newInstance()
        with(refreshWrapper) {
            wrapper(mRefreshView)
            setRefreshScene(mRefreshSceneList)

            mStatusViewList.add(ContentView(mRefreshView))
            setStatusView(mStatusViewList)
            this.setRefreshCallback(mRefreshCallback)
        }

        return refreshWrapper
    }
}