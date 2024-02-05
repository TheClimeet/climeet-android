package com.climus.climeet.presentation.ui.main.record.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.climus.climeet.R
import com.climus.climeet.databinding.ItemSectorNameBinding
import com.climus.climeet.presentation.ui.main.shorts.model.WallNameUiData

class SectorNameAdapter :
    ListAdapter<WallNameUiData, SectorNameViewHolder>(diffCallback) {

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<WallNameUiData>() {
            override fun areItemsTheSame(
                oldItem: WallNameUiData,
                newItem: WallNameUiData
            ): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(
                oldItem: WallNameUiData,
                newItem: WallNameUiData
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onBindViewHolder(holder: SectorNameViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SectorNameViewHolder =
        SectorNameViewHolder(
            ItemSectorNameBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
}

class SectorNameViewHolder(private val binding: ItemSectorNameBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: WallNameUiData) {
        binding.item = item
        if (item.isSelected) {
            binding.chipText.setChipBackgroundColorResource(R.color.cm_main)
            binding.chipText.setTextColor(
                ContextCompat.getColor(
                    binding.chipText.context,
                    R.color.black
                )
            )
        } else {
            binding.chipText.setChipBackgroundColorResource(R.color.cm_silver2)
            binding.chipText.setTextColor(
                ContextCompat.getColor(
                    binding.chipText.context,
                    R.color.white
                )
            )
        }
    }
}