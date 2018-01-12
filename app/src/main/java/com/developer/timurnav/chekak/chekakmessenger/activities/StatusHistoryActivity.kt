package com.developer.timurnav.chekak.chekakmessenger.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import com.developer.timurnav.chekak.chekakmessenger.R
import com.developer.timurnav.chekak.chekakmessenger.dao.StatusesHistoryDao
import com.developer.timurnav.chekak.chekakmessenger.dao.UserDao
import kotlinx.android.synthetic.main.activity_status_history.*

class StatusHistoryActivity : AppCompatActivity() {

    private val userDao = UserDao()
    private val statusesHistoryDao = StatusesHistoryDao()

    private val statusList = ArrayList<String>()
    private val adapter =
            StatusHistoryItemAdapter(
                    statusList, this,
                    { ifStatusChanged(it, { setStatus(it) }) }
            )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_status_history)

        userDao.fetchUserData(onUserFetched = {
            textViewCurrentStatus.text = it.status
        })

        statusesHistoryDao.fetchAll(onFetched = {
            statusList.addAll(it)
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


    }

    private fun setStatus(newStatus: String) {
        textViewCurrentStatus.text = newStatus
        userDao.fetchUserData(onUserFetched = {
            userDao.storeUser(it.copy(status = newStatus), onSuccess = {
                setResult(Activity.RESULT_OK, Intent())
                finish()
            })
        })
        if (!statusList.contains(newStatus)) {
            statusList.add(newStatus)
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
