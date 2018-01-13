package com.developer.timurnav.chekak.chekakmessenger.dao

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PrivateChatDao {

    fun fetchChatIdWithUser(userId: String, onFetched: (String) -> Unit) {
        allPrivateChatsMappingRef()
                .child(FirebaseAuth.getInstance().currentUser!!.uid)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(error: DatabaseError) {
                        //hz
                    }

                    override fun onDataChange(allPrivateChats: DataSnapshot) {
                        val chat = allPrivateChats.children.singleOrNull { userId == it.key }
                        if (chat == null) {
                            createChatWith(userId, { onFetched(it) })
                        } else {
                            onFetched(chat.value as String)
                        }
                    }
                })
    }

    fun createChatWith(userId: String, onCreated: (String) -> Unit, onFailed: (String) -> Unit = {}) {
        val myId = FirebaseAuth.getInstance().currentUser!!.uid

        val key = allPrivateChatsRef().push().key
        allPrivateChatsRef()
                .child(key)
                .setValue(mapOf("users" to listOf(userId, myId)))
                .addOnCompleteListener {
                    if (it.isSuccessful)
                        allPrivateChatsMappingRef()
                                .child(myId)
                                .child(userId)
                                .setValue(key)
                                .addOnCompleteListener {
                                    if (it.isSuccessful) {
                                        allPrivateChatsMappingRef()
                                                .child(userId)
                                                .child(myId)
                                                .setValue(key)
                                                .addOnCompleteListener {
                                                    if (it.isSuccessful) {
                                                        onCreated(key)
                                                    } else {
                                                        onFailed(it.exception!!.message!!)
                                                    }
                                                }
                                    } else {
                                        onFailed(it.exception!!.message!!)
                                    }
                                }
                    else
                        onFailed(it.exception!!.message!!)
                }
    }

    private fun allPrivateChatsRef() =
            FirebaseDatabase.getInstance().reference
                    .child("Chats")

    private fun allPrivateChatsMappingRef() =
            FirebaseDatabase.getInstance().reference
                    .child("PrivateChatsMapping")
}