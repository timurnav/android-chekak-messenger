package com.developer.timurnav.chekak.chekakmessenger.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.developer.timurnav.chekak.chekakmessenger.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_change_password.*

class ChangePasswordActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)

        buttonApplyPasswordChanging.setOnClickListener {
            val oldPassword = textEditEnterOldPassword.text.toString()
            val newPassword = textEditEnterNewPassword.text.toString()
            val confirmPassword = textEditConfirmNewPassword.text.toString()
            when {
                oldPassword.isEmpty() -> {
                    Toast.makeText(this@ChangePasswordActivity, "Old password must not be empty", Toast.LENGTH_LONG).show()
                }
                confirmPassword != newPassword -> {
                    Toast.makeText(this@ChangePasswordActivity, "New password must not be empty", Toast.LENGTH_LONG).show()
                }
                confirmPassword != newPassword -> {
                    Toast.makeText(this@ChangePasswordActivity, "Please confirm your new password", Toast.LENGTH_LONG).show()
                }
                else -> {
                    val currentUser = FirebaseAuth.getInstance().currentUser!!
                    FirebaseAuth.getInstance().signInWithEmailAndPassword(currentUser.email!!, oldPassword)
                            .addOnCompleteListener {
                                if (it.isSuccessful) {
                                    currentUser
                                            .updatePassword(newPassword)
                                            .addOnCompleteListener {
                                                if (it.isSuccessful) {
                                                    Toast.makeText(this@ChangePasswordActivity, "Password changed successfully", Toast.LENGTH_LONG).show()
                                                } else {
                                                    Toast.makeText(this@ChangePasswordActivity, it.exception!!.message, Toast.LENGTH_LONG).show()
                                                }
                                            }
                                } else {
                                    Toast.makeText(this@ChangePasswordActivity, "Can't authorize with your old password", Toast.LENGTH_LONG).show()
                                }
                            }
                }
            }
        }
    }
}
