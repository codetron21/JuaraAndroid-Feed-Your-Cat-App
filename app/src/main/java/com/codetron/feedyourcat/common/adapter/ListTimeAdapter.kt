package com.codetron.feedyourcat.common.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.codetron.feedyourcat.databinding.ItemFeedTimeBinding
import com.codetron.feedyourcat.model.Time

typealias TimeClickListener = (Long) -> Unit

class ListTimeAdapter(
    private val listener: TimeClickListener? = null
) : ListAdapter<Time, ListTimeAdapter.TimeViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemFeedTimeBinding.inflate(inflater, parent, false)
        return TimeViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: TimeViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class TimeViewHolder(
        private val binding: ItemFeedTimeBinding,
        private val listener: TimeClickListener? = null
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(data: Time) {
            binding.card.setOnClickListener {
                listener?.invoke(data.id)
            }
        }

    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Time>() {
            override fun areItemsTheSame(oldItem: Time, newItem: Time): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Time, newItem: Time): Boolean {
                return oldItem == newItem
            }
        }
    }

}