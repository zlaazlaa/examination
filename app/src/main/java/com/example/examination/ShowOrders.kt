package com.example.examination

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Message
import androidx.appcompat.app.AppCompatActivity



class ShowOrders : AppCompatActivity() {
//    private val orderItemList = ArrayList<ArrayList<OrderItem>>()
//    private lateinit var demoCollectionAdapter: DemoCollectionAdapter
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
    //    val orderItemList = Array(5) {
//        Array<OrderItem>(3) {
//            OrderItem(
//                0,
//                0,
//                "0",
//                0,
//                "0",
//                0,
//                "0",
//                "0",
//                "0",
//                "0",
//                "0"
//            )
//        }
//    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_orders)
    }
}