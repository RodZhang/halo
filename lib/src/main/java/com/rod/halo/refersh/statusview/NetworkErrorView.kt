package com.rod.halo.refersh.statusview

import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import org.jetbrains.anko.frameLayout
import org.jetbrains.anko.textView
import org.jetbrains.anko.wrapContent

/**
 *
 * @author Rod
 * @date 2019/1/1
 */
class NetworkErrorView : BaseStatusView() {

    companion object {
        const val TAG = "NetworkErrorView"
    }

    override fun createView(container: ViewGroup): View {
        return with(container) {
            frameLayout {
                textView("网络不可用，请检查网络连接后再试...")
                        .lparams(wrapContent, wrapContent, Gravity.CENTER)
            }
        }
    }

    override fun getTag() = TAG

    override fun getFlag() = StatusView.StatusFlag.NETWORK_ERR
}