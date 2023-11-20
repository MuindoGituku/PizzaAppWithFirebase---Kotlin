/**
 * @Group 1
 * @author Muindo Gituku (301372521)
 * @author Emre Deniz (301371047)
 * @date Nov 24, 2023
 * @description: Android Assignment 4
 */

package com.example.pizzaappwithfirebase

import CustomOrderAdapter
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pizzaappwithfirebase.databinding.ActivityAdminDashboardBinding
import com.example.pizzaappwithfirebase.models.OrderModel
import com.example.pizzaappwithfirebase.models.PizzaModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject

class AdminDashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminDashboardBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseFirestore: FirebaseFirestore
    private lateinit var customOrderAdapter: CustomOrderAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Binding parameters
        binding = ActivityAdminDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Firebase connection
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseFirestore = FirebaseFirestore.getInstance()

        // Get current admin user info and show on the screen
        val adminDocumentReference = firebaseFirestore.collection("admins").document(firebaseAuth.currentUser!!.uid)
        adminDocumentReference.get().addOnSuccessListener {
            if (it.exists()){
                Log.d("Current Admin","Admin Profile ${it.data}")

                binding.adminUserNamesText.text = "Username: ${it.data!!["userName"]}"
                binding.adminEmailAddressText.text = "Email Address: ${it.data!!["emailAddress"]}"
                binding.adminFullNamesText.text = "${it.data!!["firstname"]} ${it.data!!["lastname"]}"
            }
            else {
                Log.d("Current Admin", "No such admin profile")
            }
        }.addOnFailureListener {
            Log.d("Current Admin", "Fetching an admin profile failed with exception, $it")
        }

        // List orders
        customOrderAdapter = CustomOrderAdapter(emptyList()) { orderId ->
            // Handle item click, e.g., start a new activity with the selected pizza ID
            val intent = Intent(this, OrderDeliverActivity::class.java)
            intent.putExtra("selected_order_id", orderId)
            startActivity(intent)
        }
        binding.pizzasListRecycleView.layoutManager = LinearLayoutManager(this)
        binding.pizzasListRecycleView.adapter = customOrderAdapter
        val pizzasQuerySnaphot = firebaseFirestore.collection("orders").get()
        pizzasQuerySnaphot.addOnSuccessListener {
            if (it.isEmpty){

            }
            else{
                val allOrders = mutableListOf<OrderModel>()

                for (order in it){
                    val orderObject = order.toObject<OrderModel>()
                    orderObject.id = order.id
                    allOrders.add(orderObject)
                }

                customOrderAdapter.updateData(allOrders)
            }
        }.addOnFailureListener {
            Log.d("Fetching Pizzas", "Fetching a list of pizzas failed with exception, $it")
        }

        // Logout button
        binding.adminDashLogoutButton.setOnClickListener {
            firebaseAuth.signOut()

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        // Update profile button
        binding.updateAdminProfileButton.setOnClickListener {
            val intent = Intent(this, UpdateAdminProfileActivity::class.java)
            startActivity(intent)
        }

        // Upload new pizza type button
        binding.uploadNewPizzaFAB.setOnClickListener {
            val intent = Intent(this, AddNewPizzaActivity::class.java)
            startActivity(intent)
        }
    }
}
