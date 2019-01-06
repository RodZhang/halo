package com.rod.halo.refersh.statusview

import android.view.View
import android.view.ViewGroup

/**
 *
 * @author Rod
 * @date 2019/1/6
 */
internal class ContentView(private val content: View) : BaseStatusView() {

    companion object {
        const val TAG = "ContentView"
    }

    override fun createView(container: ViewGroup): View {
        return content
    }

    override fun getTag() = TAG

    override fun getFlag() = StatusView.StatusFlag.NORMAL
}