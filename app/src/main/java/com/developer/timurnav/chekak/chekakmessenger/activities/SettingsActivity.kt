package com.developer.timurnav.chekak.chekakmessenger.activities

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.developer.timurnav.chekak.chekakmessenger.R
import com.developer.timurnav.chekak.chekakmessenger.dao.UserDao
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.activity_settings.*


class SettingsActivity : AppCompatActivity() {

    private val userDao = UserDao()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        actualizeUserData()

        buttonChangePassword.setOnClickListener {
            startActivityForResult(Intent(this, ChangePasswordActivity::class.java), CHANGE_PASSWORD_ACTIVITY_CODE)
        }

        buttonEditProfile.setOnClickListener {
            startActivityForResult(Intent(this, EditProfileActivity::class.java), EDIT_PROFILE_ACTIVITY_CODE)
        }

        layoutSettingsStatus.setOnClickListener {
            startActivityForResult(Intent(this, StatusHistoryActivity::class.java), CHANGE_STATUS_ACTIVITY_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                0 -> startCropActivity(data!!.data)
                CHANGE_PASSWORD_ACTIVITY_CODE -> {
                    Toast.makeText(this, "Password changed successfully", Toast.LENGTH_LONG).show()
                }
                EDIT_PROFILE_ACTIVITY_CODE -> {
                    Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_LONG).show()
                    actualizeUserData()
                }
                CHANGE_STATUS_ACTIVITY_CODE -> {
                    Toast.makeText(this, "Status changed successfully", Toast.LENGTH_LONG).show()
                    actualizeUserData()
                }
                CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> storeCropImage(data!!)
            }
        }
    }

    private fun actualizeUserData() {
        userDao.fetchUserData(
                onUserFetched = {
                    textViewSettingsUserName.text = it.name
                    textViewSettingsStatus.text = it.status
                    textViewSettingsEmail.text = it.email
                }
        )
    }

    private fun storeCropImage(data: Intent) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun startCropActivity(image: Uri) {
        CropImage.activity(image)
                .setAspectRatio(1, 1)
                .start(this)
    }

    private companion object {
        private val CHANGE_PASSWORD_ACTIVITY_CODE = 1
        private val EDIT_PROFILE_ACTIVITY_CODE = 2
        private val CHANGE_STATUS_ACTIVITY_CODE = 3
    }
}
