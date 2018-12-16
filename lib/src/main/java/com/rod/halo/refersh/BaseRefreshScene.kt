package com.rod.halo.refersh

import com.rod.halo.refersh.abs.RefreshAble
import com.rod.halo.refersh.abs.RefreshScene

/**
 *
 * @author Rod
 * @date 2018/12/15
 */
abstract class BaseRefreshScene : RefreshScene {

    private var mRefreshAble: RefreshAble? = null

    override fun setRefreshAble(refreshAble: RefreshAble?) {
        mRefreshAble = refreshAble
    }

    override fun onRefreshSuccess() {

    }

    protected fun refresh() {
        mRefreshAble?.refresh(false)
    }

}