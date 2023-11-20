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
import com.example.pizzaappwithfirebase.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.loginCustomerDashButton.setOnClickListener {
            val intent = Intent(this, LoginCustomerActivity::class.java)
            startActivity(intent)
        }

        binding.loginAdminDashButton.setOnClickListener {
            val intent = Intent(this, LoginAdminActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()

        // Check if the user is already signed in, and if so, navigate to the home activity
        if (firebaseAuth.currentUser != null) {
            if (firebaseAuth.currentUser!!.email.toString().contains("@admin.com",ignoreCase = true)){
                val intent = Intent(this, AdminDashboardActivity::class.java)
                startActivity(intent)
            }
            else{
                val intent = Intent(this, CustomerDashboardActivity::class.java)
                startActivity(intent)
            }
        }
    }
}