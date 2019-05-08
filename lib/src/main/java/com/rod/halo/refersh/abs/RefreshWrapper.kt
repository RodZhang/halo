package com.rod.halo.refersh.abs

import android.view.View
import com.rod.halo.refersh.scene.RefreshScene
import com.rod.halo.statusview.StatusView

/**
 *
 * @author Rod
 * @date 2018/12/15
 */
interface RefreshWrapper {

    fun wrapper(refreshLayoutAdapter: RefreshLayoutAdapter, viewNeedRefresh: View)

    fun getWrapperView(): View

    fun setRefreshScene(refreshScenes: ArrayList<RefreshScene>)

    fun setStatusView(statusViews: ArrayList<StatusView>)

    fun setRefreshCallback(refreshCallback: RefreshCallback?)

    fun showStatusView(id: String)

    fun onRefreshSuccess()
}