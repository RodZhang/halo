package com.rod.halo.utils

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager

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

    fun showKeyboard(view: View?) {
        view?.run {
            (context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).showSoftInput(view, 0)
        }
    }
}