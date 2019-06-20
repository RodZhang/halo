package com.rod.halo.utils

import android.app.Activity
import android.app.Application
import android.content.ComponentCallbacks
import android.content.res.Configuration

/**
 *
 * @author Rod
 * @date 2019-06-20
 */
object DensityUtil {
    private const val TAG = "DensityUtil"
    private const val DESIGN_SCREEN_WIDTH = 360F

    private var mNoncompatDensity = 0F
    private var mNoncompatDensityScaleDensity = 0F

    @JvmStatic
    fun setCustomDensity(activity: Activity, app: Application) {
        val appDisplayMetrics = app.resources.displayMetrics

        if (mNoncompatDensity == 0F) {
            mNoncompatDensity = appDisplayMetrics.density
            mNoncompatDensityScaleDensity = appDisplayMetrics.scaledDensity
            app.registerComponentCallbacks(object : ComponentCallbacks {
                override fun onLowMemory() {
                }

                override fun onConfigurationChanged(newConfig: Configuration?) {
                    newConfig?.run {
                        HL.d(TAG, "onConfigurationChanged->fontScale=$fontScale")
                        mNoncompatDensityScaleDensity = appDisplayMetrics.scaledDensity
                    }
                }
            })
        }

        val minSidePixels = Math.min(appDisplayMetrics.widthPixels, appDisplayMetrics.heightPixels)
        val targetDensity = minSidePixels / DESIGN_SCREEN_WIDTH
        val targetScaleDensity = targetDensity * (mNoncompatDensityScaleDensity / mNoncompatDensity)
        val targetDensityDpi: Int = (160 * targetDensity).toInt()
        appDisplayMetrics.density = targetDensity
        appDisplayMetrics.densityDpi = targetDensityDpi
        appDisplayMetrics.scaledDensity = targetScaleDensity

        val activityDisplayMetrics = activity.resources.displayMetrics
        activityDisplayMetrics.density = targetDensity
        activityDisplayMetrics.densityDpi = targetDensityDpi
        activityDisplayMetrics.scaledDensity = targetScaleDensity
    }
}