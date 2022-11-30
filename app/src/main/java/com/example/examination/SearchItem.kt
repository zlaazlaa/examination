package com.example.examination

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import java.sql.SQLException
import kotlin.concurrent.thread

class SearchItem : AppCompatActivity() {
    private val itemList = ArrayList<item>()
    lateinit var adapter: ItemAdapter
    private val mHandler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                0 -> {
                    try {
                        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
                        val layoutManager = LinearLayoutManager(this@SearchItem)
                        recyclerView.layoutManager = layoutManager
                        adapter = ItemAdapter(
                            itemList,
                            this@SearchItem,
                            0,
                            null
                        )
                        recyclerView.adapter = adapter
                    } catch (e: java.lang.NullPointerException) {
                        Log.e("error", "java.lang.NullPointerException")
                    }
                }
            }
        }
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_item)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.title = "商品搜索"
        }
        val searchTxt = intent.getStringExtra("search_txt")
        findViewById<MaterialButton>(R.id.search_btn).setOnClickListener {
            itemList.clear()
            search(findViewById<EditText>(R.id.search_txt).text.toString())
        }
        if (searchTxt != null) {
            findViewById<EditText>(R.id.search_txt).setText(searchTxt)
            search(searchTxt)
        }
    }

    private fun search(txt_origin: String) {
        var txt = txt_origin
        if (txt == "") {
            txt = "B20090611"
        }
        thread {
            try {
                val mysql = MySQL()
                mysql.connect()
                val sql = "select * from items where item_name like '%$txt%';"
                val resultSet = MySQL.ps?.executeQuery(sql)
                if (resultSet != null) {
                    try {
                        itemList.clear()
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
                        mHandler.sendEmptyMessage(0)
                    } catch (e: NullPointerException) {
                        Log.e("MySQL", "nullPointerERROR")
                    } catch (e: SQLException) {
                        Log.e("MySQL", "Operation not allowed after ResultSet closed")
                    }
                }
            } finally {

            }
        }
    }
}