package com.example.examination

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class itemAdapter(private val itemList: ArrayList<item>, private val context: Context) :
    RecyclerView.Adapter<itemAdapter.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemImage: ImageView = view.findViewById(R.id.item_image)
        val itemName: TextView = view.findViewById(R.id.item_name)
        val itemPrice: TextView = view.findViewById(R.id.item_price)
        val buyerSum: TextView = view.findViewById(R.id.buyer_sum)
        val shopLocate: TextView = view.findViewById(R.id.shop_locate)
        val shopName: TextView = view.findViewById(R.id.shop_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]
        Glide.with(context).load(item.pic_url).into(holder.itemImage)
        holder.itemName.text = item.item_name
        holder.itemPrice.text = item.price
        holder.buyerSum.text = item.buyer_sum
        holder.shopLocate.text = item.shop_locate
        holder.shopName.text = item.shop_name
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}