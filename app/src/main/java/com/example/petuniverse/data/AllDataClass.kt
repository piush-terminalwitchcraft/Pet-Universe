package com.example.petuniverse.models

enum class Status{
    PENDING,DONE,INITIAL,START
}
data class petsDetails(var user:String? = "", var price: Int? = 0,
                var picture: String? = "", var category: String? = "",
                var description: String? = "", var petName: String? = "",
                var gender: String? = "")
