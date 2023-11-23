package com.example.pizzaappwithfirebase

import CustomPizzasAdapter
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pizzaappwithfirebase.databinding.ActivityAllPizzasListBinding
import com.example.pizzaappwithfirebase.models.PizzaModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject

class AllPizzasListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAllPizzasListBinding
    private lateinit var firebaseFirestore: FirebaseFirestore
    private lateinit var customPizzasAdapter: CustomPizzasAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAllPizzasListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseFirestore = FirebaseFirestore.getInstance()

        // Initialize the adapter with an empty list
        customPizzasAdapter = CustomPizzasAdapter(emptyList()) { pizzaId ->
            // Handle item click, e.g., start a new activity with the selected pizza ID

        }
        binding.seePizzasListRecycleView.layoutManager = LinearLayoutManager(this)
        binding.seePizzasListRecycleView.adapter = customPizzasAdapter

        binding.goToAddNewPizzaButton.setOnClickListener {
            val intent = Intent(this, AddNewPizzaActivity::class.java)
            startActivity(intent)
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

    }
}