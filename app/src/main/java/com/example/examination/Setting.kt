package com.example.examination

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.widget.Toast
import com.example.examination.databinding.ActivitySettingBinding
import java.sql.SQLException
import kotlin.concurrent.thread

class Setting : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
                    1 -> {
                        Toast.makeText(this@Setting, "更新成功", Toast.LENGTH_SHORT).show()
                    }
                    2 -> {
                        Toast.makeText(this@Setting, "更新失败", Toast.LENGTH_SHORT).show()
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
        binding.save.isEnabled = false
        binding.edit.setOnClickListener {
            binding.nameEdit.isEnabled = true
            binding.phoneEdit.isEnabled = true
            binding.addressEdit.isEnabled = true
            binding.otherEdit.isEnabled = true
            binding.payEdit.isEnabled = true
            binding.save.isEnabled = true
        }
        binding.save.setOnClickListener {
            val sql =
                "update user_info set name='${binding.nameEdit.text}', phone='${binding.phoneEdit.text}', address='${binding.addressEdit.text}', other_info='${binding.otherEdit.text}', pay_method='${binding.payEdit.text}' where iduser_info=1;"
            thread {
                val mysql = MySQL()
                mysql.connect()
                if (MySQL.ps?.executeUpdate(sql)!! > 0) {
                    mHandler.sendEmptyMessage(1)
                } else {
                    mHandler.sendEmptyMessage(2)
                }
            }
            binding.nameEdit.isEnabled = false
            binding.phoneEdit.isEnabled = false
            binding.addressEdit.isEnabled = false
            binding.otherEdit.isEnabled = false
            binding.payEdit.isEnabled = false
            binding.save.isEnabled = false
        }
    }
}