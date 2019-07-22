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
class EmptyView(private val context: Context) : BaseStatusView() {

    override fun inflateView(): View {
        return with(context) {
            frameLayout {
                textView("暂时没有数据...")
                        .lparams(wrapContent, wrapContent, android.view.Gravity.CENTER)
            }
        }
    }

    override fun initViewInner(view: View) {
    }

    override fun getId() = ViewStatus.EMPTY

    override fun getViewType() = StatusView.VIEW_TYPE_UN_REUSEABLE
}