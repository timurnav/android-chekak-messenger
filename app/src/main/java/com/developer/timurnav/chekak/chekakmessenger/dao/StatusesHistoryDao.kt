package com.developer.timurnav.chekak.chekakmessenger.dao

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class StatusesHistoryDao {

    fun addStatus(status: String, onSuccess: () -> Unit = {}, onFailed: () -> Unit = {}) {
        getStatusesRef()
                .push()
                .setValue(status)
                .addOnCompleteListener {
                    if (it.isSuccessful) onSuccess() else onFailed()
                }
    }

    fun fetchAll(onFetched: (List<String>) -> Unit = {}, onFailed: (String) -> Unit = {}) {
        getStatusesRef().addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                onFailed(error.message)
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val statuses = snapshot.children.map { it.value as String }
                onFetched(statuses)
            }
        })
    }


    private fun getStatusesRef(): DatabaseReference {
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        return FirebaseDatabase.getInstance().reference
                .child("Statuses")
                .child(userId)
    }

}