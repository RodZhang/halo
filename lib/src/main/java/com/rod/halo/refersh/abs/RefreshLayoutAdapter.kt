package com.rod.halo.refersh.abs

import android.content.Context
import android.view.View
import android.view.ViewGroup

/**
 *
 * @author Rod
 * @date 2018/12/15
 */
interface RefreshLayoutAdapter {

    fun getRefreshLayout(context: Context): ViewGroup

    fun getRefreshLayout(): ViewGroup?

    fun setContentView(view: View)

    fun setMode(@IARefreshMode refreshMode: String)

    fun disableContentWhenRefresh(disable: Boolean)

    fun finishRefresh(success: Boolean, hasMore: Boolean)

    fun finishLoadMore(success: Boolean, hasMore: Boolean)
}