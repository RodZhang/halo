package com.rod.halo.statusview

import android.view.View
import android.view.ViewGroup

/**
 *
 * @author Rod
 * @date 2019/5/5
 */
class ContentView(private val contentView: View) : BaseStatusView() {

    override fun onCreateView(parent: ViewGroup): View {
        return contentView;
    }

    override fun onViewCreate(view: View) {
    }

    override fun getId() = ViewStatus.CONTENT

    override fun getViewType() = StatusView.VIEW_TYPE_UN_REUSEABLE
}