package com.example.examination.ui.home

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.examination.MySQL
import com.example.examination.R
import com.example.examination.databinding.FragmentHomeBinding
import q.rorbin.verticaltablayout.adapter.TabAdapter
import q.rorbin.verticaltablayout.widget.ITabView
import kotlin.concurrent.thread

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    var homeViewModel = HomeViewModel()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

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
                while (resultSet.next()) {
                    homeViewModel.typeList.add(resultSet.getString("type"))
                }
            }
            homeViewModel.typeList.add("wqeqwe")
            mHandler.sendEmptyMessage(0)
        }


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}