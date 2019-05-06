package com.rod.halo.simple

import com.rod.halo.refersh.abs.RefreshWrapper
import com.rod.halo.statusview.ViewStatus
import com.rod.halo.utils.NetworkUtil
import com.rod.halo.utils.ToastUtil

/**
 *
 * @author Rod
 * @date 2019/5/6
 */
class RefreshLogic(private val refreshWrapper: RefreshWrapper) {

    fun beforeLoadData(): Boolean {
        if (NetworkUtil.isNetworkEnable()) {
            if (isEmpty()) {
                refreshWrapper.showStatusView(ViewStatus.LOADING)
            }
            return true
        }

        if (isEmpty()) {
            refreshWrapper.showStatusView(ViewStatus.NETWORK_ERR)
        } else {
            ToastUtil.show("网络异常...")
        }
        return false
    }

    fun afterLoadData(isSuccess: Boolean, isRspDataEmpty: Boolean) {
        if (isSuccess) {
            if (isRspDataEmpty) {
                if (isEmpty()) {
                    refreshWrapper.showStatusView(ViewStatus.EMPTY)
                }
            } else {
                refreshWrapper.showStatusView(ViewStatus.CONTENT)
            }
        } else {
            if (NetworkUtil.isNetworkEnable()) {
                if (isEmpty()) {
                    refreshWrapper.showStatusView(ViewStatus.SERVER_ERR)
                }
            } else {
                if (isEmpty()) {
                    refreshWrapper.showStatusView(ViewStatus.NETWORK_ERR)
                } else {
                    ToastUtil.show("网络异常...")
                }
            }
        }
    }

    private fun isEmpty() = true
}