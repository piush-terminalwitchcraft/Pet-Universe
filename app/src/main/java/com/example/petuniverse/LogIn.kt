package com.example.petuniverse

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LogIn : AppCompatActivity() {
    private lateinit var email : TextInputEditText
    private lateinit var password : TextInputEditText
    private lateinit var logIn : TextView
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)
        supportActionBar?.hide()
        auth = Firebase.auth
        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
        logIn = findViewById(R.id.LogIn)
        logIn.setOnClickListener {
            logInUser()
        }

    }

    private fun logInUser() {
        if(validateFields()){
            auth.signInWithEmailAndPassword(email.text.toString(),password.text.toString())
                .addOnSuccessListener {
                    startActivity(Intent(this,MainActivity::class.java))
                }
                .addOnFailureListener {
                    ToastMessage(it.message.toString())
                }
        }
    }

    private fun validateFields(): Boolean {
        return when {
            email.text.isNullOrBlank() -> {
                ToastMessage("Email is required")
                return false
            }
            password.text.isNullOrBlank() -> {
                ToastMessage("Password is required")
                return false
            }
            else -> return true
        }
    }

    private fun ToastMessage(message: String) {
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show()
    }
}