package com.developer.timurnav.chekak.chekakmessenger.activities

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.developer.timurnav.chekak.chekakmessenger.R
import com.developer.timurnav.chekak.chekakmessenger.dao.UserDao
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.CropImage
import id.zelory.compressor.Compressor
import kotlinx.android.synthetic.main.activity_settings.*
import java.io.ByteArrayOutputStream
import java.io.File


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

        imageViewSettingsUserProfile.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "SELECT_IMAGE"), IMAGE_GALERY_ACTIVITY_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                IMAGE_GALERY_ACTIVITY_CODE -> startCropActivity(data!!.data)
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
                    if (it.image.isNotEmpty()) {
                        Picasso.with(this)
                                .load(it.image)
                                .placeholder(R.drawable.ic_person_black_48dp)
                                .into(imageViewSettingsUserProfile)
                    }
                }
        )
    }

    private fun storeCropImage(data: Intent) {
        val profileImagePath = CropImage.getActivityResult(data).uri
        userDao.fetchUserData(onUserFetched = { user ->
            val profileImageFile = File(profileImagePath.path)
            val profileImageBitmap = Compressor(this)
                    .setMaxHeight(200)
                    .setMaxWidth(200)
                    .setQuality(65)
                    .compressToBitmap(profileImageFile)
            val thumbnailBAOS = ByteArrayOutputStream()
            profileImageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, thumbnailBAOS)
            val thumbnailByteArray = thumbnailBAOS.toByteArray()

            val storageReference = FirebaseStorage.getInstance().reference
            storageReference
                    .child("ProfileImages")
                    .child(user.id + ".jpg")
                    .putFile(profileImagePath)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            val profileImageDownloadUrl = it.result.downloadUrl.toString()
                            storageReference
                                    .child("UserThumbs")
                                    .child(user.id + ".jpg")
                                    .putBytes(thumbnailByteArray)
                                    .addOnCompleteListener {
                                        if (it.isSuccessful) {
                                            val thumbnailDownloadUrl = it.result.downloadUrl.toString()

                                            val updatedUser = user.copy(
                                                    image = profileImageDownloadUrl,
                                                    thumbImage = thumbnailDownloadUrl
                                            )
                                            userDao.storeUser(
                                                    updatedUser,
                                                    onSuccess = ::actualizeUserData
                                            )
                                        } else {
                                            //thumbnail image wasn't loaded
                                        }
                                    }
                        } else {
                            //profile image wasn't loaded
                        }
                    }
        })

    }

    private fun startCropActivity(image: Uri) {
        CropImage.activity(image)
                .setAspectRatio(1, 1)
                .start(this)
    }

    private companion object {
        private val IMAGE_GALERY_ACTIVITY_CODE = 0
        private val CHANGE_PASSWORD_ACTIVITY_CODE = 1
        private val EDIT_PROFILE_ACTIVITY_CODE = 2
        private val CHANGE_STATUS_ACTIVITY_CODE = 3
    }
}
