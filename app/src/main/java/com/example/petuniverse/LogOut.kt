package com.example.petuniverse

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.w3c.dom.Text

class LogOut : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_out)
        supportActionBar?.hide()

        auth = Firebase.auth
        val user  = auth.currentUser
        val deleteUser: TextView = findViewById(R.id.deleteUser)
        deleteUser.setOnClickListener {
                GetPassword()
        }

        val logOutUser: TextView = findViewById(R.id.log_out_user)
        logOutUser.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this,MainActivity::class.java))
        }
    }

    private fun GetPassword()  {
        val inputEditTextField = EditText(this)
        val dialog = AlertDialog.Builder(this)
            .setTitle("Enter the password to confirm")
            .setMessage("Password")
            .setView(inputEditTextField)
            .setPositiveButton("OK") { _, _ ->
                val password = inputEditTextField .text.toString()
                verifyCredentialsAndDeleteAccount(password)

            }
            .setNegativeButton("Cancel", null)
            .create()
        dialog.show()

    }

    private fun verifyCredentialsAndDeleteAccount(password: String) {
        val user  = auth.currentUser
        val credential = EmailAuthProvider
            .getCredential(user?.email.toString(),password)
        user!!.reauthenticate(credential)
            .addOnSuccessListener {
                Log.d("Reauthenticate", "User re-authenticated.")
                user.delete()
                    .addOnSuccessListener {
                        ToastMessage("Deleted successfully")
                        startActivity(Intent(this,MainActivity::class.java))
                    }
                    .addOnFailureListener {
                        ToastMessage(it.message.toString())
                    }
                }
                .addOnFailureListener {
                    ToastMessage(it.message.toString())
                }

    }


    private fun ToastMessage(message: String) {
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show()
    }
}