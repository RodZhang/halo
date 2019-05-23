package com.rod.halo.refersh.abs

import android.view.View
import com.rod.halo.statusview.StatusView

/**
 *
 * @author Rod
 * @date 2018/12/15
 */
interface RefreshWrapper {

    fun wrapper(refreshLayoutAdapter: RefreshLayoutAdapter, contentView: View)

    fun getWrapperView(): View

    fun setStatusView(statusViews: ArrayList<StatusView>)

    fun showStatusView(id: String)

}