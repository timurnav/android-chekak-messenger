package com.developer.timurnav.chekak.chekakmessenger.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.developer.timurnav.chekak.chekakmessenger.R
import com.developer.timurnav.chekak.chekakmessenger.dao.UserDao
import kotlinx.android.synthetic.main.activity_dashboard.*

class DashboardActivity : AppCompatActivity() {

    private val userDao = UserDao()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        userDao.fetchUserData(
                onUserFetched = { viewSomething.text = it.status },
                onFailed = {
                    Toast.makeText(this@DashboardActivity, it, Toast.LENGTH_LONG).show()
                }
        )
    }
}
