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
import com.example.pizzaappwithfirebase.databinding.ActivityRegisterCustomerBinding
import com.example.pizzaappwithfirebase.models.CustomerModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterCustomerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterCustomerBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseFirestore: FirebaseFirestore
    private lateinit var newCustomerProfile: CustomerModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterCustomerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        firebaseFirestore = FirebaseFirestore.getInstance()

        binding.customerRegisterButton.setOnClickListener {
            val customerEmail = binding.customerEmailRegEdit.text.trim().toString()
            val customerFirstName = binding.customerFirstNameRegEdit.text.trim().toString()
            val customerLastName = binding.customerLastNameRegEdit.text.trim().toString()
            val customerUserName = binding.customerUsernameRegEdit.text.trim().toString()
            val customerHomeAddress = binding.customerAddressRegEdit.text.trim().toString()
            val customerCity = binding.customerCityRegEdit.text.trim().toString()
            val customerPostalCode = binding.customerPostalRegEdit.text.trim().toString()
            val customerPassword = binding.customerPasswordRegEdit.text.trim().toString()
            val customerConfirmPass = binding.customerConfirmPasswordRegEdit.text.trim().toString()

            if (customerEmail.isNotEmpty()&&customerFirstName.isNotEmpty()&&customerLastName.isNotEmpty()&&customerHomeAddress.isNotEmpty()&&customerCity.isNotEmpty()&&customerPostalCode.isNotEmpty()&&customerPassword.isNotEmpty()&&customerConfirmPass.isNotEmpty()&&!customerEmail.contains("@admin.com",ignoreCase = true)){
                if (customerPassword==customerConfirmPass){
                    firebaseAuth.createUserWithEmailAndPassword(customerEmail,customerPassword).addOnCompleteListener {
                        if (it.isSuccessful){
                            newCustomerProfile = CustomerModel(userName = if (customerUserName.isNotEmpty())customerUserName else customerFirstName.toLowerCase()+"_"+customerLastName.toLowerCase(), firstname = customerFirstName, lastname = customerLastName, emailAddress = customerEmail, homeAddress = customerHomeAddress, city = customerCity, postalCode = customerPostalCode)
                            firebaseFirestore.collection("customers").document(it.result.user!!.uid).set(newCustomerProfile).addOnCompleteListener {
                                val intent = Intent(this, MainActivity::class.java)
                                startActivity(intent)
                            }.addOnFailureListener {
                                Log.d("Customer Profile Creation","Failed to create a new customer profile $it")
                            }
                        }
                        else{
                            Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                else{
                    binding.customerPasswordRegEdit.error = "Password does not match"
                    binding.customerConfirmPasswordRegEdit.error = "Password doe not match"
                }
            }
            else{
                if (customerEmail.isEmpty()){
                    binding.customerEmailRegEdit.error="Enter your Email Address"
                }
                if (customerEmail.isNotEmpty()&&customerEmail.contains("@admin.com",ignoreCase = true)){
                    binding.customerEmailRegEdit.error="Do not use your Admin Address"
                }
                if (customerFirstName.isEmpty()){
                    binding.customerFirstNameRegEdit.error="Enter your First Name"
                }
                if (customerLastName.isEmpty()){
                    binding.customerLastNameRegEdit.error="Enter your Last Name"
                }
                if (customerHomeAddress.isEmpty()){
                    binding.customerAddressRegEdit.error="Enter your Home Address"
                }
                if (customerCity.isEmpty()){
                    binding.customerCityRegEdit.error="Enter your City"
                }
                if (customerPostalCode.isEmpty()){
                    binding.customerPostalRegEdit.error="Enter your Postal Code"
                }
                if (customerPassword.isEmpty()){
                    binding.customerPasswordRegEdit.error="Enter your Password"
                }
                if (customerConfirmPass.isEmpty()){
                    binding.customerConfirmPasswordRegEdit.error="Confirm your Password"
                }
            }
        }

        binding.customerLoginRedirectButton.setOnClickListener {
            val intent = Intent(this, LoginCustomerActivity::class.java)
            startActivity(intent)
        }
    }
}