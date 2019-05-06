package com.rod.halo.refersh.scene

import com.rod.halo.refersh.abs.RefreshLayoutAdapter

/**
 *
 * @author Rod
 * @date 2018/12/15
 */
interface RefreshScene {

    fun setRefreshAble(refreshLayoutAdapter: RefreshLayoutAdapter?)

    fun needRefresh(manual: Boolean): Boolean

    fun onRefreshSuccess()
}