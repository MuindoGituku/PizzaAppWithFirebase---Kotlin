package com.example.pizzaappwithfirebase.models

class PizzaModel (
    val pizzaName:String,
    val price:Double,
    val category:String,
) {
    var id: String = ""
    // Add a default (no-argument) constructor
    constructor() : this("", 0.0, "")
}