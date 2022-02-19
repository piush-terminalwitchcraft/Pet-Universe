package com.example.petuniverse.data

data class TokenRequest(
    private val grant_type: String,
    private val client_id: String,
    private val client_secret: String
)