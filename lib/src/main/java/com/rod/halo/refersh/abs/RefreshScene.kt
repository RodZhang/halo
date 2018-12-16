package com.rod.halo.refersh.abs

/**
 *
 * @author Rod
 * @date 2018/12/15
 */
interface RefreshScene {

    fun setRefreshAble(refreshAble: RefreshAble?)

    fun needRefresh(manual: Boolean): Boolean

    fun onRefreshSuccess()
}