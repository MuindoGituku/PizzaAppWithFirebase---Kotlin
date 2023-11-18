package com.example.pizzaappwithfirebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.pizzaappwithfirebase.databinding.ActivityCustomerDashboardBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CustomerDashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCustomerDashboardBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseFirestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCustomerDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        firebaseFirestore = FirebaseFirestore.getInstance()

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


        binding.customerDashLogoutButton.setOnClickListener {
            firebaseAuth.signOut()

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}