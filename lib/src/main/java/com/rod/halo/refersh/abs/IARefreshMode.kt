package com.rod.halo.refersh.abs

import android.support.annotation.StringDef

/**
 * @author Rod
 * @date 2019/5/7
 */
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.FIELD, AnnotationTarget.CONSTRUCTOR,
        AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER,
        AnnotationTarget.PROPERTY_SETTER, AnnotationTarget.VALUE_PARAMETER)
@StringDef(RefreshMode.DISABLE, RefreshMode.PULL_DOWN_ONLY, RefreshMode.PULL_UP_ONLY, RefreshMode.BOTH)
annotation class IARefreshMode
