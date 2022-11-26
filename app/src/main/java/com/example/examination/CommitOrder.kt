package com.example.examination

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.examination.databinding.ActivityCommitOrderBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import java.sql.SQLException
import kotlin.concurrent.thread


class CommitOrder : AppCompatActivity() {
    lateinit var adapter: itemAdapter
    lateinit var binding: ActivityCommitOrderBinding
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private lateinit var mDialogBehavior: BottomSheetBehavior<View>
    private var arrayId = ArrayList<Int>()
    val itemList = ArrayList<item>()
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
                1 -> {
                    var sql = "update items set number = 0 WHERE id in ("
                    for (id in arrayId) {
                        sql += "$id, "
                    }
                    sql = sql.dropLast(2)
                    sql += ");"
                    thread {
                        try {
                            val mysql = MySQL()
                            mysql.connect()
                            val resultSet = MySQL.ps?.executeUpdate(sql)
                            if (resultSet != null) {
                                if (resultSet > 0) {
                                    this.sendEmptyMessage(2)
                                }
                            }
                        } catch (e: java.lang.NullPointerException) {
                            Log.e("ERROR", "ERROR77821")
                        } catch (e: com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException) {
                            Log.e("ERROR", "ERROR7221")
                        } catch (e: java.sql.SQLException) {
                            Log.e("ERROR", "ERROR722221")
                        }
                    }
                }
                2 -> {
                    onBackPressed()
                }
                3 -> {
                    Toast.makeText(this@CommitOrder, "支付成功", Toast.LENGTH_SHORT).show()
                }
                4 -> {
                    Toast.makeText(this@CommitOrder, "支付失败，网络连接异常", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommitOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        arrayId = intent.getIntegerArrayListExtra("id") as ArrayList<Int>
        updateDetails()
        var sql = "select * from items WHERE id in ("
        for (id in arrayId) {
            sql += "$id, "
        }
        sql = sql.dropLast(2)
        sql += ");"
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
            } catch (e: java.lang.NullPointerException) {
                Log.e("ERROR", "ERROR77821")
            } catch (e: com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException) {
                Log.e("ERROR", "ERROR7221")
            } catch (e: java.sql.SQLException) {
                Log.e("ERROR", "ERROR722221")
            }
        }
        binding.edit.setOnClickListener {
            val intent = Intent(this, Setting::class.java)
            startActivity(intent)
        }
        binding.floatBtn.setOnClickListener {
            if (itemList.size == 0) {
                Toast.makeText(this, "购物车为空", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else {
                showSheetDialog1(
                    binding.payEdit.text.toString(),
                    binding.totalPrice.text.toString(),
                    this@CommitOrder
                )
                return@setOnClickListener
            }
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

    override fun onResume() {
        super.onResume()
        updateDetails()
    }

    @SuppressLint("ResourceAsColor")
    private fun showSheetDialog1(pay: String, price: String, commitOrder: CommitOrder) {
        val view = View.inflate(this, R.layout.dialog_bottomsheet, null)
        var sql =
            "insert into orders (statement, price_sum, name, phone, address, other_info) values ("
        sql += "2, '${binding.totalPrice.text}', '${binding.nameEdit.text}', '${binding.phoneEdit.text}', '${binding.addressEdit.text}', '${binding.otherEdit.text}')"
        if (pay == "微信支付") {
            view.findViewById<TextView>(R.id.payment).text = pay
            view.findViewById<ImageView>(R.id.pay_img).setImageResource(R.drawable.wechat)
            view.findViewById<ImageView>(R.id.pay_img)
                .setColorFilter(ContextCompat.getColor(this, R.color.wechat))
            view.findViewById<MaterialButton>(R.id.cancel_btn)
                .setBackgroundColor(ContextCompat.getColor(this, R.color.wechat))
            view.findViewById<MaterialButton>(R.id.pay_btn)
                .setBackgroundColor(ContextCompat.getColor(this, R.color.wechat))
        }

        view.findViewById<MaterialButton>(R.id.pay_btn).setOnClickListener {
            // 已支付，状态码，2
            commitOrders(sql, 0)
            mHandler.sendEmptyMessage(1)
        }
        view.findViewById<MaterialButton>(R.id.cancel_btn).setOnClickListener {
            // 未支付，状态码，1
            commitOrders(sql, 1)
            mHandler.sendEmptyMessage(1)
        }
        try {
            view.findViewById<TextView>(R.id.price_total).text = price
            bottomSheetDialog = BottomSheetDialog(this)
            bottomSheetDialog.setContentView(view)
            bottomSheetDialog.show()
        } catch (e: Throwable) {
            Log.e("ERROR", e.toString())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (this::bottomSheetDialog.isInitialized) {
            if (bottomSheetDialog.isShowing) {
                bottomSheetDialog.dismiss()
            }
        }
    }

    private fun commitOrders(sql1: String, tag: Int) {
        var sql = sql1
        if (tag == 1) {
            // not paid yet
            sql = sql.replaceFirst('2', '1')
        }
        thread {
            try {
                var orderId = 1
                val mysql = MySQL()
                mysql.connect()
//                    MySQL.ps = MySQL.cn?.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
//                    var result = 1
//                    val result = MySQL.ps?.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS)
                var result = MySQL.ps?.executeUpdate(sql)
                if (result != null) {
                    if (result > 0) {
                        thread {
                            try {
                                mysql.connect()
                                val resultSet =
                                    MySQL.ps?.executeQuery("select max(order_id) from orders;")
                                if (resultSet != null && resultSet.next()) {
                                    orderId = resultSet.getInt(1)
                                }
                                // add to MySQL order_item
                                sql =
                                    "insert into order_item(item_id, order_id, price, buy_number) values "
                                for (item_i in itemList) {
                                    sql += "(${item_i.id}, ${orderId}, ${item_i.price}, ${item_i.number}), "
                                }
                                sql = sql.dropLast(2)
                                thread {
                                    try {
                                        mysql.connect()
                                        result = MySQL.ps?.executeUpdate(sql)
                                        if (result!! > 0) {
                                            mHandler.sendEmptyMessage(3)
                                        }
                                    } catch (e: com.mysql.jdbc.MysqlDataTruncation) {
                                        Log.e("ERROR", "ERROR141258")
                                    }
                                }
                            } catch (e: com.mysql.jdbc.MysqlDataTruncation) {
                                Log.e("ERROR", "ERROR141258")
                            }
                        }
                    } else {
                        mHandler.sendEmptyMessage(4)
                    }
                }
            } catch (e: com.mysql.jdbc.MysqlDataTruncation) {
                Log.e("ERROR", "ERROR141258")
            }
        }
    }

    private fun updateDetails() {
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