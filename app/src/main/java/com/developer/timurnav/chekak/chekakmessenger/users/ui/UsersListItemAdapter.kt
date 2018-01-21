package com.developer.timurnav.chekak.chekakmessenger.users.ui

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.developer.timurnav.chekak.chekakmessenger.R
import com.developer.timurnav.chekak.chekakmessenger.chats.dao.ChatsDao
import com.developer.timurnav.chekak.chekakmessenger.messaging.ui.PrivateChatActivity
import com.developer.timurnav.chekak.chekakmessenger.users.model.User
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class UsersListItemAdapter(
        private val list: List<User>,
        private val context: Context
) : RecyclerView.Adapter<UsersListItemAdapter.UserListItemViewHolder>() {

    private val chatDao = ChatsDao()

    override fun onBindViewHolder(holder: UserListItemViewHolder, position: Int) {
        holder.bindView(list[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserListItemViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_users_list, parent, false)
        return UserListItemViewHolder(view)
    }

    override fun getItemCount() = list.size

    inner class UserListItemViewHolder(userView: View) : RecyclerView.ViewHolder(userView) {

        private val imageViewUserThumbnail = userView.findViewById<CircleImageView>(R.id.imageViewUsersListItemThumbnail)
        private val textViewUserName = userView.findViewById<TextView>(R.id.textViewUsersListItemName)
        private val textViewUserStatus = userView.findViewById<TextView>(R.id.textViewUsersListItemStatus)

        fun bindView(user: User) {
            textViewUserName.text = user.name
            textViewUserStatus.text = user.status

            if (user.thumbImage.isNotEmpty()) {
                Picasso.with(context)
                        .load(user.thumbImage)
                        .placeholder(R.drawable.ic_person_black_48dp)
                        .into(imageViewUserThumbnail)
            }

            itemView.setOnClickListener {
                chatDao.getChatIdWithUser(
                        userId = user.id,
                        onIdFetched = {
                            val intent = Intent(context, PrivateChatActivity::class.java)
                            intent.putExtra(PrivateChatActivity.CHAT_ID_KEY, it)
                            context.startActivity(intent)
                        }
                )
            }
        }
    }
}