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
import com.example.pizzaappwithfirebase.databinding.ActivityLoginAdminBinding
import com.google.firebase.auth.FirebaseAuth

class LoginAdminActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginAdminBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        // Login button
        binding.adminLoginButton.setOnClickListener {
            val adminEmail = binding.adminEmailLogEdit.text.trim().toString()
            val adminPassword = binding.adminPassLogEdit.text.trim().toString()

            if (adminEmail.isNotEmpty()&&adminPassword.isNotEmpty()&&adminEmail.contains("@admin.com",ignoreCase = true)){
                firebaseAuth.signInWithEmailAndPassword(adminEmail,adminPassword).addOnCompleteListener {
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
                if (adminEmail.isEmpty()){
                    binding.adminEmailLogEdit.error = "Enter your Email Address"
                }
                if (adminEmail.isNotEmpty()&&!adminEmail.contains("@admin.com",ignoreCase = true)){
                    binding.adminEmailLogEdit.error = "Format your Email Address correctly"
                }
                if (adminPassword.isEmpty()){
                    binding.adminEmailLogEdit.error = "Enter your Password"
                }
            }
        }

        // Register button
        binding.adminRegisterRedirectButton.setOnClickListener {
            val intent = Intent(this, RegisterAdminActivity::class.java)
            startActivity(intent)
        }
    }
}
