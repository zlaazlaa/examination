package com.example.examination

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.examination.databinding.FragmentTabLayoutBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import okhttp3.internal.notifyAll
import kotlin.concurrent.thread

class TabLayoutFragment : Fragment() {
    // When requested, this adapter returns a DemoObjectFragment,
    // representing an object in the collection.
    private val orderItemList = ArrayList<ArrayList<OrderItem>>()
    lateinit var demoCollectionAdapter: DemoCollectionAdapter
    private lateinit var viewPager: ViewPager2
    private var _binding: FragmentTabLayoutBinding? = null
    private val binding get() = _binding!!

    val name = arrayOf("待付款", "待发货", "已发货", "待评价", "已评价")
    private val mHandler = @SuppressLint("HandlerLeak")
    object : Handler() {
        @SuppressLint("NotifyDataSetChanged")
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                0 -> {
                    demoCollectionAdapter = DemoCollectionAdapter(
                        this@TabLayoutFragment,
                        orderItemList,
                        activity
                    )
                    viewPager = binding.pager
                    viewPager.adapter = demoCollectionAdapter
                    demoCollectionAdapter.notifyDataSetChanged()
//                    findViewById<androidx.fragment.app.FragmentContainerView>(R.id.fragment_hello)
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


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTabLayoutBinding.inflate(inflater, container, false)
        for (i in 1..6) {
            orderItemList.add(ArrayList<OrderItem>())
        }
        freshData()
        return inflater.inflate(R.layout.fragment_tab_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        demoCollectionAdapter = DemoCollectionAdapter(this, orderItemList, context)
        viewPager = view.findViewById(R.id.pager)
        viewPager.adapter = demoCollectionAdapter


        val tabLayout = view.findViewById<TabLayout>(R.id.tab_layout)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
//            tab.text = "OBJECT ${(position + 1)}"
            tab.text = name[position]
        }.attach()
    }

    fun freshData() {
        for (i in 0..5) {
            orderItemList[i].clear()
        }
        val sql =
            "select * from order_item, items, orders where (order_item.item_id = items.id) and (orders.order_id = order_item.order_id);"
        thread {
            try {
                val mysql = MySQL()
                mysql.connect()
                val resultSet = MySQL.ps?.executeQuery(sql)
                if (resultSet != null) {
                    while (resultSet.next()) {
                        orderItemList[resultSet.getInt("statement") - 1].add(
                            OrderItem(
                                resultSet.getInt("idorder_item"),
                                resultSet.getInt("item_id"),
                                resultSet.getString("price"),
                                resultSet.getInt("buy_number"),
                                resultSet.getString("shop_name"),
                                0,
                                resultSet.getString("item_name"),
                                resultSet.getString("pic_url"),
                                "0",
                                resultSet.getInt("score")
                            )
                        )
                    }
                }
                for (i in 0..4) {
                    for (orderItem in orderItemList[i]) {
                        orderItem.priceTotal =
                            (orderItem.price.toFloat() * orderItem.buyNumber).toString()
                    }
                }
                mHandler.sendEmptyMessage(0)
            } finally {

            }
        }
    }
}

