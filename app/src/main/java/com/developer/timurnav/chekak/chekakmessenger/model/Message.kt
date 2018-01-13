package com.developer.timurnav.chekak.chekakmessenger.model

data class Message(
        val id: String,
        val author: User,
        val text: String,
        val date: Long
)