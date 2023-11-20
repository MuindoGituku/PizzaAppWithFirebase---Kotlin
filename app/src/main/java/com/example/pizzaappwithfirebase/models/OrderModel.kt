package com.example.pizzaappwithfirebase.models

import com.google.firebase.Timestamp
import java.util.Date

class OrderModel(
    val customerID:String,
    val productID:String,
    val orderAmount:Int,
    val orderPrice:Double,
    val orderDate: Timestamp,
    val status:String
) {
    var id: String = ""
    // Add a default (no-argument) constructor
    constructor() : this("", "", 0, 0.0, com.google.firebase.Timestamp(Date()), "")
}
