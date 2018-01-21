package com.developer.timurnav.chekak.chekakmessenger.profile.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.developer.timurnav.chekak.chekakmessenger.R
import com.developer.timurnav.chekak.chekakmessenger.chats.ui.ChatsActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mAuth = FirebaseAuth.getInstance()

        doLoginButton.setOnClickListener {
            val imm = this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            val view = this.currentFocus ?: View(this)
            imm.hideSoftInputFromWindow(view.windowToken, 0)

            val email = loginEmailField.text.toString().trim()
            val password = loginPasswordField.text.toString().trim()
            when {
                email.isEmpty() ->
                    Toast.makeText(this, "Enter your email", Toast.LENGTH_LONG).show()
                password.isEmpty() ->
                    Toast.makeText(this, "Enter your email", Toast.LENGTH_LONG).show()
                else -> loginAccount(email, password)
            }
        }

    }

    private fun loginAccount(email: String, password: String) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (it.isSuccessful)
                        onUserAuthorized()
                    else
                        onUserCreatingFailed(it.exception!!.message)
                }
    }

    private fun onUserCreatingFailed(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun onUserAuthorized() {
        if (mAuth.currentUser!!.isEmailVerified) {
            startActivity(Intent(this, ChatsActivity::class.java))
            finish()
        } else {
            mAuth.signOut()
            Snackbar
                    .make(
                            findViewById(android.R.id.content),
                            "You must accept your email before login",
                            Snackbar.LENGTH_INDEFINITE
                    )
                    .show()
        }
    }

}
