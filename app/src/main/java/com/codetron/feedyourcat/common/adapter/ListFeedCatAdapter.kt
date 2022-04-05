package com.codetron.feedyourcat.common.adapter

import android.content.Context
import android.content.res.ColorStateList
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.codetron.feedyourcat.R
import com.codetron.feedyourcat.databinding.ItemFeedCatBinding
import com.codetron.feedyourcat.model.FeedCat
import com.codetron.feedyourcat.utils.toHourMinute

class ListFeedCatAdapter :
    ListAdapter<FeedCat, ListFeedCatAdapter.FeedCatViewHolder>(DIFF_CALLBACK) {

    var trackter: SelectionTracker<Long>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedCatViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemFeedCatBinding.inflate(inflater, parent, false)
        return FeedCatViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FeedCatViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class FeedCatViewHolder(
        private val binding: ItemFeedCatBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(data: FeedCat) {
            binding.textName.text = data.name
            binding.imagePhoto.load(data.photo) {
                crossfade(true)
                placeholder(R.color.green_secondary)
            }
            binding.container.removeAllViews()
            data.times.forEach {
                val textView = TextView(binding.root.context)
                textView.text = it.toHourMinute()
                textView.gravity = Gravity.CENTER_VERTICAL
                textView.compoundDrawablePadding = 10
                textView.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_time, 0, 0, 0
                )

                binding.container.addView(textView)
            }

            trackter?.let {
                setItemSelected(binding.root.context, it.isSelected(data.feedId))
            }
        }

        fun getItem(): ItemDetailsLookup.ItemDetails<Long> =
            object : ItemDetailsLookup.ItemDetails<Long>() {
                override fun getPosition(): Int = adapterPosition
                override fun getSelectionKey(): Long = currentList[adapterPosition].feedId
            }

        private fun setItemSelected(context: Context, isSelected: Boolean) {
            if (isSelected.not()) {
                binding.card.strokeWidth = 0
                return
            }

            binding.card.strokeWidth = 4
            binding.card.setStrokeColor(
                ColorStateList.valueOf(
                    ContextCompat.getColor(
                        context,
                        R.color.orange_primary
                    )
                )
            )
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<FeedCat>() {
            override fun areItemsTheSame(oldItem: FeedCat, newItem: FeedCat): Boolean {
                return oldItem.feedId == newItem.feedId && oldItem.catId == newItem.catId
            }

            override fun areContentsTheSame(oldItem: FeedCat, newItem: FeedCat): Boolean {
                return oldItem == newItem
            }
        }
    }

}