package com.developer.timurnav.chekak.chekakmessenger.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.developer.timurnav.chekak.chekakmessenger.R
import kotlinx.android.synthetic.main.activity_private_chat.*

class PrivateChatActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_private_chat)
        setSupportActionBar(toolbar)

        Toast.makeText(this, intent.getStringExtra("CHAT_ID"), Toast.LENGTH_LONG).show()

    }
}
