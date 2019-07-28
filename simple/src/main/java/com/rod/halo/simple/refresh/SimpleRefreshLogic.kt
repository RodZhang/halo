package com.rod.halo.simple.refresh

import com.rod.halo.refersh.RefreshLogic
import com.rod.halo.refersh.abs.RefreshWrapper
import com.rod.halo.statusview.ViewStatus
import com.rod.halo.utils.NetworkUtil
import com.rod.halo.utils.ToastUtil

/**
 *
 * @author Rod
 * @date 2019/5/6
 */
class SimpleRefreshLogic(
        private val refreshWrapper: RefreshWrapper,
        private val dataFinder: DataFinder
) : RefreshLogic {

    override fun beforeLoadData(): Boolean {
        if (NetworkUtil.isNetworkEnable()) {
            if (dataFinder.isEmpty()) {
                refreshWrapper.showStatusView(ViewStatus.LOADING)
            }
            return true
        }

        if (dataFinder.isEmpty()) {
            refreshWrapper.showStatusView(ViewStatus.NETWORK_ERR)
        } else {
            ToastUtil.show("网络异常...")
        }
        return false
    }

    override fun afterLoadData(isSuccess: Boolean, isRspDataEmpty: Boolean) {
        if (isSuccess) {
            if (isRspDataEmpty) {
                if (dataFinder.isEmpty()) {
                    refreshWrapper.showStatusView(ViewStatus.EMPTY)
                }
            } else {
                refreshWrapper.showStatusView(ViewStatus.CONTENT)
            }
        } else {
            if (NetworkUtil.isNetworkEnable()) {
                if (dataFinder.isEmpty()) {
                    refreshWrapper.showStatusView(ViewStatus.SERVER_ERR)
                }
            } else {
                if (dataFinder.isEmpty()) {
                    refreshWrapper.showStatusView(ViewStatus.NETWORK_ERR)
                } else {
                    ToastUtil.show("网络异常...")
                }
            }
        }
    }

    interface DataFinder {
        fun isEmpty(): Boolean
    }
}