package com.rod.halo.statusview

import android.os.Bundle
import android.view.View
import android.view.ViewGroup

/**
 *
 * @author Rod
 * @date 2019/5/4
 */
interface StatusView {

    /**
     * 唯一标志一个状态
     * @see ViewStatus 已定义的状态，用户可以在自己的module中扩展，但要注意不要有重复
     */
    fun getId(): String

    /**
     * 初始化view，可以使用inflate从布局文件加载，也可以使用 new 或者像 anko 的方式创建
     */
    fun initView(parent: ViewGroup)

    /**
     * 另一种初始化view的方式，用于复用
     * 主要考虑这种情况，如：状态中有多种状态，但是使用的是相同的布局文件
     * 如果每个状态都去 inflate 相同的布局，会造成一定的浪费
     * 所以这时在使用时如果判断到已经有加载过相同的布局，可以直接使用 setView 方法
     */
    fun setView(view: View)

    /**
     * 获取加载的 view
     */
    fun getView(): View?

    fun isInited(): Boolean

    fun onVisibleChange(visibleToUser: Boolean, data: Bundle? = null)

    /**
     * 定义 view 的类型，用户复用逻辑，可以以 layout id，或者自己定义的不重复的 type 值
     */
    fun getViewType(): Int

    companion object {
        /**
         * 不可复用的view type
         */
        const val VIEW_TYPE_UN_REUSEABLE: Int = -1
    }
}

abstract class BaseStatusView : StatusView {
    private var mIsInited: Boolean = false
    private var mView: View? = null

    override fun initView(parent: ViewGroup) {
        if (mIsInited) {
            throw IllegalStateException("initView, has been inited")
        }
        val view = onCreateView(parent)
        mView = view
        onViewCreated(view)
        mIsInited = true
    }

    override fun setView(view: View) {
        if (mIsInited) {
            throw IllegalStateException("setView, has been inited")
        }

        mView = view
        onViewCreated(view)
        mIsInited = true
    }

    override fun isInited(): Boolean {
        return mIsInited
    }

    override fun getView(): View? {
        return mView
    }

    override fun onVisibleChange(visibleToUser: Boolean, data: Bundle?) {
    }

    abstract fun onCreateView(parent: ViewGroup): View

    abstract fun onViewCreated(view: View)
}