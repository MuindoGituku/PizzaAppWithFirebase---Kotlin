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
import android.widget.Toast
import com.example.pizzaappwithfirebase.databinding.ActivityAddNewPizzaBinding
import com.example.pizzaappwithfirebase.models.PizzaModel
import com.google.firebase.firestore.FirebaseFirestore

class AddNewPizzaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddNewPizzaBinding
    private lateinit var firebaseFirestore: FirebaseFirestore
    private lateinit var newPizzaEntry:PizzaModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddNewPizzaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseFirestore = FirebaseFirestore.getInstance()

        binding.addPizzaUploadButton.setOnClickListener {
            val pizzaName = binding.pizzaNameAddEdit.text.trim().toString()
            val pizzaCategory = binding.pizzaCategoryAddEdit.text.trim().toString()
            val pizzaPrice = binding.pizzaPriceAddEdit.text.trim().toString()

            if (pizzaName.isNotEmpty()&&pizzaCategory.isNotEmpty()&&pizzaPrice.isNotEmpty()){
                newPizzaEntry = PizzaModel(pizzaName=pizzaName, price = pizzaPrice.toDouble(), category = pizzaCategory)
                firebaseFirestore.collection("pizzas").add(newPizzaEntry).addOnSuccessListener {
                    binding.pizzaNameAddEdit.text.clear()
                    binding.pizzaPriceAddEdit.text.clear()
                    binding.pizzaCategoryAddEdit.text.clear()

                    Toast.makeText(this, "Pizza Added successfully", Toast.LENGTH_SHORT).show()
                    finish()


                }.addOnFailureListener {
                    Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
                }
            }
            else{
                if (pizzaName.isEmpty()){
                    binding.pizzaNameAddEdit.error="Enter the pizza name"
                }
                if (pizzaPrice.isEmpty()){
                    binding.pizzaPriceAddEdit.error="Enter the pizza price"
                }
                if (pizzaCategory.isEmpty()){
                    binding.pizzaCategoryAddEdit.error="Enter the pizza category"
                }
            }
        }
    }
}