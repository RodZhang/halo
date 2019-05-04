package com.rod.halo.statusview

import android.view.View

/**
 *
 * @author Rod
 * @date 2019/5/5
 */
class ContentView(private val view: View) : StatusView {

    override fun getId() = ViewStatus.CONTENT

    override fun getView() = view
}