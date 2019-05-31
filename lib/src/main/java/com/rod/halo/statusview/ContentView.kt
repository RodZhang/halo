package com.rod.halo.statusview

import android.view.View

/**
 *
 * @author Rod
 * @date 2019/5/5
 */
class ContentView(private val view: View) : BaseStatusView() {

    override fun initViewInner() {
    }

    override fun setViewInner(view: View) {
    }

    override fun getId() = ViewStatus.CONTENT

    override fun getView() = view

    override fun getViewType() = StatusView.VIEW_TYPE_UN_REUSEABLE
}