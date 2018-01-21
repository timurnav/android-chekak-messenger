package com.developer.timurnav.chekak.chekakmessenger.messaging.ui

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.developer.timurnav.chekak.chekakmessenger.R
import com.developer.timurnav.chekak.chekakmessenger.chats.dao.ChatsRemoteRepository
import com.developer.timurnav.chekak.chekakmessenger.messaging.model.OwnedMessage
import com.developer.timurnav.chekak.chekakmessenger.users.dao.UserDao
import com.developer.timurnav.chekak.chekakmessenger.users.ui.UserInfoActivity
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_private_chat.*

class PrivateChatActivity : AppCompatActivity() {

    private val chatDao = ChatsRemoteRepository()
    private val userDao = UserDao()

    private val messages = ArrayList<OwnedMessage>()
    private val adapter = MessageAdapter(messages, this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_private_chat)

        recyclerViewPrivateMessages.adapter = adapter
        val layoutManager = LinearLayoutManager(this)
        recyclerViewPrivateMessages.layoutManager = layoutManager

        val chatId = intent.getStringExtra(CHAT_ID_KEY)

        initToolbar(chatId)

        chatDao.listenMessages(
                chatId = chatId,
                onMessagesSetChanged = { fetchedMessages ->
                    messages.clear()
                    messages.addAll(fetchedMessages)
                    adapter.notifyDataSetChanged()
                    layoutManager.scrollToPosition(messages.size - 1)
                })

        buttonPrivateChatSendMessage.setOnClickListener {
            val message = editTextUserPreparedMessage.text.toString()
            if (message.isNotEmpty()) {
                editTextUserPreparedMessage.text.clear()
                chatDao.sendMessageToChat(chatId, message)
            }
        }

        layoutUserInfo.setOnClickListener {
            startActivity(Intent(this, UserInfoActivity::class.java))
        }
    }

    private fun initToolbar(chatId: String) {
        chatDao.listenChatUsers(
                chatId = chatId,
                onUsersSetChanged = { usersIds ->
                    val userId = usersIds.single { it != FirebaseAuth.getInstance().currentUser!!.uid }
                    userDao.listenUserById(userId, onUserUpdated = {
                        textViewPrivateChatUserName.text = it.name
                        if (it.thumbImage.isNotEmpty()) {
                            Picasso.with(this)
                                    .load(it.thumbImage)
                                    .placeholder(R.drawable.ic_person_black_48dp)
                                    .into(imageViewPrivateChatThumbnail)
                        }
                    })
                })

        setSupportActionBar(toolbarPrivateChat)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    companion object {
        val CHAT_ID_KEY = "CHAT_ID"
    }

}
