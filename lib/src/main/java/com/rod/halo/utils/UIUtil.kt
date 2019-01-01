package com.rod.halo.utils

import android.view.View
import android.view.ViewGroup

/**
 *
 * @author Rod
 * @date 2019/1/1
 */
object UIUtil {

    fun removeFromParent(view: View?) {
        if (view != null && view.parent is ViewGroup) {
            val parent = view.parent as ViewGroup
            parent.removeView(view)
        }
    }
}