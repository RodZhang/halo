package com.rod.halo.refersh.scene

import com.rod.halo.refersh.abs.RefreshLayoutAdapter

/**
 *
 * @author Rod
 * @date 2018/12/15
 */
abstract class BaseRefreshScene : RefreshScene {

    private var mRefreshLayoutAdapter: RefreshLayoutAdapter? = null

    override fun setRefreshAble(refreshLayoutAdapter: RefreshLayoutAdapter?) {
        mRefreshLayoutAdapter = refreshLayoutAdapter
    }

    override fun onRefreshSuccess() {

    }

    protected fun refresh() {
        mRefreshLayoutAdapter?.refresh(false)
    }

}