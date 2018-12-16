package com.rod.halo.refersh

import android.view.View
import com.rod.halo.refersh.abs.RefreshScene
import com.rod.halo.refersh.abs.RefreshWrapperCallback

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
    private var mRefreshWrapperCallback: RefreshWrapperCallback? = null

    fun putRefreshScene(refreshScene: RefreshScene): SmartRefreshWrapperBuilder {
        mRefreshSceneList.add(refreshScene)
        return this
    }

    fun setRefreshCallback(callback: RefreshWrapperCallback?): SmartRefreshWrapperBuilder {
        mRefreshWrapperCallback = callback
        return this
    }

    fun build(): SmartRefreshWrapper {
        val smartRefreshWrapper = SmartRefreshWrapper()
        with(smartRefreshWrapper) {
            wrapper(mRefreshView)
            setRefreshScene(mRefreshSceneList)
            this.setRefreshCallback(mRefreshWrapperCallback)
        }

        return smartRefreshWrapper
    }
}