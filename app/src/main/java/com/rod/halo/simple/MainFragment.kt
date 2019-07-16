package com.rod.halo.simple

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rod.annotation.OnClick
import com.rod.api.Halo
import com.rod.halo.utils.ToastUtil

/**
 *
 * @author Rod
 * @date 2019-07-15
 */
class MainFragment : Fragment() {

    companion object {
        const val TAG = "MainFragment"
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Halo.inject(this, view)
    }

    @OnClick(R.id.frag_btn)
    fun onContainerClick(view: View) {
        ToastUtil.show("hahaha")
    }
}