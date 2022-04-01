package com.codetron.feedyourcat.common.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.codetron.feedyourcat.R
import com.codetron.feedyourcat.databinding.ItemSortByBinding
import com.codetron.feedyourcat.model.SortItem

typealias SortClickListener = (Long) -> Unit

class ListSortAdapter(private val clickListener: SortClickListener? = null) :
    RecyclerView.Adapter<ListSortAdapter.SortViewHolder>() {

    private val data = mutableListOf<SortItem>()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(newData: List<SortItem>) {
        data.clear()
        data.addAll(newData)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SortViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemSortByBinding.inflate(inflater, parent, false)
        return SortViewHolder(binding, clickListener)
    }

    override fun onBindViewHolder(holder: SortViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class SortViewHolder(
        private val binding: ItemSortByBinding,
        private val clickListener: SortClickListener? = null
    ) : RecyclerView.ViewHolder(binding.root) {

        private val context by lazy { binding.root.context }

        fun bind(item: SortItem) {
            binding.textTitle.text = context.getString(item.title)
            binding.textTitle.setTextColor(
                ContextCompat.getColor(
                    context,
                    if (item.isSelected) R.color.orange_primary else R.color.black
                )
            )

            binding.root.setOnClickListener {
                clickListener?.invoke(item.id)
            }
        }

    }

}