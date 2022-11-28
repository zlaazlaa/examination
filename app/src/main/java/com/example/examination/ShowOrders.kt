package com.example.examination

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider


class ShowOrders : AppCompatActivity() {
    //    private val orderItemList = ArrayList<ArrayList<OrderItem>>()
//    private lateinit var demoCollectionAdapter: DemoCollectionAdapter
    private val model by lazy {
        ViewModelProvider(this).get(MyViewModel::class.java)
    }
    private val viewModel: MyViewModel by viewModels()

    private val mHandler = @SuppressLint("HandlerLeak")
    object : Handler() {
        @SuppressLint("NotifyDataSetChanged")
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                0 -> {
                    findViewById<androidx.fragment.app.FragmentContainerView>(R.id.fragment_hello)
//                    demoCollectionAdapter = DemoCollectionAdapter(this)
//                    val tabLayout = findViewById<TabLayout>(R.id.tab_layout)
//                    val viewPager = findViewById<ViewPager2>(R.id.view_pager)
//                    viewPager.adapter = demoCollectionAdapter
//                    TabLayoutMediator(tabLayout, viewPager) { tab, position ->
//                        tab.text = "OBJECT ${(position + 1)}"
//                    }.attach()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_orders)
        val tt = intent.getStringExtra("target_id")
        Log.e("tt", tt.toString())
        intent.getStringExtra("target_id")?.let { viewModel.selectItem(it) }
//        viewModel.selectItem("124")
//        val transaction = supportFragmentManager.beginTransaction()
//        transaction.replace(R.id.fragment_hello, TabLayoutFragment())
//        transaction.commit()
//
//        model.textLiveData.value = "761234561"

    }
}