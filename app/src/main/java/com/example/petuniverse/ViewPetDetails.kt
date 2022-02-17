package com.example.petuniverse

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.petuniverse.models.petsDetails
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson

class ViewPetDetails : AppCompatActivity() {

    private lateinit var Auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var user_textview: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_pet_details)
        val gson = Gson()
        val petDetail = gson.fromJson<petsDetails>(intent.getStringExtra("PetDetail"),petsDetails::class.java)

        supportActionBar?.hide()

        firestore = FirebaseFirestore.getInstance()

        user_textview = findViewById(R.id.user_name_apd)
        firestore.collection("Users")
            .document(petDetail?.user.toString()).get().addOnSuccessListener {
                user_textview.text = it["username"].toString()
            }

    }
}