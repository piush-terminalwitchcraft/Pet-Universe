package com.example.petuniverse.Interfaces

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    val PetfinderApi: TodoInterface by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.petfinder.com/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TodoInterface::class.java)

    }

}