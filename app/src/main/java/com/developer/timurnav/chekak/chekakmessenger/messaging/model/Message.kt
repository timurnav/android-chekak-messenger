package com.developer.timurnav.chekak.chekakmessenger.messaging.model

import com.google.firebase.database.DataSnapshot

data class Message(
        val id: String,
        val authorId: String,
        val message: String,
        val timestamp: Long
) {
    companion object {
        fun mapper(snapshot: DataSnapshot): Message {
            return Message(
                    id = snapshot.child("id").value as String,
                    authorId = snapshot.child("authorId").value as String,
                    message = snapshot.child("message").value as String,
                    timestamp = snapshot.child("timestamp").value as Long
            )
        }
    }
}