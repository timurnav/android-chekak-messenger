package com.developer.timurnav.chekak.chekakmessenger.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.developer.timurnav.chekak.chekakmessenger.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mAuth = FirebaseAuth.getInstance()

        doLoginButton.setOnClickListener {
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
                        onUserCreatingFailed()
                }
    }

    private fun onUserCreatingFailed() {
        val message = "Incorrect email or password"
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun onUserAuthorized() {
        startActivity(Intent(this, DashboardActivity::class.java))
        finish()
    }

}
