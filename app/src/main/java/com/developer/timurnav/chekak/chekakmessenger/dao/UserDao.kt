package com.developer.timurnav.chekak.chekakmessenger.dao

import com.developer.timurnav.chekak.chekakmessenger.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class UserDao {

    fun storeUser(user: User, onSuccess: () -> Unit, onFailed: () -> Unit) {
        getUserRef()
                .setValue(user)
                .addOnCompleteListener {
                    if (it.isSuccessful) onSuccess() else onFailed()
                }
    }

    fun fetchUserData(onUserFetched: (User) -> Unit, onFailed: (String) -> Unit) {
        getUserRef().addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                onFailed(error.message)
            }

            override fun onDataChange(user: DataSnapshot) {
                onUserFetched(User(
                        name = user.child("name")!!.getValue(String::class.java)!!,
                        status = user.child("status")!!.getValue(String::class.java)!!,
                        image = user.child("image")!!.getValue(String::class.java)!!,
                        thumbImage = user.child("thumbImage")!!.getValue(String::class.java)!!
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

}