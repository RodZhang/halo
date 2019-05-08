package com.rod.halo.simple

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import com.rod.halo.refersh.WrapperBuilder
import com.rod.halo.refersh.abs.RefreshCallback
import com.rod.halo.refersh.abs.RefreshWrapper
import com.rod.halo.simple.refresh.SmartRefreshLayoutAdapter
import com.rod.halo.simple.refresh.scene.NetworkChangeScene
import com.rod.halo.simple.refresh.scene.TimerScene
import com.rod.halo.simple.refresh.statusview.EmptyView
import com.rod.halo.simple.refresh.statusview.LoadingView
import com.rod.halo.simple.refresh.statusview.NetworkErrView
import com.rod.halo.simple.refresh.statusview.ServerErrView
import com.rod.halo.refersh.SmartRefreshWrapper
import com.rod.halo.statusview.ViewStatus
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), RefreshCallback {

    private lateinit var mAdapter: ArrayAdapter<String>
    private lateinit var mRefreshWrapper: RefreshWrapper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1,
                android.R.id.text1, ArrayList<String>())
        list_view.adapter = mAdapter
        list_view.setOnItemClickListener { _, _, position, id ->
            Toast.makeText(this, "click item $position, $id", Toast.LENGTH_SHORT).show()
        }

        mRefreshWrapper = WrapperBuilder.newInstance(list_view, SmartRefreshLayoutAdapter::class.java)
                .putRefreshScene(TimerScene(this, 10000))
                .putRefreshScene(NetworkChangeScene(this))
                .putStatusView(NetworkErrView(this))
                .putStatusView(ServerErrView(this))
                .putStatusView(EmptyView(this))
                .putStatusView(LoadingView(this))
                .setRefreshCallback(this)
                .build(SmartRefreshWrapper::class.java)
//        container.setOnClickListener { mRefreshWrapper.refresh(true) }
        startRefresh()
    }

    override fun startRefresh() {
        if (mAdapter.isEmpty) {
            mRefreshWrapper.showStatusView(ViewStatus.LOADING)
        }
        container.postDelayed({
            mAdapter.addAll((0 until 100).mapIndexed { index, _ -> "item $index" })
            mRefreshWrapper.onRefreshSuccess()
            Log.d("MainActivity", "refresh success")
        }, 3000)
    }
}
