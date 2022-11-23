package com.example.examination

import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import android.util.Pair
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.drawToBitmap
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.w3c.dom.Text
import java.io.ByteArrayOutputStream
import kotlin.concurrent.thread


class itemAdapter(
    private val itemList: ArrayList<item>,
    private val context: Context,
    val tag: Boolean
) :
    RecyclerView.Adapter<itemAdapter.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemImage: ImageView = view.findViewById(R.id.item_image)
        val itemName: TextView = view.findViewById(R.id.item_name)
        val itemPrice: TextView = view.findViewById(R.id.item_price)
        val buyerSum: TextView = view.findViewById(R.id.buyer_sum)
        val shopLocate: TextView = view.findViewById(R.id.shop_locate)
        val shopName: TextView = view.findViewById(R.id.shop_name)
        val wholeLayout: LinearLayout = view.findViewById(R.id.whole_layout)
        val dolor: TextView = view.findViewById(R.id.dolor)
        val itemId: TextView = view.findViewById(R.id.item_id)
        val buyNumber: TextView = view.findViewById(R.id.buy_number)
        val upImg: ImageView = view.findViewById(R.id.up_img)
        val downImg: ImageView = view.findViewById(R.id.down_img)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        lateinit var view: View
        if (!tag) {
            view = LayoutInflater.from(parent.context).inflate(R.layout.item_card, parent, false)
        } else {
            view =
                LayoutInflater.from(parent.context).inflate(R.layout.item_card_cargo, parent, false)
        }
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]
        Glide.with(context).load(item.pic_url).into(holder.itemImage)
        holder.itemId.text = item.id.toString()
        holder.itemName.text = item.item_name
        holder.itemPrice.text = item.price
        holder.buyerSum.text = item.buyer_sum
        holder.shopLocate.text = item.shop_locate
        holder.shopName.text = item.shop_name
        holder.buyNumber.text = item.number.toString()
        holder.upImg.setOnClickListener {
            upAndDown(1, position, holder)
        }
        holder.downImg.setOnClickListener {
            upAndDown(-1, position, holder)
        }
        holder.wholeLayout.setOnClickListener {
            val pairItemName = Pair<View, String>(holder.itemName, "item_name")
            val pairItemPrice = Pair<View, String>(holder.itemImage, "item_price")
            val pairBuyerSum = Pair<View, String>(holder.buyerSum, "buyer_sum")
            val pairShopLocate = Pair<View, String>(holder.shopLocate, "shop_locate")
            val pairShopName = Pair<View, String>(holder.buyerSum, "shop_name")
            val pairItemImage = Pair<View, String>(holder.itemImage, "item_image")
            val pairDolor = Pair<View, String>(holder.dolor, "dolor")

            val bundle =
                ActivityOptions.makeSceneTransitionAnimation(
                    context as Activity?,
                    pairItemName,
                    pairItemPrice,
                    pairBuyerSum,
                    pairShopLocate,
                    pairShopName,
                    pairItemImage,
                    pairDolor
                ).toBundle()
            val byte = bmpToByteArray(holder.itemImage.drawToBitmap())

            val intent = Intent(context, ShowItemDetail::class.java)
            intent.putExtra("item_name", holder.itemName.text)
            intent.putExtra("item_price", holder.itemPrice.text)
            intent.putExtra("buyer_sum", holder.buyerSum.text)
            intent.putExtra("shop_locate", holder.shopLocate.text)
            intent.putExtra("shop_name", holder.shopName.text)
            intent.putExtra("item_image", byte)
            intent.putExtra("item_id", holder.itemId.text)
            startActivity(context, intent, bundle)
        }
    }

    fun upAndDown(tag: Int, pos: Int, holder: ViewHolder) {
        val item = itemList[pos]
        if (item.number + tag >= 0) {
            item.number += tag
        }
        holder.buyNumber.text = item.number.toString()
        thread {
            val mysql = MySQL()
            mysql.connect()
            val resultSet =
                MySQL.ps?.executeUpdate("update items set number = ${item.number} where id = ${item.id};")
            if (resultSet != null) {
                if (resultSet > 0) {
                    Log.e("OK", "UPDATE DATABASE")
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    fun bmpToByteArray(bmp: Bitmap): ByteArray {
        val output = ByteArrayOutputStream()
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output)

        val result = output.toByteArray()
        try {
            output.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return result
    }

}