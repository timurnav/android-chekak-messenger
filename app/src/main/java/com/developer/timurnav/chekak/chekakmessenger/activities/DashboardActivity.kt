package com.developer.timurnav.chekak.chekakmessenger.activities

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.developer.timurnav.chekak.chekakmessenger.R
import com.developer.timurnav.chekak.chekakmessenger.dao.UserDao
import com.developer.timurnav.chekak.chekakmessenger.fragments.SectionPagerAdapter
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_dashboard.*

class DashboardActivity : AppCompatActivity() {

    private val userDao = UserDao()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        supportActionBar!!.title = "Dashboard"

        val adapter = SectionPagerAdapter(supportFragmentManager)
        dashboardViewPager.adapter = adapter
        dashboardTabs.setupWithViewPager(dashboardViewPager)
        dashboardTabs.setTabTextColors(Color.WHITE, Color.GREEN)

        userDao.fetchUserData(
                onUserFetched = {
                    Toast.makeText(this@DashboardActivity, it.toString(), Toast.LENGTH_LONG).show()
                },
                onFailed = {
                    Toast.makeText(this@DashboardActivity, it, Toast.LENGTH_LONG).show()
                }
        )
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
                    startActivity(Intent(this@DashboardActivity, MainActivity::class.java))
                    finish()
                }
                R.id.settingsMenuItem -> {
                    startActivity(Intent(this@DashboardActivity, SettingsActivity::class.java))
                }
            }
        }
        return true
    }
}
