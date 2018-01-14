package com.developer.timurnav.chekak.chekakmessenger.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.developer.timurnav.chekak.chekakmessenger.R
import com.developer.timurnav.chekak.chekakmessenger.dao.UserDao
import com.developer.timurnav.chekak.chekakmessenger.model.User
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_new_chat.*
import java.util.concurrent.TimeUnit

class NewChatActivity : AppCompatActivity() {

    private val userDao = UserDao()

    private val usersList = ArrayList<User>()
    private val usersDisplayedList = ArrayList<User>()
    private val adapter = UsersListItemAdapter(usersDisplayedList, this)

    private var currentNameFilter = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_chat)

        usersRecyclerView.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        usersRecyclerView.adapter = adapter

        initToolbar()

        userDao.fetchAllUsers(onFetched = { fetchedList ->
            val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid
            usersList.clear()
            fetchedList
                    .filter { currentUserId != it.id }
                    .sortedBy { it.name }
                    .toCollection(usersList)
            filterUsersList()
        })

        startFiltering()
    }

    private fun initToolbar() {
        setSupportActionBar(toolbarNewChat)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        buttonNewChatSearch.setOnClickListener {
            layoutNewChatBasic.visibility = View.GONE
            layoutNewChatSearch.visibility = View.VISIBLE
        }

        buttonNewChatCancelSearch.setOnClickListener {
            if (editTextNewChatNamesListFilter.text.isEmpty()) {
                layoutNewChatBasic.visibility = View.VISIBLE
                layoutNewChatSearch.visibility = View.GONE
            } else {
                editTextNewChatNamesListFilter.text.clear()
            }
        }
    }

    private fun startFiltering() {
        val thread = Thread({
            while (true) {
                editTextNewChatNamesListFilter?.let {
                    val nameFilter = editTextNewChatNamesListFilter.text.toString().trim()
                    if (nameFilter.isNotEmpty() || nameFilter != currentNameFilter) {
                        currentNameFilter = nameFilter
                        runOnUiThread({
                            filterUsersList()
                        })
                    }
                }
                TimeUnit.MILLISECONDS.sleep(300)
            }
        })
        thread.isDaemon = true
        thread.start()
    }

    private fun filterUsersList() {
        usersDisplayedList.clear()
        usersDisplayedList.addAll(usersList.filter { it.name.contains(currentNameFilter) })
        adapter.notifyDataSetChanged()
    }
}
