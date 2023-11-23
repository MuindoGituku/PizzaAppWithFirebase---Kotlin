/**
 * @Group 1
 * @author Muindo Gituku (301372521)
 * @author Emre Deniz (301371047)
 * @date Nov 24, 2023
 * @description: Android Assignment 4
 */

package com.example.pizzaappwithfirebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.view.isVisible
import com.example.pizzaappwithfirebase.databinding.ActivityOrderDeliverBinding
import com.example.pizzaappwithfirebase.models.CustomerModel
import com.example.pizzaappwithfirebase.models.OrderModel
import com.example.pizzaappwithfirebase.models.PizzaModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import java.text.SimpleDateFormat

class OrderDeliverActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOrderDeliverBinding
    private lateinit var firebaseFirestore: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var selectedOrderID: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_deliver)

        binding = ActivityOrderDeliverBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        firebaseFirestore = FirebaseFirestore.getInstance()

        val intent = intent
        selectedOrderID = intent.getStringExtra("selected_order_id").toString()

        val orderDocumentReference = firebaseFirestore.collection("orders").document(selectedOrderID).get()

        orderDocumentReference.addOnSuccessListener { orderSnapshot ->
            if (orderSnapshot.exists()){
                val orderObject = orderSnapshot.toObject<OrderModel>()

                binding.deliverOrderButton.isVisible = orderObject!!.status=="In Progress"

                val customerDocumentReference = firebaseFirestore.collection("customers").document(orderObject!!.customerID).get()
                val pizzasDocumentReference = firebaseFirestore.collection("pizzas").document(orderObject.productID).get()

                customerDocumentReference.addOnSuccessListener {customerSnapshot->
                    if (customerSnapshot.exists()){
                        val orderCustomer = customerSnapshot.toObject<CustomerModel>()

                        binding.customerEmailAddressText.text = orderCustomer!!.emailAddress
                        binding.customerFullNamesText.text = "${orderCustomer.firstname} ${orderCustomer.lastname}"
                        binding.customerFullAddressText.text = "${orderCustomer.homeAddress}, ${orderCustomer.city} ${orderCustomer.postalCode}"
                    }
                    else{
                        binding.customerEmailAddressText.text = "No Customer Found!!"
                        binding.customerFullNamesText.text = "No Customer Found!!"
                        binding.customerFullAddressText.text = "No Customer Found!!"
                    }

                }.addOnFailureListener {
                    Log.d("Order in View", "Fetching the order info failed with exception, $it")
                }

                pizzasDocumentReference.addOnSuccessListener {pizzaSnapshot->
                    if (pizzaSnapshot.exists()){
                        val orderPizza = pizzaSnapshot.toObject<PizzaModel>()

                        binding.orderPizzaName.text = orderPizza!!.pizzaName
                        binding.orderPizzaCategory.text = orderPizza.category
                        binding.orderPizzaPrice.text = "$ ${orderPizza.price.toString()}"
                    }
                    else{
                        binding.orderPizzaName.text = "Pizza Not Found"
                        binding.orderPizzaCategory.text = "Pizza Not Found"
                        binding.orderPizzaPrice.text = "Null"
                    }
                }.addOnFailureListener {
                    Log.d("Order in View", "Fetching the order info failed with exception, $it")
                }
                binding.orderStatusText.text = orderObject.status
                binding.orderQuantityText.text = orderObject.orderAmount.toString()
                binding.orderTotalPriceText.text = "$ ${orderObject.orderPrice.toString()}"
                binding.orderDateText.text = SimpleDateFormat("YYYY/MM/DD hh:mm:ss").format(orderObject.orderDate.seconds * 1000L)
            }
            else{
                Log.d("Order in View", "No such order entry found")
            }
        }.addOnFailureListener {
            Log.d("Order in View", "Fetching the order info failed with exception, $it")
        }



        // Deliver button
        binding.deliverOrderButton.setOnClickListener {
            firebaseFirestore.collection("orders").document(selectedOrderID).update(mapOf("status" to "Delivery"))
                .addOnSuccessListener {
                    Toast.makeText(this, "Order status updated successfully", Toast.LENGTH_SHORT).show()
                    finish()
                }.addOnFailureListener {
                    Log.d("Order Status Update","Failed to update the order status $it")
                }
        }
    }
}
