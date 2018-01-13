package com.developer.timurnav.chekak.chekakmessenger.model

data class PrivateChat(
        val users: List<User>,
        val messages: List<Message>
)