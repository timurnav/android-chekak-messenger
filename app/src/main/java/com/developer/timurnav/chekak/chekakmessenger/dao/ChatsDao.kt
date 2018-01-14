package com.developer.timurnav.chekak.chekakmessenger.dao

import com.developer.timurnav.chekak.chekakmessenger.model.Message
import com.developer.timurnav.chekak.chekakmessenger.model.OwnedMessage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ChatsDao {

    fun fetchMyChatsMappings(onPrivateChatMappingFetched: (Map<String, String>) -> Unit, onFailed: (String) -> Unit = {}) {
        allChatsMapping()
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(error: DatabaseError) {
                        onFailed(error.message)
                    }

                    override fun onDataChange(mappingSnapshot: DataSnapshot) {
                        onPrivateChatMappingFetched(
                                mappingSnapshot
                                        .child(FirebaseAuth.getInstance().currentUser!!.uid)
                                        .child("private").children
                                        .map { it.key to (it.value as String) }
                                        .toMap()
                        )
                    }
                })
    }

    fun listenMessages(chatId: String,
                       onMessagesSetChanged: (List<OwnedMessage>) -> Unit) {
        val myId = FirebaseAuth.getInstance().currentUser!!.uid

        allChatsRef()
                .child(chatId)
                .child("messages")
                .addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(error: DatabaseError?) {
                    }

                    override fun onDataChange(snapshot: DataSnapshot) {
                        val messages = snapshot.children
                                .map(Message.Companion::mapper)
                                .map { OwnedMessage(isMine = myId == it.authorId, message = it) }
                        onMessagesSetChanged(messages)
                    }
                })
    }

    fun listenChatUsers(chatId: String, onUsersSetChanged: (List<String>) -> Unit) {
        allChatsRef()
                .child(chatId)
                .child("users")
                .addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(error: DatabaseError) {
                    }

                    override fun onDataChange(snapshot: DataSnapshot) {
                        val usersId = snapshot.children.map { it.value as String }
                        onUsersSetChanged(usersId)
                    }
                })
    }

    fun sendMessageToChat(chatId: String, message: String) {
        val messageKey = allChatsRef()
                .child(chatId)
                .child("messages")
                .push().key
        allChatsRef()
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

    fun fetchPrivateChatIdWithUser(userId: String,
                                   onIdFetched: (String) -> Unit,
                                   onFailed: (String) -> Unit = {}) {
        allChatsMapping()
                .child(FirebaseAuth.getInstance().currentUser!!.uid)
                .child("private")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(error: DatabaseError) {
                        onFailed(error.message)
                    }

                    override fun onDataChange(allPrivateChats: DataSnapshot) {
                        val chat = allPrivateChats.children.singleOrNull { userId == it.key }
                        if (chat == null) {
                            createPrivateChatWith(userId = userId, onCreated = onIdFetched, onFailed = onFailed)
                        } else {
                            onIdFetched(chat.value as String)
                        }
                    }
                })
    }

    private fun createPrivateChatWith(userId: String,
                                      onCreated: (String) -> Unit = {},
                                      onFailed: (String) -> Unit = {}) {
        val myId = FirebaseAuth.getInstance().currentUser!!.uid

        val key = allChatsRef().push().key
        allChatsRef()
                .child(key)
                .setValue(mapOf("users" to listOf(userId, myId), "private" to true))
                .addOnCompleteListener {
                    if (it.isSuccessful)
                        allChatsMapping()
                                .child(myId)
                                .child("private")
                                .child(userId)
                                .setValue(key)
                                .addOnCompleteListener {
                                    if (it.isSuccessful) {
                                        allChatsMapping()
                                                .child(userId)
                                                .child("private")
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

    private fun allChatsRef() =
            FirebaseDatabase.getInstance().reference
                    .child("chats")

    private fun allChatsMapping() =
            FirebaseDatabase.getInstance().reference
                    .child("chats_mapping")
}