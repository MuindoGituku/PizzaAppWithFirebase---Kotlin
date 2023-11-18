package com.example.pizzaappwithfirebase.models

import com.google.type.Date

class OrderModel(
    val customerID:String,
    val productID:String,
    val orderDate: Date,
    val status:String,
)