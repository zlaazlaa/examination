package com.example.examination

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class DemoObjectFragment(
    private val orderItemList: ArrayList<ArrayList<OrderItem>>,
    private val position: Int,
    private val context1: Context?
) : Fragment() {
    //    private lateinit var demoCollectionAdapter: DemoCollectionAdapter
    private val ARG_OBJECT = "object"
    lateinit var layoutManager: LinearLayoutManager
    lateinit var adapter: OrderAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.order_list, container, false)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        try {
            arguments?.takeIf { it.containsKey(ARG_OBJECT) }?.apply {
                if (context1 != null) {
                    layoutManager = LinearLayoutManager(activity)
                    val recyclerview = view.findViewById<RecyclerView>(R.id.recyclerView)
                    recyclerview.layoutManager = layoutManager
                    adapter = OrderAdapter(orderItemList[position], context1)
                    recyclerview.adapter = adapter
                    adapter.notifyDataSetChanged()
                }
            }
        } catch (e: java.lang.IndexOutOfBoundsException) {
            Log.e("ERROR", "ERROR88254")
        }
    }
}