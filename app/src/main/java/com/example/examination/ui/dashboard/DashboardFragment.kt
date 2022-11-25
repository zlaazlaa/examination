package com.example.examination.ui.dashboard

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.examination.*
import com.example.examination.databinding.FragmentDashboardBinding
import okhttp3.internal.notifyAll
import java.sql.SQLException
import kotlin.concurrent.thread

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    var dashboardViewModel = DashboardViewModel()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    lateinit var adapter: itemAdapter

    private val mHandler2 = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                0 -> {
                    Log.e("handler", "ok")
                    updateSum()
                }
            }
        }
    }


    private val mHandler = @SuppressLint("HandlerLeak")
    object : Handler() {
        @SuppressLint("NotifyDataSetChanged")
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                0 -> {
                    try {
                        val layoutManager = LinearLayoutManager(activity)
                        binding.recyclerView.layoutManager = layoutManager
                        adapter =
                            activity?.let {
                                itemAdapter(
                                    dashboardViewModel.itemList,
                                    it,
                                    1,
                                    mHandler2
                                )
                            }!!
                        binding.recyclerView.adapter = adapter
                        updateSum()
                    } catch (e: java.lang.NullPointerException) {
                        Log.e("error", "java.lang.NullPointerException")
                    }
                }
                1 -> {
                    adapter.notifyDataSetChanged()
                    updateSum()
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

        setHasOptionsMenu(true)

        binding.floatBtn.setOnClickListener {
            val arrayId = ArrayList<Int>()
            val itemList = dashboardViewModel.itemList
            var sql = "update items set number = 0 WHERE FIND_IN_SET (id,'"
            val array = ArrayList<Int>()
            for ((index, item) in itemList.withIndex()) {
                if (item.isChecked) {
                    array.add(index)
                    arrayId.add(item.id)
                }
            }
            val intent = Intent(activity, CommitOrder::class.java)
            intent.putExtra("id", arrayId)
            startActivity(intent)
        }

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
                                resultSet.getInt("number"),
                                false
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_optionmenu, menu)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        when (item.itemId) {
            R.id.menu1 -> {
                thread {
                    val mysql = MySQL()
                    mysql.connect()
                    dashboardViewModel.itemList.clear()
                    val result =
                        MySQL.ps?.executeUpdate("update android.items set number = 0 where number > 0 and id < 1000")
                    result!! > 0
                    mHandler.sendEmptyMessage(1)
                }
            }
            else -> {
                Log.e("ERROR", "error")
                return true
            }
        }
        return true
    }

    fun updateSum() {
        var sum = 0.0
        for (item in dashboardViewModel.itemList) {
            sum += item.number * item.price.toFloat()
        }
        binding.totalPrice.text = sum.toString()
    }
}