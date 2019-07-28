package com.rod.halo.app

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import com.rod.annotation.OnClick
import com.rod.api.Halo
import com.rod.halo.refersh.RefreshLogic
import com.rod.halo.refersh.SimpleRefreshWrapper
import com.rod.halo.refersh.WrapperBuilder
import com.rod.halo.refersh.abs.RefreshWrapper
import com.rod.halo.simple.refresh.SimpleRefreshLogic
import com.rod.halo.simple.refresh.SmartRefreshLayoutAdapter
import com.rod.halo.simple.refresh.statusview.EmptyView
import com.rod.halo.simple.refresh.statusview.LoadingView
import com.rod.halo.simple.refresh.statusview.NetworkErrView
import com.rod.halo.simple.refresh.statusview.ServerErrView
import com.rod.halo.statusview.ViewStatus
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), SimpleRefreshLogic.DataFinder {

    private lateinit var mAdapter: ArrayAdapter<String>
    private lateinit var mRefreshWrapper: RefreshWrapper
    private lateinit var mRefreshLogic: RefreshLogic

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
                .putStatusView(NetworkErrView(this))
                .putStatusView(ServerErrView(this))
                .putStatusView(EmptyView(this))
                .putStatusView(LoadingView(this))
                .build(SimpleRefreshWrapper::class.java)
        mRefreshLogic = SimpleRefreshLogic(mRefreshWrapper, this)
        loadData()
        Halo.inject(this)
    }

    private fun loadData() {
        if (!mRefreshLogic.beforeLoadData()) {
            return
        }

        GlobalScope.launch {
            val data = (0 until 100).mapIndexed { index, _ -> "item $index" }
            delay(3000)

            launch(Dispatchers.Main) {
                endRefresh(true, data)
            }
        }
    }

    private fun endRefresh(success: Boolean, data: List<String>) {
        if (data.isNotEmpty()) {
            mAdapter.addAll(data)
        }
        mRefreshLogic.afterLoadData(success, data.isEmpty())
    }

    override fun isEmpty() = mAdapter.isEmpty

    @OnClick(R.id.btn_one)
    fun onBtnClick(view: View) {
        supportFragmentManager.beginTransaction()
                .add(R.id.container, MainFragment(), MainFragment.TAG)
                .addToBackStack(null)
                .commit()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main_activity, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.show_content -> {
            mRefreshWrapper.showStatusView(ViewStatus.CONTENT)
            true
        }
        R.id.show_empty -> {
            mRefreshWrapper.showStatusView(ViewStatus.EMPTY)
            true
        }
        R.id.show_loading -> {
            mRefreshWrapper.showStatusView(ViewStatus.LOADING)
            true
        }
        R.id.show_net_err -> {
            mRefreshWrapper.showStatusView(ViewStatus.NETWORK_ERR)
            true
        }
        R.id.show_service_err -> {
            mRefreshWrapper.showStatusView(ViewStatus.SERVER_ERR)
            true
        }
        else -> super.onOptionsItemSelected(item)
    }
}
