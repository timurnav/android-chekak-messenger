package com.developer.timurnav.chekak.chekakmessenger.dao

import com.developer.timurnav.chekak.chekakmessenger.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class UserDao {

    fun storeUser(user: User, onSuccess: () -> Unit = {}, onFailed: () -> Unit = {}) {
        getUserRef()
                .setValue(user)
                .addOnCompleteListener {
                    if (it.isSuccessful) onSuccess() else onFailed()
                }
    }

    fun fetchUserData(onUserFetched: (User) -> Unit = {}, onFailed: (String) -> Unit = {}) {
        getUserRef().addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                onFailed(error.message)
            }

            override fun onDataChange(user: DataSnapshot) {
                onUserFetched(mapUser(user))
            }
        })
    }

    fun fetchAllUsers(onFetched: (List<User>) -> Unit = {}, onFailed: (String) -> Unit = {}) {
        allUsersRef().addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                onFailed(error.message)
            }

            override fun onDataChange(usersSnapshot: DataSnapshot) {
                onFetched(
                        usersSnapshot.children
                                .map { mapUser(it) }
                )
            }
        })
    }

    private fun mapUser(user: DataSnapshot): User {
        return User(
                id = user.child("id").value as String,
                name = user.child("name").value as String,
                status = user.child("status").value as String,
                email = user.child("email").value as String,
                image = user.child("image").value as String,
                thumbImage = user.child("thumbImage").value as String
        )
    }

    private fun getUserRef(): DatabaseReference {
        return allUsersRef()
                .child(FirebaseAuth.getInstance().currentUser!!.uid)
    }

    private fun allUsersRef() = FirebaseDatabase.getInstance().reference
            .child("Users")

}