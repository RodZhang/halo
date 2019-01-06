package com.rod.halo.refersh.abs

import android.view.View
import com.rod.halo.refersh.scene.RefreshScene
import com.rod.halo.refersh.statusview.StatusView

/**
 *
 * @author Rod
 * @date 2018/12/15
 */
interface RefreshWrapper : RefreshAble {

    fun wrapper(viewNeedRefresh: View)

    fun getWrapperView(): View?

    fun setRefreshScene(refreshScenes: ArrayList<RefreshScene>)

    fun setStatusView(statusViews: ArrayList<StatusView>)

    fun setRefreshCallback(refreshCallback: RefreshCallback?)

    fun onRefreshSuccess()

    fun onRefreshError(status: StatusView.StatusFlag, reason: Int)

    fun showStatus(statusFlag: StatusView.StatusFlag)
}