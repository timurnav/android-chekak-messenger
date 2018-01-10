package com.developer.timurnav.chekak.chekakmessenger.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.inputmethod.InputMethodManager
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
            val imm = this@CreateAccountActivity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            val view = this@CreateAccountActivity.currentFocus ?: View(this@CreateAccountActivity)
            imm.hideSoftInputFromWindow(view.windowToken, 0)
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
                        mAuth.currentUser!!.sendEmailVerification()
                        val user = User(
                                name = name,
                                status = "Chekak brodyaga",
                                image = "default",
                                thumbImage = "default",
                                email = email
                        )
                        userDao.storeUser(
                                user = user,
                                onSuccess = { onUserDataStored() },
                                onFailed = { onUserCreatingFailed() }
                        )
                    } else {
                        Toast.makeText(this, it.exception!!.localizedMessage, Toast.LENGTH_LONG).show()
                    }
                }
    }

    private fun onUserCreatingFailed() {
        val message = "Something went wrong, try another name"
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun onUserDataStored() {
        mAuth.signOut()
        Snackbar.make(findViewById(android.R.id.content), "Please accept your email and then you will be able to login", Snackbar.LENGTH_INDEFINITE)
                .setAction("To Login", {
                    startActivity(Intent(this@CreateAccountActivity, LoginActivity::class.java))
                    finish()
                }).show()
    }
}
