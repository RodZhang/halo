package com.rod.halo.simple.refresh.statusview

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.rod.halo.statusview.StatusView
import com.rod.halo.statusview.ViewStatus
import org.jetbrains.anko.frameLayout
import org.jetbrains.anko.textView
import org.jetbrains.anko.wrapContent

/**
 *
 * @author Rod
 * @date 2019/5/5
 */
class LoadingView(private val context: Context) : StatusView {

    private lateinit var mTextView: TextView
    private var mCount = 0
    private val mLoadingRunnable = Runnable {
        val dotCount = mCount++ % 3 + 1
        mTextView.text = "Loading ${getDot(dotCount)}"
        postChangeText()
    }
    private val mView: View by lazy {
        with(context) {
            frameLayout {
                mTextView = textView().lparams(wrapContent, wrapContent, android.view.Gravity.CENTER)
            }
        }
    }

    override fun onAttach(parent: ViewGroup) {
        postChangeText()
    }

    override fun onDetach(parent: ViewGroup) {
        mTextView.removeCallbacks(mLoadingRunnable)
    }

    override fun getId() = ViewStatus.LOADING

    override fun getView() = mView

    private fun getDot(count: Int): String {
        var result = ""
        (0 until count).forEach { _ -> result += "." }
        return result
    }

    private fun postChangeText() {
        mTextView.postDelayed(mLoadingRunnable, 333)
    }
}