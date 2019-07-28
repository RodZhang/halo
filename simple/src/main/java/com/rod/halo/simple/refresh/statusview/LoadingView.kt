package com.rod.halo.simple.refresh.statusview

import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import com.rod.halo.simple.R
import com.rod.halo.statusview.BaseStatusView
import com.rod.halo.statusview.ViewStatus
import kotlinx.android.synthetic.main.common_loading_view.view.*
import org.jetbrains.anko.layoutInflater

/**
 *
 * @author Rod
 * @date 2019-07-24
 */
class LoadingView() : BaseStatusView() {

    companion object {
        private val LAYOUT = R.layout.common_loading_view
    }

    private lateinit var mProgressBar: ProgressBar

    override fun onCreateView(parent: ViewGroup): View {
        return parent.context.layoutInflater.inflate(LAYOUT, parent, false)
    }

    override fun onViewCreated(view: View) {
        mProgressBar = view.progressBar
    }

    override fun getId(): String {
        return ViewStatus.LOADING
    }

    override fun getViewType(): Int {
        return LAYOUT
    }
}