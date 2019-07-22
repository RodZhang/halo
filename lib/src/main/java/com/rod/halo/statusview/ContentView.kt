package com.rod.halo.statusview

import android.view.View

/**
 *
 * @author Rod
 * @date 2019/5/5
 */
class ContentView(private val contentView: View) : BaseStatusView() {

    override fun inflateView(): View {
        return contentView;
    }

    override fun initViewInner(view: View) {
    }

    override fun getId() = ViewStatus.CONTENT

    override fun getViewType() = StatusView.VIEW_TYPE_UN_REUSEABLE
}