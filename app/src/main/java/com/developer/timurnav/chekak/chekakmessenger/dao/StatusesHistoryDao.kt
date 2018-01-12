package com.developer.timurnav.chekak.chekakmessenger.dao

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class StatusesHistoryDao {

    fun addStatus(status: String, onSuccess: () -> Unit = {}, onFailed: () -> Unit = {}) {
        getStatusesRef()
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
                Log.d("Chetatam", snapshot.toString())
                onFetched(listOf())
            }
        })
    }


    private fun getStatusesRef(): DatabaseReference {
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        return FirebaseDatabase.getInstance().reference
                .child("Statuses")
                .child(userId)
                .push()
    }

}