package com.example.petuniverse

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class EditPetDetails : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_pet_details)
        supportActionBar?.hide()
    }
}