package com.example.examination

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider


class ShowOrders : AppCompatActivity() {
    //    private val orderItemList = ArrayList<ArrayList<OrderItem>>()
    //    private lateinit var demoCollectionAdapter: DemoCollectionAdapter
    private val model by lazy {
        ViewModelProvider(this).get(MyViewModel::class.java)
    }
    private val viewModel: MyViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_orders)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.title = "我的订单"
            actionBar.setDisplayHomeAsUpEnabled(true)
        }
        intent.getStringExtra("target_id")?.let { viewModel.selectItem(it) }
    }

    override fun onOptionsItemSelected(menu_item: MenuItem): Boolean {
        this.finish()
        return true
    }
}