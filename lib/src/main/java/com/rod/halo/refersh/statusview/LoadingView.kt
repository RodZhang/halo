package com.rod.halo.refersh.statusview

import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import org.jetbrains.anko.frameLayout
import org.jetbrains.anko.textView
import org.jetbrains.anko.wrapContent

/**
 *
 * @author Rod
 * @date 2019/1/7
 */
class LoadingView : BaseStatusView() {

    companion object {
        const val TAG = "LoadingView"
    }

    private var mTextView: TextView? = null
    private var mCount = 0
    private val mLoadingRunnable = Runnable {
        val dotCount = mCount++ % 3 + 1
        mTextView?.text = "Loading " + getDot(dotCount)
        postChangeText()
    }

    private fun getDot(count: Int): String {
        var result = ""
        (0 until count).forEach { _ -> result += "." }
        return result
    }

    override fun createView(container: ViewGroup): View {
        return with(container) {
            frameLayout {
                mTextView = textView().lparams(wrapContent, wrapContent, Gravity.CENTER)
            }
        }
    }

    override fun attachToContainer() {
        super.attachToContainer()
        postChangeText()
    }

    override fun detachFromContainer() {
        super.detachFromContainer()
        mTextView?.removeCallbacks(mLoadingRunnable)
    }

    private fun postChangeText() {
        mTextView?.postDelayed(mLoadingRunnable, 333)
    }

    override fun getTag() = TAG

    override fun getFlag() = StatusView.StatusFlag.LOADING
}