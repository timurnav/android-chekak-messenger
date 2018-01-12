package com.developer.timurnav.chekak.chekakmessenger.model

data class User(
        val id: String,
        val name: String,
        val status: String,
        val image: String = "",
        val thumbImage: String = "",
        val email: String
)