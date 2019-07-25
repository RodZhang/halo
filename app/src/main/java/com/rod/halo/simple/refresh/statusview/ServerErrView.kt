package com.rod.halo.simple.refresh.statusview

import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.rod.halo.statusview.BaseStatusView
import com.rod.halo.statusview.StatusView
import com.rod.halo.statusview.ViewStatus
import org.jetbrains.anko.frameLayout
import org.jetbrains.anko.textView
import org.jetbrains.anko.wrapContent

/**
 *
 * @author Rod
 * @date 2019/5/5
 */
class ServerErrView(private val context: Context) : BaseStatusView() {

    override fun onCreateView(parent: ViewGroup): View {
        return with(context) {
            frameLayout {
                textView("请求服务器失败").lparams(wrapContent, wrapContent, android.view.Gravity.CENTER)
            }
        }
    }

    override fun onViewCreated(view: View) {
    }

    override fun getId() = ViewStatus.SERVER_ERR

    override fun getViewType() = StatusView.VIEW_TYPE_UN_REUSEABLE
}