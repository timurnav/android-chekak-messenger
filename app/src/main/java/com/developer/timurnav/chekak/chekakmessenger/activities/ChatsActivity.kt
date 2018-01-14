package com.developer.timurnav.chekak.chekakmessenger.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import com.developer.timurnav.chekak.chekakmessenger.R
import com.developer.timurnav.chekak.chekakmessenger.dao.ChatsDao
import com.developer.timurnav.chekak.chekakmessenger.dao.UserDao
import com.developer.timurnav.chekak.chekakmessenger.model.ChatInfo
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_chats.*

class ChatsActivity : AppCompatActivity() {

    private val chatsDao = ChatsDao()
    private val userDao = UserDao()

    private val chatsList = ArrayList<ChatInfo>()
    private val adapter = ChatListItemAdapter(chatsList, this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chats)
        setSupportActionBar(toolbarChats)

        chatsDao.fetchMyChatsMappings(onPrivateChatMappingFetched = { userIdOnChatMapping ->
            val userIds = userIdOnChatMapping.keys
            userDao.fetchAllUsers(onFetched = { allUsers ->
                chatsList.clear()
                allUsers.filter { userIds.contains(it.id) }
                        .mapTo(chatsList, { user ->
                            ChatInfo(user.thumbImage, user.name, userIdOnChatMapping[user.id]!!)
                        })
                adapter.notifyDataSetChanged()
            })
        })

        recyclerViewChatsList.layoutManager = LinearLayoutManager(this)
        recyclerViewChatsList.adapter = adapter

        chatsListFab.setOnClickListener {
            startActivity(Intent(this, NewChatActivity::class.java))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        super.onOptionsItemSelected(item)
        item?.let {
            when (it.itemId) {
                R.id.logoutMenuItem -> {
                    FirebaseAuth.getInstance().signOut()
                    startActivity(Intent(this, LandingPageActivity::class.java))
                    finish()
                }
                R.id.settingsMenuItem -> {
                    startActivity(Intent(this, SettingsActivity::class.java))
                }
            }
        }
        return true
    }
}
