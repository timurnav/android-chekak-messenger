package com.developer.timurnav.chekak.chekakmessenger.profile.ui

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import com.developer.timurnav.chekak.chekakmessenger.R
import com.developer.timurnav.chekak.chekakmessenger.chats.ui.ChatsActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_landing_page.*


class LandingPageActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var mListener: FirebaseAuth.AuthStateListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing_page)

        mAuth = FirebaseAuth.getInstance()
        mListener = FirebaseAuth.AuthStateListener {
            it.currentUser?.let {
                if (it.isEmailVerified) {
                    startActivity(Intent(this, ChatsActivity::class.java))
                    finish()
                } else {
                    mAuth.signOut()
                    Snackbar
                            .make(findViewById(android.R.id.content),
                                    "Please accept your email and then you will be able to login",
                                    Snackbar.LENGTH_INDEFINITE)
                            .setAction("To Login", {
                                startActivity(Intent(this, LoginActivity::class.java))
                                finish()
                            })
                            .show()
                }
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
