package com.example.examination

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.examination.databinding.ActivityShowItemDetailBinding
import com.example.examination.databinding.ActivityShowOrderDetailsBinding
import kotlin.concurrent.thread

class ShowOrderDetails : AppCompatActivity() {
    private lateinit var binding: ActivityShowOrderDetailsBinding
    private val mHandler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                0 -> {
                    Toast.makeText(this@ShowOrderDetails, "评价提交成功", Toast.LENGTH_SHORT).show()
                }
                1 -> {
                    Toast.makeText(this@ShowOrderDetails, "评价失败", Toast.LENGTH_SHORT).show()
                }
                2 -> {
                    Toast.makeText(this@ShowOrderDetails, "收货成功", Toast.LENGTH_SHORT).show()
                }
                3 -> {
                    Toast.makeText(this@ShowOrderDetails, "收获失败", Toast.LENGTH_SHORT).show()
                }
                4 -> {
                    Toast.makeText(this@ShowOrderDetails, "支付失败", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowOrderDetailsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.itemName.text = intent.getStringExtra("item_name")
        binding.itemPrice.text = intent.getStringExtra("item_price")
        binding.itemNumber.text = intent.getStringExtra("item_number")
        binding.shopName.text = intent.getStringExtra("shop_name")
        binding.priceTotal.text = intent.getStringExtra("price_total")
        binding.orderStatus.text = intent.getStringExtra("order_status")
        binding.scoreNow.text = intent.getStringExtra("score")
        val idOrderItem = intent.getStringExtra("id_order_item").toString().toInt()
        val itemId = intent.getStringExtra("item_id")
        val score = binding.scoreNow.text.toString().toInt()
        val order_id = intent.getStringExtra("order_id")
        if (score in 1..5) {
            Listener().lightStar(score - 1)
        }
//        intent.getStringExtra("id_order_item")?.let {
//            Listener().lightStar(it.toInt())
//        }
        val imgUrl = intent.getStringExtra("img_url")
        Glide.with(this).load(imgUrl).into(binding.itemImage)
        binding.star1.setOnClickListener(Listener())
        binding.star2.setOnClickListener(Listener())
        binding.star3.setOnClickListener(Listener())
        binding.star4.setOnClickListener(Listener())
        binding.star5.setOnClickListener(Listener())
        if (binding.orderStatus.text == "待付款") {
            binding.confirmOrder.text = "继续付款"
        }
        binding.scoreBtn.setOnClickListener {
            if (binding.orderStatus.text != "待评价") {
                Toast.makeText(this, "当前状态不能评价", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            thread {
                try {
                    val mysql = MySQL()
                    mysql.connect()
                    var sql =
                        "update order_item set score = ${binding.scoreNow.text} where idorder_item = $idOrderItem;"
                    val result = MySQL.ps?.executeUpdate(sql)
                    if (result != null) {
                        if (result > 0) {
                            thread {
                                try {
                                    sql =
                                        "update orders set statement = 5 where order_id = $order_id;"
                                    MySQL.ps?.executeUpdate(sql)
                                    mHandler.sendEmptyMessage(0)
                                } finally {

                                }
                            }

                        } else {
                            mHandler.sendEmptyMessage(1)
                        }
                    } else {
                        mHandler.sendEmptyMessage(1)
                    }
                } finally {

                }
            }
        }
        binding.confirmOrder.setOnClickListener {
            if (binding.orderStatus.text == "待付款") {
                val arrayId = ArrayList<Int>()
                if (itemId != null) {
                    arrayId.add(itemId.toInt())
                }
                thread {
                    try {
                        val mysql = MySQL()
                        mysql.connect()
                        // restore the buy_number
                        var sql =
                            "update items set number = ${binding.itemNumber.text} WHERE id = $itemId;"
                        val result = MySQL.ps?.executeUpdate(sql)
                        if (result != null) {
                            if (result > 0) {
                                thread {
                                    try {
                                        // delete order
                                        sql =
                                            "update orders set statement = 6 where order_id = $order_id;"
                                        val result2 = MySQL.ps?.executeUpdate(sql)
                                        if (result2 != null) {
                                            if (result2 > 0) {
                                                val intent = Intent(this, CommitOrder::class.java)
                                                intent.putExtra("id", arrayId)
                                                startActivity(intent)
                                                mHandler.sendEmptyMessage(0)
                                            }
                                        }
                                    } finally {

                                    }
                                }
                            } else {
                                mHandler.sendEmptyMessage(5)
                            }
                        } else {
                            mHandler.sendEmptyMessage(1)
                        }
                    } finally {

                    }
                }
                return@setOnClickListener
            }
            if (binding.orderStatus.text != "已发货") {
                Toast.makeText(this, "当前状态不能收货", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            thread {
                try {
                    val mysql = MySQL()
                    mysql.connect()
                    val sql =
                        "update orders set statement = 4 where order_id = (select order_id from order_item where idorder_item = $idOrderItem);"
                    val result = MySQL.ps?.executeUpdate(sql)
                    if (result != null) {
                        if (result > 0) {
                            mHandler.sendEmptyMessage(2)
                        } else {
                            mHandler.sendEmptyMessage(2)
                        }
                    } else {
                        mHandler.sendEmptyMessage(2)
                    }
                } finally {

                }
            }
        }
////        val byte = intent.getByteArrayExtra("item_img")
//        val bitmap = intent.getParcelableExtra("item_img")
//        if (byte != null) {
//            val map = BitmapFactory.decodeByteArray(byte, 0, byte.size)
//            binding.itemImage.setImageDrawable(BitmapDrawable(resources, map))
//        }
    }

    override fun onResume() {
        super.onResume()
        val aa = 123
    }

    override fun onRestart() {
        super.onRestart()
        val aa = 1
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onPause() {
        super.onPause()
//        TabLayoutFragment().freshData()
//        TabLayoutFragment().demoCollectionAdapter.notifyDataSetChanged()
    }

    inner class Listener : View.OnClickListener {
        val arrayId = arrayOf(R.id.star1, R.id.star2, R.id.star3, R.id.star4, R.id.star5)
        override fun onClick(p0: View?) {
            when (p0?.id) {
                R.id.star1 -> {
                    lightStar(0)
                }
                R.id.star2 -> {
                    lightStar(1)
                }
                R.id.star3 -> {
                    lightStar(2)
                }
                R.id.star4 -> {
                    lightStar(3)
                }
                R.id.star5 -> {
                    lightStar(4)
                }
            }
        }

        @SuppressLint("SetTextI18n")
        fun lightStar(j: Int) {
            findViewById<TextView>(R.id.score_now).text = (j + 1).toString()
            for (k in 0..j) {
                findViewById<ImageView>(arrayId[k]).setImageResource(R.drawable.full_star)
            }
            for (k in j + 1..4) {
                findViewById<ImageView>(arrayId[k]).setImageResource(R.drawable.empty_star)
            }
        }
    }
}