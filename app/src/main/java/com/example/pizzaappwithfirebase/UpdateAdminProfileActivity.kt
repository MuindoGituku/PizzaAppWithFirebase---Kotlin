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
import com.example.pizzaappwithfirebase.databinding.ActivityUpdateAdminProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class UpdateAdminProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateAdminProfileBinding
    private lateinit var firebaseFirestore: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var updatedAdminModel: Map<String,String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUpdateAdminProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        firebaseFirestore = FirebaseFirestore.getInstance()

        val adminDocumentReference = firebaseFirestore.collection("admins").document(firebaseAuth.currentUser!!.uid)

        adminDocumentReference.get().addOnSuccessListener {
            if (it.exists()){
                binding.adminFirstNameUpdEdit.setText(it.data!!["firstname"].toString())
                binding.adminLastNameUpdEdit.setText(it.data!!["lastname"].toString())
                binding.adminUsernameUpdEdit.setText(it.data!!["userName"].toString())
            }
            else{
                Log.d("Current Admin", "No such admin profile")
            }
        }.addOnFailureListener {
            Log.d("Current Admin", "Fetching an admin profile failed with exception, $it")
        }

        binding.adminUpdateButton.setOnClickListener {
            val newFirstName = binding.adminFirstNameUpdEdit.text.trim().toString()
            val newLastName = binding.adminLastNameUpdEdit.text.trim().toString()
            val newUserName = binding.adminUsernameUpdEdit.text.trim().toString()

            if (newFirstName.isNotEmpty()&&newLastName.isNotEmpty()&&newUserName.isNotEmpty()){
                updatedAdminModel = mapOf("firstname" to newFirstName,"lastname" to newLastName,"userName" to newUserName)
                adminDocumentReference.update(updatedAdminModel)
                    .addOnSuccessListener {
                    Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, AdminDashboardActivity::class.java)
                        startActivity(intent)
                }.addOnFailureListener {
                    Log.d("Admin Profile Update","Failed to update the admin profile $it")
                }

            }
            else{
                if (newFirstName.isEmpty()){
                    binding.adminFirstNameUpdEdit.error = "Provide a First Name"
                }
                if (newLastName.isEmpty()){
                    binding.adminLastNameUpdEdit.error = "Provide a Last Name"
                }
                if (newUserName.isEmpty()){
                    binding.adminUsernameUpdEdit.error = "Provide a Username"
                }
            }
        }
    }
}
