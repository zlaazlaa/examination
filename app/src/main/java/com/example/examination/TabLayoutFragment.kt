package com.example.examination

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.viewpager2.widget.ViewPager2
import com.example.examination.databinding.FragmentTabLayoutBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlin.concurrent.thread

class TabLayoutFragment : Fragment() {
    // When requested, this adapter returns a DemoObjectFragment,
    // representing an object in the collection.
    private val orderItemList = ArrayList<ArrayList<OrderItem>>()
    lateinit var demoCollectionAdapter: DemoCollectionAdapter
    private lateinit var viewPager: ViewPager2
    private var _binding: FragmentTabLayoutBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MyViewModel by activityViewModels()
    var targetId: Int = 0

    val name = arrayOf("待付款", "待发货", "已发货", "待评价", "已评价")
    private val mHandler = @SuppressLint("HandlerLeak")
    object : Handler() {
        @SuppressLint("NotifyDataSetChanged")
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                0 -> {
                    onResume()
                    return
//                    findViewById<androidx.fragment.app.FragmentContainerView>(R.id.fragment_hello)
//                    demoCollectionAdapter = DemoCollectionAdapter(this)
//                    val tabLayout = findViewById<TabLayout>(R.id.tab_layout)
//                    val viewPager = findViewById<ViewPager2>(R.id.view_pager)
//                    viewPager.adapter = demoCollectionAdapter
//                    TabLayoutMediator(tabLayout, viewPager) { tab, position ->
//                        tab.text = "OBJECT ${(position + 1)}"
//                    }.attach()
                }
                1 -> {
                    demoCollectionAdapter.notifyDataSetChanged()
                }
                2 -> {
                    viewPager.currentItem = 4
                }
            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.e("fragment", "onCreateView")
        _binding = FragmentTabLayoutBinding.inflate(inflater, container, false)
        for (i in 1..7) {
            orderItemList.add(ArrayList<OrderItem>())
        }
//        freshData()
        return inflater.inflate(R.layout.fragment_tab_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.e("fragment", "onViewCreated")

        viewModel.selectedItem.observe(viewLifecycleOwner, Observer { item ->
            // Perform an action with the latest item data
            targetId = item.toInt()
        })

        demoCollectionAdapter = DemoCollectionAdapter(this, orderItemList, context)
        viewPager = view.findViewById(R.id.pager)
        viewPager.adapter = demoCollectionAdapter
//        viewPager.currentItem = 3
        freshData()

        val tabLayout = view.findViewById<TabLayout>(R.id.tab_layout)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
//            tab.text = "OBJECT ${(position + 1)}"
            tab.text = name[position]
        }.attach()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        val aaa = 123
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.e("fragment", "onAttach")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.e("fragment", "onActivityCreated")
    }

    override fun onPrimaryNavigationFragmentChanged(isPrimaryNavigationFragment: Boolean) {
        super.onPrimaryNavigationFragmentChanged(isPrimaryNavigationFragment)
        Log.e("fragment", "onPrimaryNavigationFragmentChanged")
    }


    override fun onStart() {
        super.onStart()
        Log.e("fragment", "onStart")
    }

    override fun onResume() {
        super.onResume()
        val orderItemList2 = orderItemList
//        freshData()

        demoCollectionAdapter = DemoCollectionAdapter(
            this@TabLayoutFragment,
            orderItemList2,
            activity
        )
//                    demoCollectionAdapter.notifyDataSetChanged()
        if (viewPager.adapter != null)
            viewPager.adapter = null
        viewPager.adapter = demoCollectionAdapter
//        viewPager.currentItem = 3
//
//        viewPager.doOnPreDraw {
//            viewPager.currentItem = 2
//        }
//        Handler().post { viewPager.currentItem = 3 }
//
//
//        viewPager.currentItem = 3
        viewPager.doOnPreDraw {
            if (viewPager.adapter != null)
                viewPager.adapter = null
            viewPager.adapter = demoCollectionAdapter
            viewPager.currentItem = targetId
        }
        Log.e("fragment", "onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.e("fragment", "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.e("fragment", "onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("fragment", "onDestroy")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.e("fragment", "onDestroyView")
    }


    override fun onDetach() {
        super.onDetach()
        Log.e("fragment", "onDetach")
    }


    private fun freshData() {
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
                                resultSet.getInt("statement") - 1,
                                resultSet.getString("item_name"),
                                resultSet.getString("pic_url"),
                                "0",
                                resultSet.getInt("score"),
                                resultSet.getInt("order_id")
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
//                mHandler.sendEmptyMessage(1)
            } finally {

            }
        }
    }
}

