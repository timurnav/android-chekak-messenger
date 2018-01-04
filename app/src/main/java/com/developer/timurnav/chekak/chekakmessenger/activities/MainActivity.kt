package com.developer.timurnav.chekak.chekakmessenger.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.developer.timurnav.chekak.chekakmessenger.R
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        createAccountButton.setOnClickListener {
            startActivity(Intent(this, CreateAccountActivity::class.java))
        }

        loginExistingAccountButton.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

    }
}
