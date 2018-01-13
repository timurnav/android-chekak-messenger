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
                onUserFetched(User.mapper(user))
            }
        })
    }

    fun listenUserById(userId: String, onUserUpdated: (User) -> Unit) {
        allUsersRef()
                .child(userId)
                .addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(error: DatabaseError) {
                    }

                    override fun onDataChange(snapshot: DataSnapshot) {
                        val user = User.mapper(snapshot)
                        onUserUpdated(user)
                    }
                })
    }

    fun fetchAllUsers(onFetched: (List<User>) -> Unit = {}, onFailed: (String) -> Unit = {}) {
        allUsersRef().addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                onFailed(error.message)
            }

            override fun onDataChange(usersSnapshot: DataSnapshot) {
                val users = usersSnapshot.children.map(User.Companion::mapper)
                onFetched(users)
            }
        })
    }

    private fun getUserRef(): DatabaseReference {
        return allUsersRef()
                .child(FirebaseAuth.getInstance().currentUser!!.uid)
    }

    private fun allUsersRef() = FirebaseDatabase.getInstance().reference
            .child("Users")

}