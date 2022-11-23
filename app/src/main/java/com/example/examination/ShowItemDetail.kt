package com.example.examination

import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import com.example.examination.databinding.ActivityMainBinding
import com.example.examination.databinding.ActivityShowItemDetailBinding

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
        if (byte != null) {
            val map = BitmapFactory.decodeByteArray(byte, 0, byte.size)
            binding.itemImage.setImageDrawable(BitmapDrawable(resources, map))
        }
        binding.itemName.text = itemName
        binding.itemPrice.text = itemPrice
        binding.buyerSum.text = buyerSum
        binding.shopLocate.text = shopLocate
        binding.shopName.text = shopName
    }

    override fun onBackPressed() {
        super.onBackPressed()
        ActivityCompat.finishAfterTransition(this)
    }
}