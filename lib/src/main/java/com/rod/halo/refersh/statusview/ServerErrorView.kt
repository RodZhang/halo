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
class ServerErrorView : BaseStatusView() {

    companion object {
        private const val TAG = "ServerErrorView"
    }

    override fun createView(container: ViewGroup): View {
        return with(container) {
            frameLayout {
                textView("请求服务器失败")
                        .lparams(wrapContent, wrapContent, Gravity.CENTER)
            }
        }
    }

    override fun getTag() = TAG

    override fun getFlag() = StatusView.StatusFlag.SERVICE_ERR
}