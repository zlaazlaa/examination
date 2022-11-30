package com.example.examination

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter


class DemoCollectionAdapter(
    fragment: Fragment,
    val orderItemList: ArrayList<ArrayList<OrderItem>>,
    val context: Context?
) : FragmentStateAdapter(fragment) {
    private val ARG_OBJECT = "object"
    lateinit var layoutManager: LinearLayoutManager
    lateinit var adapter: OrderAdapter
    override fun getItemCount(): Int = 5

    override fun getItemId(position: Int): Long {
        return orderItemList[position].hashCode().toLong()
    }

    override fun containsItem(itemId: Long): Boolean {
        return itemId in 0 until itemCount
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun createFragment(position: Int): Fragment {
        // Return a NEW fragment instance in createFragment(int)
        val fragment = DemoObjectFragment(orderItemList, position, context)
        val recyclerView = fragment.view?.findViewById<RecyclerView>(R.id.recyclerView)
        if (recyclerView != null && context != null) {
            layoutManager = LinearLayoutManager(fragment.context)
            recyclerView.layoutManager = layoutManager
            adapter = OrderAdapter(orderItemList[position], context)
            recyclerView.adapter = adapter
            adapter.notifyDataSetChanged()
        }
        fragment.arguments = Bundle().apply {
            // Our object is just an integer :-P
            putInt(ARG_OBJECT, position + 1)
        }
        return fragment
    }
}