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
import android.widget.Toast
import com.example.pizzaappwithfirebase.databinding.ActivityLoginCustomerBinding
import com.google.firebase.auth.FirebaseAuth

class LoginCustomerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginCustomerBinding
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginCustomerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.customerLoginButton.setOnClickListener {
            val customerEmail = binding.customerEmailLogEdit.text.trim().toString()
            val customerPassword = binding.customerPassLogEdit.text.trim().toString()

            if (customerPassword.isNotEmpty()&&customerEmail.isNotEmpty()&&!customerEmail.contains("@admin.com",ignoreCase = true)){
                firebaseAuth.signInWithEmailAndPassword(customerEmail,customerPassword).addOnCompleteListener {
                    if (it.isSuccessful){
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    }
                    else{
                        Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            }
            else{
                if (customerEmail.isEmpty()){
                    binding.customerEmailLogEdit.error = "Enter your Email Address"
                }
                if (customerEmail.isNotEmpty()&&customerEmail.contains("@admin.com",ignoreCase = true)){
                    binding.customerEmailLogEdit.error = "Do not use your Admin Address"
                }
                if (customerPassword.isEmpty()){
                    binding.customerPassLogEdit.error = "Enter your Password"
                }
            }
        }

        binding.customerRegisterRedirectButton.setOnClickListener {
            val intent = Intent(this, RegisterCustomerActivity::class.java)
            startActivity(intent)
        }
    }
}
