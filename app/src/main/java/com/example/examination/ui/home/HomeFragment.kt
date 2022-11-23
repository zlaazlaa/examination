package com.example.examination.ui.home

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.examination.MySQL
import com.example.examination.R
import com.example.examination.databinding.FragmentHomeBinding
import com.example.examination.item
import com.example.examination.itemAdapter
import q.rorbin.verticaltablayout.VerticalTabLayout
import q.rorbin.verticaltablayout.adapter.TabAdapter
import q.rorbin.verticaltablayout.widget.ITabView
import q.rorbin.verticaltablayout.widget.TabView
import java.sql.SQLException
import kotlin.concurrent.thread

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    var homeViewModel = HomeViewModel()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    lateinit var adapter: itemAdapter
    private val mHandler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                0 -> {
                    //完成主界面更新,拿到数据
                    activity?.findViewById<q.rorbin.verticaltablayout.VerticalTabLayout>(R.id.tablayout)
                        ?.setTabAdapter(object : TabAdapter {
                            override fun getCount(): Int {
                                return homeViewModel.typeList.size
                            }

                            override fun getBadge(position: Int): ITabView.TabBadge? {
                                return null
                            }

                            override fun getIcon(position: Int): ITabView.TabIcon? {
                                return null
                            }

                            override fun getTitle(position: Int): ITabView.TabTitle? {
                                return ITabView.TabTitle.Builder()
                                    .setContent(homeViewModel.typeList[position])
                                    .setTextColor(Color.RED, Color.BLACK)
                                    .build()
                            }

                            override fun getBackground(position: Int): Int {
                                return 0
                            }
                        })
                    showType("手机", 1)
                }
                1 -> {
                    try {
                        val layoutManager = LinearLayoutManager(activity)
                        binding.recyclerView.layoutManager = layoutManager
                        adapter = activity?.let { itemAdapter(homeViewModel.itemList, it) }!!
                        binding.recyclerView.adapter = adapter
                    } catch (e: java.lang.NullPointerException) {
                        Log.e("error", "java.lang.NullPointerException")
                    }
                }
                2 -> {
//                    adapter.notifyItemRangeRemoved(0, homeViewModel.itemList.size);
                    adapter.notifyItemRangeChanged(0, homeViewModel.itemList.size)
                }
                else -> {}
            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root


        thread {
            val mysql = MySQL()
            mysql.connect()
            val resultSet = MySQL.ps?.executeQuery("select DISTINCT type from items;")
            if (resultSet != null) {
                homeViewModel.typeList.clear()
                while (resultSet.next()) {
                    homeViewModel.typeList.add(resultSet.getString("type"))
                }
            }
            mHandler.sendEmptyMessage(0)
        }

        binding.tablayout.addOnTabSelectedListener(
            object : VerticalTabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabView?, position: Int) {
                    showType(homeViewModel.typeList[position], 2)
                }

                override fun onTabReselected(tab: TabView?, position: Int) {
                    showType(homeViewModel.typeList[position], 2)
                }
            }
        )
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun showType(type: String, tag: Int) {
        thread {
            val resultSet = MySQL.ps?.executeQuery("select * from items where type='$type';")
            if (resultSet != null) {
                try {
                    homeViewModel.itemList.clear()
                    while (resultSet.next()) {
                        homeViewModel.itemList.add(
                            item(
                                resultSet.getInt("id"),
                                resultSet.getString("type"),
                                resultSet.getString("price"),
                                resultSet.getString("item_name"),
                                resultSet.getString("buyer_sum"),
                                resultSet.getString("shop_name"),
                                resultSet.getString("shop_locate"),
                                resultSet.getString("pic_url")
                            )
                        )
                    }
                } catch (e: NullPointerException) {
                    Log.e("MySQL", "nullPointerERROR")
                } catch (e: SQLException) {
                    Log.e("MySQL", "Operation not allowed after ResultSet closed")
                }
            }
            mHandler.sendEmptyMessage(tag)
        }
    }
}