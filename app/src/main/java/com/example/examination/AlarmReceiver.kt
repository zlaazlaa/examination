package com.example.examination

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.Message
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import kotlin.concurrent.thread

class AlarmReceiver : BroadcastReceiver() {
    val CHANNEL_ID = "12451"
    lateinit var context: Context
    var count = 0
    private val mHandler = @SuppressLint("HandlerLeak")
    object : Handler() {
        @SuppressLint("ResourceAsColor")
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                0 -> {
                    // Create an explicit intent for an Activity in your app
                    val intent = Intent(context, ShowOrders::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                    val pendingIntent: PendingIntent =
                        getActivity(context, 0, intent, FLAG_IMMUTABLE)

                    val builder = NotificationCompat.Builder(context, CHANNEL_ID)
                        .setSmallIcon(R.drawable.app_icon_new_round)
                        .setContentTitle("京淘APP")
                        .setContentText("您有 $count 笔订单未支付，请尽快支付呦~")
                        .setContentIntent(pendingIntent)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        // Set the intent that will fire when the user taps the notification
                        .setAutoCancel(true)
                    with(NotificationManagerCompat.from(context)) {
                        // notificationId is a unique int for each notification that you must define
                        notify(1242, builder.build())
                    }
                }
            }
        }
    }

    override fun onReceive(p0: Context?, p1: Intent?) {
        if (p0 != null) {
            context = p0
        }
        thread {
            val mysql = MySQL()
            mysql.connect()
            val sql = "select count(*) from orders where statement = 1;"
            val resultSet = MySQL.ps?.executeQuery(sql)
            if (resultSet != null) {
                while (resultSet.next()) {
                    count = resultSet.getInt("count(*)")
                    if (count > 0) {
                        mHandler.sendEmptyMessage(0)
                    }
                }
            }
        }
    }
}