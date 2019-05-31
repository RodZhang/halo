package com.rod.halo.simple.refresh.statusview

import android.content.Context
import android.view.View
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

    private var mView: View? = null

    override fun initViewInner() {
        mView = with(context) {
            frameLayout {
                textView("请求服务器失败").lparams(wrapContent, wrapContent, android.view.Gravity.CENTER)
            }
        }
    }

    override fun setViewInner(view: View) {
        mView = view
    }

    override fun getId() = ViewStatus.SERVER_ERR

    override fun getView() = mView

    override fun getViewType() = StatusView.VIEW_TYPE_UN_REUSEABLE
}