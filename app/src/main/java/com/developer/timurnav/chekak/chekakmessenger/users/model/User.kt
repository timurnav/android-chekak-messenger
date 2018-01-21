package com.developer.timurnav.chekak.chekakmessenger.users.model

import com.google.firebase.database.DataSnapshot

data class User(
        val id: String,
        val name: String,
        val status: String,
        val image: String = "",
        val thumbImage: String = "",
        val email: String
) {
    companion object {
        fun mapper(snapshot: DataSnapshot): User {
            return User(
                    id = snapshot.child("id").value as String,
                    name = snapshot.child("name").value as String,
                    status = snapshot.child("status").value as String,
                    email = snapshot.child("email").value as String,
                    image = snapshot.child("image").value as String,
                    thumbImage = snapshot.child("thumbImage").value as String
            )
        }
    }
}