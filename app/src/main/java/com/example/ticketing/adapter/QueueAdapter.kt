package com.example.ticketing.adapter

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.ticketing.R
import com.example.ticketing.data.TicketingDatabaseHelper
import com.example.ticketing.databinding.ItemQueueBinding
import com.example.ticketing.model.Queue

class QueueAdapter(private val listener: QueueActionListener) : 
    ListAdapter<Queue, QueueAdapter.QueueViewHolder>(QueueDiffCallback()) {

    interface QueueActionListener {
        fun onCallClicked(queue: Queue)
        fun onDoneClicked(queue: Queue)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QueueViewHolder {
        val binding = ItemQueueBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return QueueViewHolder(binding)
    }

    override fun onBindViewHolder(holder: QueueViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class QueueViewHolder(private val binding: ItemQueueBinding) : 
        RecyclerView.ViewHolder(binding.root) {

        fun bind(queue: Queue) {
            // Format queue info text
            val queueInfo = "${queue.queueNumber} - ${queue.name}"
            binding.tvQueueInfo.text = queueInfo
            
            // Set status text
            val statusText = when (queue.status) {
                TicketingDatabaseHelper.STATUS_WAITING -> binding.root.context.getString(R.string.waiting)
                TicketingDatabaseHelper.STATUS_CALLED -> binding.root.context.getString(R.string.called)
                else -> queue.status
            }
            binding.tvStatus.text = statusText
            
            // Set status background color
            val statusColor = when (queue.status) {
                TicketingDatabaseHelper.STATUS_WAITING -> R.color.status_waiting
                TicketingDatabaseHelper.STATUS_CALLED -> R.color.status_called
                TicketingDatabaseHelper.STATUS_DONE -> R.color.status_done
                else -> R.color.primary_blue
            }
            binding.tvStatus.backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(binding.root.context, statusColor)
            )
            
            // Configure buttons based on status
            if (queue.status == TicketingDatabaseHelper.STATUS_CALLED) {
                binding.btnCall.isEnabled = false
                binding.btnDone.isEnabled = true
            } else {
                binding.btnCall.isEnabled = true
                binding.btnDone.isEnabled = false
            }
            
            // Set click listeners
            binding.btnCall.setOnClickListener {
                listener.onCallClicked(queue)
            }
            
            binding.btnDone.setOnClickListener {
                listener.onDoneClicked(queue)
            }
        }
    }

    class QueueDiffCallback : DiffUtil.ItemCallback<Queue>() {
        override fun areItemsTheSame(oldItem: Queue, newItem: Queue): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Queue, newItem: Queue): Boolean {
            return oldItem == newItem
        }
    }
}