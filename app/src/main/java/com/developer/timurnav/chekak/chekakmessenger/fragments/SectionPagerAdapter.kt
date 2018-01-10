package com.developer.timurnav.chekak.chekakmessenger.fragments

import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

class SectionPagerAdapter(manager: FragmentManager) : FragmentPagerAdapter(manager) {

    override fun getItem(position: Int) =
            when (position) {
                0 -> UsersFragment()
                1 -> ChatsFragment()
                else -> null!!
            }

    override fun getCount() = 2

    override fun getPageTitle(position: Int): String =
            when (position) {
                0 -> "USERS"
                1 -> "CHATS"
                else -> null!!
            }
}