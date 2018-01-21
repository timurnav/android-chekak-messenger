package com.developer.timurnav.chekak.chekakmessenger.chats.ui

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.developer.timurnav.chekak.chekakmessenger.R
import com.developer.timurnav.chekak.chekakmessenger.chats.model.ChatInfo
import com.developer.timurnav.chekak.chekakmessenger.messaging.ui.PrivateChatActivity
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class ChatListItemAdapter(
        private val chatsList: List<ChatInfo>,
        private val context: Context
) : RecyclerView.Adapter<ChatListItemAdapter.ChatListItemViewHolder>() {

    override fun onBindViewHolder(holder: ChatListItemViewHolder, position: Int) {
        holder.bindView(chatsList[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ChatListItemViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_chats_list, parent, false)
        return ChatListItemViewHolder(view)
    }

    override fun getItemCount() = chatsList.size

    inner class ChatListItemViewHolder(chatView: View) : RecyclerView.ViewHolder(chatView) {

        private val imageViewUserThumbnail = chatView.findViewById<CircleImageView>(R.id.imageViewChatsListItemThumbnail)!!
        private val textViewUserName = chatView.findViewById<TextView>(R.id.textViewChatsListItemName)!!

        fun bindView(chatInfo: ChatInfo) {
            textViewUserName.text = chatInfo.name

            if (chatInfo.thumbnail.isNotEmpty()) {
                Picasso.with(context)
                        .load(chatInfo.thumbnail)
                        .placeholder(R.drawable.ic_person_black_48dp)
                        .into(imageViewUserThumbnail)
            }

            itemView.setOnClickListener {
                val intent = Intent(context, PrivateChatActivity::class.java)
                intent.putExtra(PrivateChatActivity.CHAT_ID_KEY, chatInfo.chatId)
                context.startActivity(intent)
            }
        }

    }
}