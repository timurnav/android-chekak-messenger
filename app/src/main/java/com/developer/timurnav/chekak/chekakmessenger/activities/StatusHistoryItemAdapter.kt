package com.developer.timurnav.chekak.chekakmessenger.activities

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.developer.timurnav.chekak.chekakmessenger.R

class StatusHistoryItemAdapter(
        private val list: List<String>,
        private val context: Context,
        private val onStatusSelected: (String) -> Unit
) : RecyclerView.Adapter<StatusHistoryItemAdapter.StatusViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): StatusViewHolder {
        val view = LayoutInflater.from(context)
                .inflate(R.layout.item_status_history, parent, false)
        return StatusViewHolder(view)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: StatusViewHolder, position: Int) {
        holder.bindView(list[position])
    }

    inner class StatusViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val statusView = view.findViewById<TextView>(R.id.textViewStoredStatus)

        fun bindView(status: String) {
            statusView.text = status
            statusView.setOnClickListener {
                onStatusSelected(status)
            }
        }

    }
}