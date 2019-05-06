package com.rod.halo.utils

import android.widget.Toast
import com.rod.halo.BaseApp

/**
 *
 * @author Rod
 * @date 2019/5/6
 */
object ToastUtil {

    fun show(msg: String) {
        Toast.makeText(BaseApp.gContext, msg, Toast.LENGTH_SHORT).show()
    }
}