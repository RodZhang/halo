package com.rod.halo.refersh.abs

import android.view.View

/**
 *
 * @author Rod
 * @date 2018/12/15
 */
interface RefreshLayoutAdapter {

    fun setContentView(view: View)

    fun setMode(@IARefreshMode refreshMode: String)

    fun disableContentWhenRefresh(disable: Boolean)

    fun finishRefresh(success: Boolean, hasMore: Boolean)

    fun finishLoadMore(success: Boolean, hasMore: Boolean)
}