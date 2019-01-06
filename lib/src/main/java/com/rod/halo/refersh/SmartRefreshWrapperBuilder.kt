package com.rod.halo.refersh

import android.view.View
import com.rod.halo.refersh.abs.RefreshCallback
import com.rod.halo.refersh.scene.RefreshScene
import com.rod.halo.refersh.statusview.ContentView
import com.rod.halo.refersh.statusview.StatusView

/**
 *
 * @author Rod
 * @date 2018/12/15
 */
class SmartRefreshWrapperBuilder private constructor(private val mRefreshView: View) {

    companion object {
        fun newInstance(refreshView: View) = SmartRefreshWrapperBuilder(refreshView)
    }

    private val mRefreshSceneList = ArrayList<RefreshScene>()
    private val mStatusViewList = ArrayList<StatusView>()
    private var mRefreshCallback: RefreshCallback? = null

    fun putRefreshScene(refreshScene: RefreshScene): SmartRefreshWrapperBuilder {
        mRefreshSceneList.add(refreshScene)
        return this
    }

    fun putStatusView(statusView: StatusView): SmartRefreshWrapperBuilder {
        mStatusViewList.add(statusView)
        return this
    }

    fun setRefreshCallback(callback: RefreshCallback?): SmartRefreshWrapperBuilder {
        mRefreshCallback = callback
        return this
    }

    fun build(): SmartRefreshWrapper {
        val smartRefreshWrapper = SmartRefreshWrapper()
        with(smartRefreshWrapper) {
            wrapper(mRefreshView)
            setRefreshScene(mRefreshSceneList)

            mStatusViewList.add(ContentView(mRefreshView))
            setStatusView(mStatusViewList)
            this.setRefreshCallback(mRefreshCallback)
        }

        return smartRefreshWrapper
    }
}