package com.developer.timurnav.chekak.chekakmessenger.activities

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.developer.timurnav.chekak.chekakmessenger.R
import com.developer.timurnav.chekak.chekakmessenger.dao.UserDao
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.activity_settings.*


class SettingsActivity : AppCompatActivity() {

    private val userDao = UserDao()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        userDao.fetchUserData(
                onUserFetched = {
                    textViewSettingsUserName.text = it.name
                    textViewSettingsStatus.text = it.status
                    textViewSettingsEmail.text = it.email
                }
        )

        buttonChangePassword.setOnClickListener {
            startActivity(Intent(this@SettingsActivity, ChangePasswordActivity::class.java))
        }

        buttonEditProfile.setOnClickListener {
            startActivity(Intent(this@SettingsActivity, EditProfileActivity::class.java))
        }

        layoutSettingsStatus.setOnClickListener {
            startActivity(Intent(this@SettingsActivity, StatusHistoryActivity::class.java))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {

            0 -> if (resultCode == Activity.RESULT_OK) startCropActivity(data!!.data)

            CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> storeCropImage(data!!)
        }

    }

    private fun storeCropImage(data: Intent) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun startCropActivity(image: Uri) {
        CropImage.activity(image)
                .setAspectRatio(1, 1)
                .start(this)
    }
}
