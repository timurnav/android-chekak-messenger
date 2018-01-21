package com.developer.timurnav.chekak.chekakmessenger.messaging.ui

import android.content.Context
import android.graphics.Color
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import com.developer.timurnav.chekak.chekakmessenger.R
import com.developer.timurnav.chekak.chekakmessenger.messaging.model.OwnedMessage
import java.util.*

class MessageAdapter(
        private val messages: List<OwnedMessage>,
        private val context: Context
) : RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.bindView(messages[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_message, parent, false)
        return MessageViewHolder(view)
    }

    override fun getItemCount() = messages.size

    inner class MessageViewHolder(messageView: View) : RecyclerView.ViewHolder(messageView) {

        private val messageContainer = messageView.findViewById<RelativeLayout>(R.id.layoutMessageContainer)
        private val messageCard = messageView.findViewById<CardView>(R.id.cardViewMessageContainer)
        private val messageTextView = messageView.findViewById<TextView>(R.id.textViewMessageText)
        private val messageTimestampView = messageView.findViewById<TextView>(R.id.textViewMessageTimestamp)

        fun bindView(message: OwnedMessage) {
            setLayoutParams(message.isMine)
            messageTextView.text = message.message.message
            messageTimestampView.text = DateFormat.format("HH:mm:ss", Date(message.message.timestamp))
        }

        private fun setLayoutParams(isMine: Boolean) {
            val layoutParams = messageContainer.layoutParams as RelativeLayout.LayoutParams
            if (isMine) {
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
                layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_LEFT)
                layoutParams.leftMargin = 120
                layoutParams.rightMargin = 0
                messageCard.setCardBackgroundColor(Color.YELLOW)
            } else {
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT)
                layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_RIGHT)
                layoutParams.leftMargin = 0
                layoutParams.rightMargin = 120
                messageCard.setCardBackgroundColor(Color.WHITE)
            }
            messageContainer.layoutParams = layoutParams
        }
    }
}