package com.rod.halo.refersh.statusview

/**
 *
 * @author Rod
 * @date 2019/1/1
 */
interface StatusView {

    fun setContainerInfo(containerInfo: ContainerInfo)

    fun show()

    fun hide()

    fun setOnViewClickListener(listener: OnViewClickListener?)

    interface OnViewClickListener {
        fun onClickView()
    }
}