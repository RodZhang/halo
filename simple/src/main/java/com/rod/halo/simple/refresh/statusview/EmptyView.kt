package com.rod.halo.simple.refresh.statusview

import android.support.annotation.StringRes
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.rod.halo.simple.R
import com.rod.halo.statusview.BaseStatusView
import com.rod.halo.statusview.ViewStatus
import kotlinx.android.synthetic.main.common_empty_view.view.*
import org.jetbrains.anko.layoutInflater

/**
 *
 * @author Rod
 * @date 2019-07-23
 */
class EmptyView
@JvmOverloads constructor(
        @StringRes private val mTipResId: Int = R.string.common_empty_tip
) : BaseStatusView() {

    companion object {
        private val LAYOUT = R.layout.common_empty_view
    }

    private var mTipTv: TextView? = null

    override fun onCreateView(parent: ViewGroup): View {
        return parent.context.layoutInflater.inflate(LAYOUT, parent, false)
    }

    override fun onViewCreated(view: View) {
        mTipTv = view.emptyTipTv
        mTipTv?.setText(mTipResId)
    }

    override fun getId(): String {
        return ViewStatus.EMPTY
    }

    override fun getViewType(): Int {
        return LAYOUT
    }

}