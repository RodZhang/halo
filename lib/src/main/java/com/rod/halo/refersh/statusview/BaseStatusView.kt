package com.rod.halo.refersh.statusview

import android.view.View
import android.view.ViewGroup
import com.rod.halo.utils.RL
import com.rod.halo.utils.UIUtil

/**
 *
 * @author Rod
 * @date 2019/1/1
 */
abstract class BaseStatusView : StatusView {

    protected var mViewClickListener: StatusView.OnViewClickListener? = null
    protected var mView: View? = null
    protected var mContainerInfo: ContainerInfo? = null

    override fun setContainerInfo(containerInfo: ContainerInfo) {
        mContainerInfo = containerInfo
    }

    override fun attachToContainer() {
        if (mContainerInfo == null || !mContainerInfo!!.isValid()) {
            RL.e(getTag(), "attachToContainer, mContainerInfo is invalid")
            return
        }
        val containerInfo = checkNotNull(mContainerInfo)
        if (mView == null) {
            mView = createView(containerInfo.container)
        }

        val view = checkNotNull(mView)
        UIUtil.removeFromParent(view)
        containerInfo.container.addView(view, containerInfo.index)
    }

    override fun detachFromContainer() {
        UIUtil.removeFromParent(mView)
    }

    override fun setOnViewClickListener(listener: StatusView.OnViewClickListener?) {
        mViewClickListener = listener
    }

    override fun getView() = mView

    abstract fun createView(container: ViewGroup): View

    abstract fun getTag(): String
}