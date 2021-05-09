package com.rod.halo.simple.refresh.statusview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rod.halo.simple.R
import com.rod.halo.statusview.BaseStatusView
import com.rod.halo.statusview.StatusView
import com.rod.halo.statusview.ViewStatus

/**
 *
 * @author Rod
 * @date 2019/5/5
 */
class ServerErrView(private val context: Context) : BaseStatusView() {

    override fun onCreateView(parent: ViewGroup): View {
        return LayoutInflater.from(context).inflate(R.layout.state_server_error, parent, false)
    }

    override fun onViewCreated(view: View) {
    }

    override fun getId() = ViewStatus.SERVER_ERR

    override fun getViewType() = StatusView.VIEW_TYPE_UN_REUSEABLE
}