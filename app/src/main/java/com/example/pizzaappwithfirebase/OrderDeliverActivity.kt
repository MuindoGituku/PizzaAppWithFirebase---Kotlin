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
import com.example.pizzaappwithfirebase.databinding.ActivityOrderDeliverBinding
import com.example.pizzaappwithfirebase.models.OrderModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

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

        val orderDocumentReference = firebaseFirestore.collection("orders").document(selectedOrderID)

        orderDocumentReference.get().addOnSuccessListener {
            if (it.exists()){
                binding.orderIdText.text = intent.getStringExtra("selected_order_id").toString()
                binding.orderStatusText.text = it.data!!["status"].toString()
                binding.orderPriceText.text = it.data!!["orderPrice"].toString()
            }
            else{
                Log.d("Order in View", "No such order entry found")
            }
        }.addOnFailureListener {
            Log.d("Order in View", "Fetching the order info failed with exception, $it")
        }

        binding.deliverOrderButton.setOnClickListener {
            orderDocumentReference.update(mapOf("status" to "Delivery"))
                .addOnSuccessListener {
                    Toast.makeText(this, "Order status updated successfully", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, AdminDashboardActivity::class.java)
                    startActivity(intent)
                }.addOnFailureListener {
                    Log.d("Order Status Update","Failed to update the order status $it")
                }
        }
    }
}
