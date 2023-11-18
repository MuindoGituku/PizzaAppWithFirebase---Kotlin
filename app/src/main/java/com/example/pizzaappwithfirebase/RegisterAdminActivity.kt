package com.example.pizzaappwithfirebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.pizzaappwithfirebase.databinding.ActivityRegisterAdminBinding
import com.example.pizzaappwithfirebase.models.AdminModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterAdminActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterAdminBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseFirestore: FirebaseFirestore
    private lateinit var newAdminProfile:AdminModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        firebaseFirestore = FirebaseFirestore.getInstance()

        binding.adminRegisterButton.setOnClickListener {
            val adminEmail = binding.adminEmailRegEdit.text.trim().toString()
            val adminFirstName = binding.adminFirstNameRegEdit.text.trim().toString()
            val adminLastName = binding.adminLastNameRegEdit.text.trim().toString()
            val adminUserName = binding.adminUsernameRegEdit.text.trim().toString()
            val adminPassword = binding.adminPassRegEdit.text.trim().toString()
            val adminConfirmPass = binding.adminConfirmPassRegEdit.text.trim().toString()

            if (adminEmail.isNotEmpty()&&adminFirstName.isNotEmpty()&&adminLastName.isNotEmpty()&&adminPassword.isNotEmpty()&&adminConfirmPass.isNotEmpty()&&adminEmail.contains("@admin.com",ignoreCase = true)){
                if (adminPassword==adminConfirmPass){
                    firebaseAuth.createUserWithEmailAndPassword(adminEmail,adminPassword).addOnCompleteListener {
                        if (it.isSuccessful){
                            newAdminProfile = AdminModel(userName = if (adminUserName.isNotEmpty())adminUserName else adminFirstName.toLowerCase()+"_"+adminLastName.toLowerCase(), firstname = adminFirstName, lastname = adminLastName, emailAddress = adminEmail)
                            firebaseFirestore.collection("admins").document(it.result.user!!.uid).set(newAdminProfile).addOnSuccessListener {
                                val intent = Intent(this, MainActivity::class.java)
                                startActivity(intent)
                            }.addOnFailureListener {
                                Log.d("Admin Profile Creation","Failed to create a new admin profile $it")
                            }
                        }
                        else{
                            Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                else{
                    binding.adminPassRegEdit.error = "Password does not match"
                    binding.adminConfirmPassRegEdit.error = "Password doe not match"
                }
            }
            else{
                if (adminEmail.isEmpty()){
                    binding.adminEmailRegEdit.error="Enter your Email Address"
                }
                if (adminEmail.isNotEmpty()&&!adminEmail.contains("@admin.com",ignoreCase = true)){
                    binding.adminEmailRegEdit.error="Format your Email Address correctly"
                }
                if (adminFirstName.isEmpty()){
                    binding.adminFirstNameRegEdit.error="Enter your First Name"
                }
                if (adminLastName.isEmpty()){
                    binding.adminLastNameRegEdit.error="Enter your Last Name"
                }
                if (adminPassword.isEmpty()){
                    binding.adminPassRegEdit.error="Enter your password"
                }
                if (adminConfirmPass.isEmpty()){
                    binding.adminConfirmPassRegEdit.error="Enter your password"
                }
            }
        }

        binding.adminLoginRedirectButton.setOnClickListener {
            val intent = Intent(this, LoginAdminActivity::class.java)
            startActivity(intent)
        }
    }
}