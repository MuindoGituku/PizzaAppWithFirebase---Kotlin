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
import com.example.pizzaappwithfirebase.databinding.ActivityAdminDashboardBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AdminDashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminDashboardBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseFirestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAdminDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        firebaseFirestore = FirebaseFirestore.getInstance()

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

        binding.adminDashLogoutButton.setOnClickListener {
            firebaseAuth.signOut()

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        binding.updateAdminProfileButton.setOnClickListener {
            val intent = Intent(this, UpdateAdminProfileActivity::class.java)
            startActivity(intent)
        }

        binding.uploadNewPizzaFAB.setOnClickListener {
            val intent = Intent(this, AddNewPizzaActivity::class.java)
            startActivity(intent)
        }
    }
}