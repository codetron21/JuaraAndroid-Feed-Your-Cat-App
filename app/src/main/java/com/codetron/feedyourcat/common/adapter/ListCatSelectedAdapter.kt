package com.codetron.feedyourcat.common.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.codetron.feedyourcat.R
import com.codetron.feedyourcat.databinding.ItemCatBinding
import com.codetron.feedyourcat.model.CatSelectedItem
import com.codetron.feedyourcat.utils.formatString

typealias CatItemSelectedListener = (Long) -> Unit

class ListCatSelectedAdapter(
    private val listener: CatItemSelectedListener? = null
) : RecyclerView.Adapter<ListCatSelectedAdapter.CatSelectedViewHolder>() {

    private val cats = mutableListOf<CatSelectedItem>()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<CatSelectedItem>) {
        cats.clear()
        cats.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatSelectedViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCatBinding.inflate(inflater, parent, false)
        return CatSelectedViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: CatSelectedViewHolder, position: Int) {
        holder.bind(cats[position])
    }

    override fun getItemCount(): Int {
        return cats.size
    }

    inner class CatSelectedViewHolder(
        private val binding: ItemCatBinding,
        private val listener: CatItemSelectedListener? = null
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(data: CatSelectedItem) {
            val catData = data.cat

            setItemSelected(binding.root.context, data.isSelected)
            binding.textName.text = catData.name
            binding.textBrihtDate.text = catData.birthDate.formatString()
            binding.imagePhoto.load(catData.photo) {
                crossfade(true)
                placeholder(R.color.green_secondary)
            }

            binding.card.setOnClickListener {
                listener?.invoke(data.cat.id)
            }

        }

        private fun setItemSelected(context: Context, isSelected: Boolean) {
            if (isSelected.not()) {
                binding.card.strokeWidth = 0
                return
            }

            binding.card.strokeWidth = 2
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

}