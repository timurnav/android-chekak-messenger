package com.developer.timurnav.chekak.chekakmessenger.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.developer.timurnav.chekak.chekakmessenger.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var mListener: FirebaseAuth.AuthStateListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAuth = FirebaseAuth.getInstance()
        mListener = FirebaseAuth.AuthStateListener {
            it.currentUser?.let {
                startActivity(Intent(this, DashboardActivity::class.java))
                finish()
            }
        }

        createAccountButton.setOnClickListener {
            startActivity(Intent(this, CreateAccountActivity::class.java))
        }

        loginExistingAccountButton.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    override fun onStart() {
        super.onStart()
        mAuth.addAuthStateListener(mListener)
    }

    override fun onStop() {
        super.onStop()
        mAuth.removeAuthStateListener(mListener)
    }
}
