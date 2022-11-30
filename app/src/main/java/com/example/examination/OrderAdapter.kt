package com.example.examination

import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.util.Log
import android.util.Pair
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class OrderAdapter(private val orderItemList: ArrayList<OrderItem>, private val context: Context) :
    RecyclerView.Adapter<OrderAdapter.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val shopName: TextView = view.findViewById(R.id.shop_name)
        val orderStatus: TextView = view.findViewById(R.id.order_status)
        val itemName: TextView = view.findViewById(R.id.item_name)
        val itemImg: ImageView = view.findViewById(R.id.item_image)
        val itemPrice: TextView = view.findViewById(R.id.item_price)
        val itemNumber: TextView = view.findViewById(R.id.item_number)
        val priceTotal: TextView = view.findViewById(R.id.price_total)
        val cardLinearLayout: LinearLayout = view.findViewById(R.id.card_layout)
        val imgUrl: TextView = view.findViewById(R.id.img_url)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.order_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val orderItem = orderItemList[position]
        Glide.with(context).load(orderItem.itemImgUrl).into(holder.itemImg)
        holder.imgUrl.text = orderItem.itemImgUrl
        holder.shopName.text = orderItem.shopName
        holder.orderStatus.text = when (orderItem.orderStatus) {
            0 -> "待付款"
            1 -> "待发货"
            2 -> "已发货"
            3 -> "待评价"
            4 -> "已评价"
            else -> "未知状态"
        }
        holder.itemName.text = orderItem.itemName
        holder.itemPrice.text = orderItem.price
        holder.itemNumber.text = orderItem.buyNumber.toString()
        holder.priceTotal.text = orderItem.priceTotal
        holder.cardLinearLayout.setOnClickListener {
            val pairCardLayout = Pair<View, String>(holder.cardLinearLayout, "card_layout")
            val bundle =
                ActivityOptions.makeSceneTransitionAnimation(
                    context as Activity?,
                    pairCardLayout
                ).toBundle()
            try {
//                val byte = bmpToByteArray(holder.itemImg.drawToBitmap())
                val intent = Intent(context, ShowOrderDetails::class.java)
                intent.putExtra("id_order_item", orderItem.idOrderItem.toString())
                intent.putExtra("item_id", orderItem.itemId.toString())
                intent.putExtra("img_url", holder.imgUrl.text)
                intent.putExtra("item_name", holder.itemName.text)
                intent.putExtra("item_price", holder.itemPrice.text)
                intent.putExtra("item_number", holder.itemNumber.text)
                intent.putExtra("shop_name", holder.shopName.text)
                intent.putExtra("price_total", holder.priceTotal.text)
                intent.putExtra("score", orderItem.score.toString())
                intent.putExtra("item_img", holder.itemImg.drawingCache)
                intent.putExtra("order_id", orderItem.orderId.toString())
                holder.itemImg.isDrawingCacheEnabled = true
                intent.putExtra("order_status", holder.orderStatus.text)
                ContextCompat.startActivity(context, intent, bundle)
            } catch (e: java.lang.IllegalStateException) {
                Log.e("error", "java.lang.IllegalStateException")
                Log.e("error", e.toString())
            }
        }
    }

    override fun getItemCount(): Int {
        return orderItemList.size
    }
}