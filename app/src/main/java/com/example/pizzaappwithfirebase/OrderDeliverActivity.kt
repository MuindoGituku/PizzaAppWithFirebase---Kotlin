/**
 * @Group 1
 * @author Muindo Gituku (301372521)
 * @author Emre Deniz (301371047)
 * @date Nov 24, 2023
 * @description: Android Assignment 4
 */

package com.example.pizzaappwithfirebase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.pizzaappwithfirebase.databinding.ActivityOrderDeliverBinding
import com.example.pizzaappwithfirebase.models.OrderModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class OrderDeliverActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOrderDeliverBinding
    private lateinit var firebaseFirestore: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var selectedOrderID: String
    private lateinit var newOrderModel: OrderModel
    private var itemCount = 1

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
                Log.d("ORder in View", "No such order entry found")
            }
        }.addOnFailureListener {
            Log.d("Pizza in View", "Fetching the order info failed with exception, $it")
        }
    }
}
