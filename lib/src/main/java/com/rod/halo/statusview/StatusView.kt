package com.rod.halo.statusview

import android.view.View
import android.view.ViewGroup

/**
 *
 * @author Rod
 * @date 2019/5/4
 */
interface StatusView {

    fun onAttach(parent: ViewGroup) {}

    fun onDetach(parent: ViewGroup) {}

    fun getId(): String

    fun getView(): View
}