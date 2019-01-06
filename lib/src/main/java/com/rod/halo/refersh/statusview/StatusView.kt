package com.rod.halo.refersh.statusview

import android.view.View

/**
 *
 * @author Rod
 * @date 2019/1/1
 */
interface StatusView {

    fun setContainerInfo(containerInfo: ContainerInfo)

    fun attachToContainer()

    fun detachFromContainer()

    fun setOnViewClickListener(listener: OnViewClickListener?)

    fun getView(): View?

    fun getFlag(): StatusFlag

    enum class StatusFlag {
        LOADING, NORMAL, EMPTY, NETWORK_ERR, SERVICE_ERR
    }

    interface OnViewClickListener {
        fun onClickView()
    }
}