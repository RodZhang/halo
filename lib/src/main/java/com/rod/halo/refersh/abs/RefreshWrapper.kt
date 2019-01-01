package com.rod.halo.refersh.abs

import android.view.View
import com.rod.halo.refersh.scene.RefreshScene

/**
 *
 * @author Rod
 * @date 2018/12/15
 */
interface RefreshWrapper : RefreshAble {

    fun wrapper(viewNeedRefresh: View)

    fun setRefreshScene(refreshScenes: ArrayList<RefreshScene>)

    fun setRefreshCallback(refreshCallback: RefreshCallback?)

    fun onRefreshSuccess()
}