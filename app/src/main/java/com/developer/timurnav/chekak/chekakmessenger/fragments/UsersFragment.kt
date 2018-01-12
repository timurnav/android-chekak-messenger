package com.developer.timurnav.chekak.chekakmessenger.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.developer.timurnav.chekak.chekakmessenger.R
import com.developer.timurnav.chekak.chekakmessenger.dao.UserDao
import com.developer.timurnav.chekak.chekakmessenger.model.User
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_users.*
import java.util.concurrent.TimeUnit

class UsersFragment : Fragment() {

    private val userDao = UserDao()

    private val usersList = ArrayList<User>()
    private val usersDisplayedList = ArrayList<User>()

    private lateinit var adapter: UsersListItemAdapter

    private var currentNameFilter = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_users, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = UsersListItemAdapter(usersDisplayedList, context!!)

        usersRecyclerView.setHasFixedSize(true)
        usersRecyclerView.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        usersRecyclerView.adapter = adapter

        userDao.fetchAllUsers(onFetched = { fetchedList ->
            val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid
            usersList.clear()
            fetchedList
                    .filterTo(usersList, { currentUserId != it.id })
            filterUsersList()
        })

        startFiltering()
    }

    private fun startFiltering() {
        val thread = Thread({
            while (true) {
                val nameFilter = editTextNamesListFilter.text.toString().trim()
                if (nameFilter.isNotEmpty() || nameFilter != currentNameFilter) {
                    currentNameFilter = nameFilter
                    activity!!.runOnUiThread({
                        filterUsersList()
                    })
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
