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
import android.widget.Toast
import com.example.pizzaappwithfirebase.databinding.ActivityUpdateCustomerProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class UpdateCustomerProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateCustomerProfileBinding
    private lateinit var firebaseFirestore: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var updatedCustomerModel:Map<String,String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUpdateCustomerProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        firebaseFirestore = FirebaseFirestore.getInstance()

        val customerDocumentReference = firebaseFirestore.collection("customers").document(firebaseAuth.currentUser!!.uid)

        customerDocumentReference.get().addOnSuccessListener {
            if (it.exists()){
                binding.customerFirstNameUpdEdit.setText(it.data!!["firstname"].toString())
                binding.customerLastNameUpdEdit.setText(it.data!!["lastname"].toString())
                binding.customerUsernameUpdEdit.setText(it.data!!["userName"].toString())
                binding.customerAddressUpdEdit.setText(it.data!!["homeAddress"].toString())
                binding.customerCityUpdEdit.setText(it.data!!["city"].toString())
                binding.customerPostalUpdEdit.setText(it.data!!["postalCode"].toString())
            }
            else{
                Log.d("Current Customer", "No such customer profile")
            }
        }.addOnFailureListener {
            Log.d("Current Admin", "Fetching an admin profile failed with exception, $it")
        }

        binding.customerUpdateButton.setOnClickListener {
            val newFirstName = binding.customerFirstNameUpdEdit.text.trim().toString()
            val newLastName = binding.customerLastNameUpdEdit.text.trim().toString()
            val newUsername = binding.customerUsernameUpdEdit.text.trim().toString()
            val newHomeAddress = binding.customerAddressUpdEdit.text.trim().toString()
            val newCity = binding.customerCityUpdEdit.text.trim().toString()
            val newPostalCode = binding.customerPostalUpdEdit.text.trim().toString()

            if (newFirstName.isNotEmpty()&&newLastName.isNotEmpty()&&newUsername.isNotEmpty()&&newHomeAddress.isNotEmpty()&&newCity.isNotEmpty()&&newPostalCode.isNotEmpty()){
                updatedCustomerModel = mapOf("firstname" to newFirstName,"lastname" to newLastName,"userName" to newUsername,"homeAddress" to newHomeAddress,"city" to newCity,"postalCode" to newPostalCode)

                customerDocumentReference.update(updatedCustomerModel).addOnSuccessListener {
                    Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show()
                    finish()
                }.addOnFailureListener {
                    Log.d("Customer Profile Update","Failed to update the customer profile $it")
                }

            }
            else{
                if (newFirstName.isEmpty()){
                    binding.customerFirstNameUpdEdit.error = "Provide a First Name"
                }
                if (newLastName.isEmpty()){
                    binding.customerLastNameUpdEdit.error = "Provide a Last Name"
                }
                if (newUsername.isEmpty()){
                    binding.customerUsernameUpdEdit.error = "Provide a Username"
                }
                if (newHomeAddress.isEmpty()){
                    binding.customerAddressUpdEdit.error = "Provide your Home Address"
                }
                if (newCity.isEmpty()){
                    binding.customerCityUpdEdit.error = "Provide your City"
                }
                if (newPostalCode.isEmpty()){
                    binding.customerPostalUpdEdit.error = "Provide your Postal Code"
                }
            }
        }
    }
}