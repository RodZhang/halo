package com.rod.halo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ArrayAdapter
import android.widget.Toast
import com.rod.halo.refersh.NetworkChangeScene
import com.rod.halo.refersh.SmartRefreshWrapperBuilder
import com.rod.halo.refersh.TimerScene
import com.rod.halo.refersh.abs.RefreshWrapper
import com.rod.halo.refersh.abs.RefreshWrapperCallback
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), RefreshWrapperCallback {

    private lateinit var mAdapter: ArrayAdapter<String>
    private lateinit var mRefreshWrapper: RefreshWrapper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAdapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                android.R.id.text1, (0 until 100).map { "item $it" })
        list_view.adapter = mAdapter

        mRefreshWrapper = SmartRefreshWrapperBuilder.newInstance(list_view)
                .putRefreshScene(TimerScene(this, 10000))
                .putRefreshScene(NetworkChangeScene(this))
                .setRefreshCallback(this)
                .build()
        mRefreshWrapper.refresh(true)
    }

    override fun startRefresh() {
        Toast.makeText(this, "startRefresh", Toast.LENGTH_SHORT).show()
        mRefreshWrapper.onRefreshSuccess()
    }
}
