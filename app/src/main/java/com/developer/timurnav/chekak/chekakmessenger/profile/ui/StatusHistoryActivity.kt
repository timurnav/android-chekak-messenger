package com.developer.timurnav.chekak.chekakmessenger.profile.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import com.developer.timurnav.chekak.chekakmessenger.R
import com.developer.timurnav.chekak.chekakmessenger.profile.dao.StatusesHistoryDao
import com.developer.timurnav.chekak.chekakmessenger.users.dao.UserDao
import kotlinx.android.synthetic.main.activity_status_history.*
import java.util.concurrent.TimeUnit

class StatusHistoryActivity : AppCompatActivity() {

    private val userDao = UserDao()
    private val statusesHistoryDao = StatusesHistoryDao()

    private val statusesList = ArrayList<String>()
    private val statusesDisplayedList = ArrayList<String>()

    private val adapter =
            StatusHistoryItemAdapter(
                    statusesDisplayedList, this,
                    { ifStatusChanged(it, { setStatus(it) }) }
            )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_status_history)

        userDao.fetchUserData(onUserFetched = {
            textViewCurrentStatus.text = it.status
        })

        statusesHistoryDao.fetchAll(onFetched = {
            statusesList.addAll(it)
            statusesDisplayedList.addAll(it)
        })

        buttonSetStatus.setOnClickListener {
            val newStatus = editTextStatus.text.toString()
            ifStatusChanged(newStatus, {
                setStatus(newStatus)
                editTextStatus.text.clear()
            })
        }

        recyclerViewStatusHistory.adapter = adapter
        recyclerViewStatusHistory.layoutManager = LinearLayoutManager(this)
        val itemSeparator = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        recyclerViewStatusHistory.addItemDecoration(itemSeparator)

        filterList()
    }

    private fun filterList() {
        var previousValue = ""
        val thread = Thread({
            while (true) {
                val newStatus = editTextStatus.text.toString().trim()
                if (newStatus.isNotEmpty() || newStatus != previousValue) {
                    previousValue = newStatus
                    statusesDisplayedList.clear()
                    statusesDisplayedList.addAll(statusesList.filter { it.contains(newStatus) })
                    runOnUiThread({
                        adapter.notifyDataSetChanged()
                    })
                }
                TimeUnit.MILLISECONDS.sleep(300)
            }
        })
        thread.isDaemon = true
        thread.start()
    }

    private fun setStatus(newStatus: String) {
        textViewCurrentStatus.text = newStatus
        if (newStatus.isNotEmpty()) {
            userDao.fetchUserData(onUserFetched = {
                userDao.storeUser(it.copy(status = newStatus), onSuccess = {
                    setResult(Activity.RESULT_OK, Intent())
                    finish()
                })
            })
        }
        if (!statusesDisplayedList.contains(newStatus)) {
            statusesDisplayedList.add(newStatus)
            adapter.notifyDataSetChanged()
            statusesHistoryDao.addStatus(newStatus)
        }
    }

    private fun ifStatusChanged(newStatus: String, action: () -> Unit) {
        if (newStatus == textViewCurrentStatus.text) {
            Toast.makeText(this, "Status isn't changed", Toast.LENGTH_LONG).show()
        } else {
            action()
        }
    }
}
