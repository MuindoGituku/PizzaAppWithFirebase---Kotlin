package com.example.pizzaappwithfirebase.models

import com.google.firebase.Timestamp

class OrderModel(
    val customerID:String,
    val productID:String,
    val orderAmount:Int,
    val orderPrice:Double,
    val orderDate: Timestamp,
    val status:String,
)