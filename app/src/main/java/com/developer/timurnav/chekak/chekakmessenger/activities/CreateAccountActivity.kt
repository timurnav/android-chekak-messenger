package com.developer.timurnav.chekak.chekakmessenger.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.developer.timurnav.chekak.chekakmessenger.R
import com.developer.timurnav.chekak.chekakmessenger.dao.UserDao
import com.developer.timurnav.chekak.chekakmessenger.model.User
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_create_account.*

class CreateAccountActivity : AppCompatActivity() {

    private val userDao = UserDao()
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)

        mAuth = FirebaseAuth.getInstance()

        createButton.setOnClickListener {
            val name = createNameField.text.toString().trim()
            val email = createEmailFiled.text.toString().trim()
            val password = createPasswordField.text.toString().trim()
            when {
                name.isEmpty() ->
                    Toast.makeText(this, "Enter your name", Toast.LENGTH_LONG).show()
                email.isEmpty() ->
                    Toast.makeText(this, "Enter your email", Toast.LENGTH_LONG).show()
                password.isEmpty() ->
                    Toast.makeText(this, "Enter your email", Toast.LENGTH_LONG).show()
                else -> createAccount(email, password, name)
            }
        }
    }

    private fun createAccount(email: String, password: String, name: String) {
        mAuth
                .createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        val user = User(
                                name = name,
                                status = "Chekak brodyaga",
                                image = "default",
                                thumbImage = "default"
                        )
                        userDao.storeUser(
                                user = user,
                                onSuccess = { onUserDataStored() },
                                onFailed = { onUserCreatingFailed() }
                        )
                    } else {

                    }
                }
    }

    private fun onUserCreatingFailed() {
        val message = "Something went wrong, try another name"
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun onUserDataStored() {
        startActivity(Intent(this, DashboardActivity::class.java))
        finish()
    }
}
