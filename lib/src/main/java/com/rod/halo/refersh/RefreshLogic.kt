package com.rod.halo.refersh

/**
 *
 * @author Rod
 * @date 2019-05-24
 */
interface RefreshLogic {

    /**
     * 在准备刷新数据之前调用，可用于判断是否能加载数据，以及展示相应的视图
     *
     * @return true: 当前环境可以加载数据； false：当前环境不能加载数据
     */
    fun beforeLoadData(): Boolean

    /**
     * 加载数据之后调用，用于根据数据加载结果来显示对应的视图
     */
    fun afterLoadData(isSuccess: Boolean, isRspDataEmpty: Boolean)

}