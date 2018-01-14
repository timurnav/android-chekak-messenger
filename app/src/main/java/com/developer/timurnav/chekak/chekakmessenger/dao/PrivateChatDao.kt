package com.developer.timurnav.chekak.chekakmessenger.dao

import com.developer.timurnav.chekak.chekakmessenger.model.Message
import com.developer.timurnav.chekak.chekakmessenger.model.OwnedMessage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PrivateChatDao {

    fun fetchChat(chatId: String,
                  onMessagesSetChanged: (List<OwnedMessage>) -> Unit) {
        val myId = FirebaseAuth.getInstance().currentUser!!.uid

        allPrivateChatsRef()
                .child(chatId)
                .child("messages")
                .addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(error: DatabaseError?) {
                    }

                    override fun onDataChange(usersSnapshot: DataSnapshot) {
                        val messages = usersSnapshot.children
                                .map(Message.Companion::mapper)
                                .map { OwnedMessage(isMine = myId == it.authorId, message = it) }
                        onMessagesSetChanged(messages)
                    }
                })
    }

    fun sendMessageToChat(chatId: String, message: String) {
        val messageKey = allPrivateChatsRef()
                .child(chatId)
                .child("messages")
                .push().key
        allPrivateChatsRef()
                .child(chatId)
                .child("messages")
                .child(messageKey)
                .setValue(Message(
                        id = messageKey,
                        authorId = FirebaseAuth.getInstance().currentUser!!.uid,
                        message = message,
                        timestamp = System.currentTimeMillis()
                ))
    }

    fun fetchChatIdWithUser(userId: String,
                            onIdFetched: (String) -> Unit,
                            onFailed: (String) -> Unit = {}) {
        allPrivateChatsMappingRef()
                .child(FirebaseAuth.getInstance().currentUser!!.uid)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(error: DatabaseError) {
                        onFailed(error.message)
                    }

                    override fun onDataChange(allPrivateChats: DataSnapshot) {
                        val chat = allPrivateChats.children.singleOrNull { userId == it.key }
                        if (chat == null) {
                            createChatWith(userId = userId, onCreated = onIdFetched, onFailed = onFailed)
                        } else {
                            onIdFetched(chat.value as String)
                        }
                    }
                })
    }

    private fun createChatWith(userId: String,
                               onCreated: (String) -> Unit = {},
                               onFailed: (String) -> Unit = {}
    ) {
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