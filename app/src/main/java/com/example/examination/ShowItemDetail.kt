package com.example.examination

import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.examination.databinding.ActivityMainBinding
import com.example.examination.databinding.ActivityShowItemDetailBinding
import kotlin.concurrent.thread

class ShowItemDetail : AppCompatActivity() {
    private lateinit var binding: ActivityShowItemDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowItemDetailBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val itemName = intent.getStringExtra("item_name")
        val itemPrice = intent.getStringExtra("item_price")
        val buyerSum = intent.getStringExtra("buyer_sum")
        val shopLocate = intent.getStringExtra("shop_locate")
        val byte = intent.getByteArrayExtra("item_image")
        val shopName = intent.getStringExtra("shop_name")
        val itemId = intent.getStringExtra("item_id")
        if (byte != null) {
            val map = BitmapFactory.decodeByteArray(byte, 0, byte.size)
            binding.itemImage.setImageDrawable(BitmapDrawable(resources, map))
        }
        binding.itemId.text = itemId
        binding.itemName.text = itemName
        binding.itemPrice.text = itemPrice
        binding.buyerSum.text = buyerSum
        binding.shopLocate.text = shopLocate
        binding.shopName.text = shopName
        binding.floatBtn.setOnClickListener {
            var tag = 0
            thread {
                val mysql = MySQL()
                mysql.connect()
                val resultSet =
                    MySQL.ps?.executeQuery("select * from items where id = '$itemId' limit 1;")
                if (resultSet != null) {
                    if (!resultSet.next()) {
                        tag = 0
                    } else {
                        var number = resultSet.getInt("number")
                        number++
                        mysql.connect()
                        tag =
                            MySQL.ps?.executeUpdate("update items set number=$number where id=$itemId")!!
                    }
                } else {
                    tag = 0
                }
                Looper.prepare()
                if (tag > 0) {
                    Toast.makeText(this, "添加成功", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "添加失败", Toast.LENGTH_SHORT).show()
                }
                Looper.loop()
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        ActivityCompat.finishAfterTransition(this)
    }
}