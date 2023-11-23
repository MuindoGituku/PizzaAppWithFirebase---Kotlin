package com.example.pizzaappwithfirebase

import CustomOrderAdapter
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pizzaappwithfirebase.databinding.ActivityCustomerOrdersBinding
import com.example.pizzaappwithfirebase.models.OrderModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject

class CustomerOrdersActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCustomerOrdersBinding
    private lateinit var firebaseFirestore: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var customOrderAdapter: CustomOrderAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCustomerOrdersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        firebaseFirestore = FirebaseFirestore.getInstance()

        // List orders
        customOrderAdapter = CustomOrderAdapter(emptyList()) { orderId ->

        }

        binding.seeCustomerOrdersListRecycleView.layoutManager = LinearLayoutManager(this)
        binding.seeCustomerOrdersListRecycleView.adapter = customOrderAdapter

        val customerOrdersQuerySnaphot = firebaseFirestore.collection("orders").whereEqualTo("customerID",firebaseAuth.currentUser!!.uid).get()

        customerOrdersQuerySnaphot.addOnSuccessListener {
            if (it.isEmpty){

            }
            else{
                val submittedOrders = mutableListOf<OrderModel>()
                for (orderRecord in it){
                    val singleOrder = orderRecord.toObject<OrderModel>()
                    singleOrder.id = orderRecord.id
                    submittedOrders.add(singleOrder)
                }
                customOrderAdapter.updateData(submittedOrders)
            }
        }.addOnFailureListener {
            Log.d("Fetching Customer Orders", "Fetching a list of orders made by this customer failed with exception, $it")
        }
    }
}