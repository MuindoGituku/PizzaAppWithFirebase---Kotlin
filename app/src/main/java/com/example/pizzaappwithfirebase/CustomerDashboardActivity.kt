/**
 * @Group 1
 * @author Muindo Gituku (301372521)
 * @author Emre Deniz (301371047)
 * @date Nov 24, 2023
 * @description: Android Assignment 4
 */


package com.example.pizzaappwithfirebase

import CustomPizzasAdapter
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pizzaappwithfirebase.databinding.ActivityCustomerDashboardBinding
import com.example.pizzaappwithfirebase.models.PizzaModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject

class CustomerDashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCustomerDashboardBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseFirestore: FirebaseFirestore
    private lateinit var customPizzasAdapter: CustomPizzasAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCustomerDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        firebaseFirestore = FirebaseFirestore.getInstance()

        // Initialize the adapter with an empty list
        customPizzasAdapter = CustomPizzasAdapter(emptyList()) { pizzaId ->
            // Handle item click, e.g., start a new activity with the selected pizza ID
            val intent = Intent(this, PizzaOrderActivity::class.java)
            intent.putExtra("selected_pizza_id", pizzaId)
            startActivity(intent)
        }
        binding.pizzasListRecycleView.layoutManager = LinearLayoutManager(this)
        binding.pizzasListRecycleView.adapter = customPizzasAdapter

        val customerDocumentReference = firebaseFirestore.collection("customers").document(firebaseAuth.currentUser!!.uid)

        customerDocumentReference.get().addOnSuccessListener {
            if (it.exists()){
                Log.d("Current Customer","Customer Profile ${it.data}")

                binding.customerUserNamesText.text = "Username: ${it.data!!["userName"]}"
                binding.customerEmailAddressText.text = "Email Address: ${it.data!!["emailAddress"]}"
                binding.customerFullNamesText.text = "${it.data!!["firstname"]} ${it.data!!["lastname"]}"
            }
            else {
                Log.d("Current Customer", "No such customer profile")
            }
        }.addOnFailureListener {
            Log.d("Current Customer", "Fetching a customer profile failed with exception, $it")
        }

        val pizzasQuerySnaphot = firebaseFirestore.collection("pizzas").get()


        pizzasQuerySnaphot.addOnSuccessListener {
            if (it.isEmpty){

            }
            else{
                val uploadedPizzas = mutableListOf<PizzaModel>()

                for (pizzaDoc in it){
                    val pizzaObject = pizzaDoc.toObject<PizzaModel>()
                    pizzaObject.id = pizzaDoc.id
                    uploadedPizzas.add(pizzaObject)
                }

                customPizzasAdapter.updateData(uploadedPizzas)
            }
        }.addOnFailureListener {
            Log.d("Fetching Pizzas", "Fetching a list of pizzas failed with exception, $it")
        }


        binding.customerDashLogoutButton.setOnClickListener {
            firebaseAuth.signOut()

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}