package com.codetron.feedyourcat.common.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.codetron.feedyourcat.R
import com.codetron.feedyourcat.databinding.ItemCatBinding
import com.codetron.feedyourcat.model.Cat
import com.codetron.feedyourcat.utils.formatString

typealias CatClickListener = (Long) -> Unit

class ListCatAdapter(
    private val listener: CatClickListener
) : ListAdapter<Cat, ListCatAdapter.CatViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCatBinding.inflate(inflater, parent, false)
        return CatViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: CatViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class CatViewHolder(
        private val binding: ItemCatBinding,
        private val listener: CatClickListener
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(data: Cat) {
            binding.textName.text = data.name
            binding.textBrihtDate.text = data.birthDate.formatString()
            binding.imagePhoto.load(data.photo) {
                crossfade(true)
                placeholder(R.color.green_light_secondary)
            }
            binding.card.setOnClickListener {
                listener(data.id)
            }
        }

    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Cat>() {
            override fun areItemsTheSame(oldItem: Cat, newItem: Cat): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Cat, newItem: Cat): Boolean {
                return oldItem == newItem
            }
        }
    }

}