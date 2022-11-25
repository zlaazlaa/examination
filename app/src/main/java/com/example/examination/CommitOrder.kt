package com.example.examination

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.examination.databinding.ActivityCommitOrderBinding
import java.sql.SQLException
import kotlin.concurrent.thread

class CommitOrder : AppCompatActivity() {
    lateinit var adapter: itemAdapter
    lateinit var binding: ActivityCommitOrderBinding
    val itemList = ArrayList<item>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommitOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val arrayId = intent.getIntegerArrayListExtra("id")

        updateDetails()
        val mHandler = @SuppressLint("HandlerLeak") object : Handler() {
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                when (msg.what) {
                    0 -> {
                        val layoutManager = LinearLayoutManager(this@CommitOrder)
                        binding.recyclerView.layoutManager = layoutManager
                        adapter = itemAdapter(
                            itemList,
                            this@CommitOrder,
                            2,
                            null
                        )
                        binding.recyclerView.adapter = adapter
                        updateSum()
                    }
                }
            }
        }
        var sql = "select * from items WHERE id in ("
        if (arrayId != null) {
            for (id in arrayId) {
                sql += "$id, "
            }
            sql = sql.dropLast(2)
            sql += ");"
        }
        thread {
            try {
                val mysql = MySQL()
                mysql.connect()
                val resultSet = MySQL.ps?.executeQuery(sql)
                if (resultSet != null) {
                    while (resultSet.next()) {
                        itemList.add(
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
                }
                mHandler.sendEmptyMessage(0)
            } finally {
                Log.e("ERROR", "ERROR77821")
            }
        }
        binding.floatBtn.setOnClickListener {
            return@setOnClickListener
//            if (size > 0) {
//                for (i in arrayId) {
//                    sql += "${itemList[i].id}', '"
//                }
//                sql = sql.dropLast(3)
//                sql += ")"
//                thread {
//                    try {
//                        val mysql = MySQL()
//                        mysql.connect()
//                        if (MySQL.ps?.executeUpdate(sql)!! > 0) {
//                            Log.e("OK", "OK")
//                        }
//                    } finally {
//                        Log.e("ERROR", "ERROR16674")
//                    }
//                }
//    //                sql = "insert into android.order(statement, price_sum, name, phone, address, other_info, order_score) values "
//    //                sql += "(0, ${binding.totalPrice.text}, )"
//                "SELECT LAST_INSERT_ID();"
//                sql = "insert into android.order_item(item_id, order_id, price, buy_number, price_total) values "
//                for (i in arrayId) {
//    //                    sql += "(${itemList[i].id}, ${itemList[i].}), "
//                }
//            }
        }
    }

    fun updateDetails() {
        var name = ""
        var phone = ""
        var address = ""
        var otherInfo = ""
        var payMethod = ""
        val mHandler = @SuppressLint("HandlerLeak") object : Handler() {
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                when (msg.what) {
                    0 -> {
                        binding.nameEdit.setText(name)
                        binding.phoneEdit.setText(phone)
                        binding.addressEdit.setText(address)
                        binding.otherEdit.setText(otherInfo)
                        binding.payEdit.setText(payMethod)
                    }
                }
            }
        }

        thread {
            val mysql = MySQL()
            mysql.connect()
            val resultSet = MySQL.ps?.executeQuery("select * from user_info limit 1;")
            if (resultSet != null) {
                try {
                    while (resultSet.next()) {
                        name = resultSet.getString("name")
                        phone = resultSet.getString("phone")
                        address = resultSet.getString("address")
                        otherInfo = resultSet.getString("other_info")
                        payMethod = resultSet.getString("pay_method")
                        mHandler.sendEmptyMessage(0)
                    }
                } catch (e: NullPointerException) {
                    Log.e("MySQL", "nullPointerERROR")
                } catch (e: SQLException) {
                    Log.e("MySQL", "Operation not allowed after ResultSet closed")
                }
            }
        }
    }

    fun updateSum() {
        var sum = 0.0
        for (item in itemList) {
            sum += item.number * item.price.toFloat()
        }
        binding.totalPrice.text = sum.toString()
    }
}