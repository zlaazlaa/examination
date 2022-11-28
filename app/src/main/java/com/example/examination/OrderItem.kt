package com.example.examination

class OrderItem(
    val idOrderItem: Int,
    val itemId: Int,
    val price: String,
    val buyNumber: Int,
    val shopName: String,
    val orderStatus: Int,
    val itemName: String,
    val itemImgUrl: String,
    var priceTotal: String,
    val score: Int,
    val orderId: Int
)