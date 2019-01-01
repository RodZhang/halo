package com.rod.halo.refersh.statusview

import android.view.ViewGroup

/**
 *
 * @author Rod
 * @date 2019/1/1
 */
data class ContainerInfo(val container: ViewGroup, val index: Int) {

    fun isValid() = index >= 0
}