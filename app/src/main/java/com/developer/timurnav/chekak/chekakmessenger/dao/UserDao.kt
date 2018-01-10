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
                onUserFetched(User(
                        name = user.child("name").value as String,
                        status = user.child("status").value as String,
                        email = auth().currentUser!!.email ?: "",
                        image = user.child("image").value as String,
                        thumbImage = user.child("thumbImage").value as String
                ))
            }
        })
    }

    private fun getUserRef(): DatabaseReference {
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        return FirebaseDatabase.getInstance().reference
                .child("Users")
                .child(userId)
    }

    private fun auth() = FirebaseAuth.getInstance()

}