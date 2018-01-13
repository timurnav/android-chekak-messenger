package com.developer.timurnav.chekak.chekakmessenger.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.developer.timurnav.chekak.chekakmessenger.R
import com.developer.timurnav.chekak.chekakmessenger.dao.PrivateChatDao
import com.developer.timurnav.chekak.chekakmessenger.dao.UserDao
import com.developer.timurnav.chekak.chekakmessenger.model.OwnedMessage
import kotlinx.android.synthetic.main.activity_private_chat.*

class PrivateChatActivity : AppCompatActivity() {

    private val chatDao = PrivateChatDao()
    private val userDao = UserDao()

    private val messages = ArrayList<OwnedMessage>()
    private val adapter = MessageAdapter(messages, this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_private_chat)
        setSupportActionBar(toolbar)

        recyclerViewPrivateMessages.adapter = adapter
        val layoutManager = LinearLayoutManager(this)
        recyclerViewPrivateMessages.layoutManager = layoutManager

        val userId = intent.getStringExtra("USER_ID")

        userDao.listenUserById(userId, onUserUpdated = {
            //            textViewUserName.text = it.name
//            Picasso.with(this)
//                    .load(it.thumbImage)
//                    .placeholder(R.drawable.ic_person_black_48dp)
//                    .into(imageViewUserThumbnail)

        })

        chatDao.fetchChatIdWithUser(
                userId = userId,
                onIdFetched = { chatId ->
                    chatDao.fetchChat(chatId = chatId, onMessagesSetChanged = { fetchedMessages ->
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
                }
        )
    }
}
