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
import com.example.pizzaappwithfirebase.databinding.ActivityPizzaOrderBinding
import com.example.pizzaappwithfirebase.models.OrderModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Date

class PizzaOrderActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPizzaOrderBinding
    private lateinit var firebaseFirestore: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var selectedPizzaID: String
    private lateinit var newOrderModel: OrderModel
    private var itemCount = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPizzaOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        firebaseFirestore = FirebaseFirestore.getInstance()

        val intent = intent
        selectedPizzaID = intent.getStringExtra("selected_pizza_id").toString()

        val pizzaDocumentReference = firebaseFirestore.collection("pizzas").document(selectedPizzaID)

        pizzaDocumentReference.get().addOnSuccessListener {
            if (it.exists()){
                binding.pizzaNameText.text = it.data!!["pizzaName"].toString()
                binding.pizzaCategoryText.text = it.data!!["category"].toString()
                binding.pizzaPriceText.text = it.data!!["price"].toString()
                binding.pizzaChargeText.text = it.data!!["price"].toString()
                binding.orderAmountText.text = itemCount.toString()
            }
            else{
                Log.d("Pizza in View", "No such pizza entry found")
            }
        }.addOnFailureListener {
            Log.d("Pizza in View", "Fetching the pizza info failed with exception, $it")
        }

        binding.increaseOrderButton.setOnClickListener {
            itemCount++
            updateTotalOrder()
        }

        binding.decreaseOrderButton.setOnClickListener {
            if (itemCount > 1) {
                itemCount--
                updateTotalOrder()
            }
        }

        binding.placeOrderButton.setOnClickListener {
            val customerID = firebaseAuth.currentUser!!.uid
            val productID = selectedPizzaID
            val orderAmount = binding.orderAmountText.text.toString()
            val orderPrice = binding.pizzaChargeText.text.toString()
            val orderDate = com.google.firebase.Timestamp(Date())
            val orderStatus = "In Progress"

            newOrderModel = OrderModel(customerID=customerID, productID = productID, orderAmount = orderAmount.toInt(), orderDate = orderDate, orderPrice = orderPrice.toDouble(), status = orderStatus)

            firebaseFirestore.collection("orders").add(newOrderModel).addOnSuccessListener {
                Toast.makeText(this, "Order placed successfully", Toast.LENGTH_SHORT).show()
                finish()
            }.addOnFailureListener {
                Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateTotalOrder() {
        val totalOrder = itemCount * binding.pizzaPriceText.text.toString().toDouble()
        binding.pizzaChargeText.text = totalOrder.toString()
        binding.orderAmountText.text = itemCount.toString()
    }
}
