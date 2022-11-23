package com.example.examination.ui.dashboard

import android.annotation.SuppressLint
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
import com.example.examination.databinding.FragmentDashboardBinding
import com.example.examination.item
import com.example.examination.itemAdapter
import java.sql.SQLException
import kotlin.concurrent.thread

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    var dashboardViewModel = DashboardViewModel()

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
                    try {
                        val layoutManager = LinearLayoutManager(activity)
                        binding.recyclerView.layoutManager = layoutManager
                        adapter =
                            activity?.let { itemAdapter(dashboardViewModel.itemList, it, true) }!!
                        binding.recyclerView.adapter = adapter
                    } catch (e: java.lang.NullPointerException) {
                        Log.e("error", "java.lang.NullPointerException")
                    }
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        thread {
            val mysql = MySQL()
            mysql.connect()
            val resultSet = MySQL.ps?.executeQuery("select * from items where number > 0;")
            if (resultSet != null) {
                try {
                    dashboardViewModel.itemList.clear()
                    while (resultSet.next()) {
                        dashboardViewModel.itemList.add(
                            item(
                                resultSet.getInt("id"),
                                resultSet.getString("type"),
                                resultSet.getString("price"),
                                resultSet.getString("item_name"),
                                resultSet.getString("buyer_sum"),
                                resultSet.getString("shop_name"),
                                resultSet.getString("shop_locate"),
                                resultSet.getString("pic_url"),
                                resultSet.getInt("number")
                            )
                        )
                    }
                } catch (e: NullPointerException) {
                    Log.e("MySQL", "nullPointerERROR")
                } catch (e: SQLException) {
                    Log.e("MySQL", "Operation not allowed after ResultSet closed")
                }
            }
            mHandler.sendEmptyMessage(0)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}