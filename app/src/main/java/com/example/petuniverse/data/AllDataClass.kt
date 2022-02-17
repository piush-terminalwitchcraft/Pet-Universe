package com.example.petuniverse.models

import com.example.petuniverse.R
import com.google.firebase.Timestamp


enum class Status{
    PENDING,DONE,INITIAL,START
}
data class petsDetails(var user:String? = "", var price: Int? = 0,
                var picture: String? = "", var category: String? = "",
                var description: String? = "", var petName: String? = "",
                var gender: String? = "", var timestamp: Timestamp? = null,
                var status: Int? = R.string.sold) {
}
