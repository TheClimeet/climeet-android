package com.climus.climeet.presentation.ui.main.record.timer.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.climus.climeet.R
import com.climus.climeet.databinding.ItemSectorNameBinding
import com.climus.climeet.presentation.ui.main.global.selectsector.model.SectorNameUiData

class RecordSectorNameAdapter :
    ListAdapter<SectorNameUiData, RecordWallViewHolder>(diffCallback) {

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<SectorNameUiData>() {
            override fun areItemsTheSame(
                oldItem: SectorNameUiData,
                newItem: SectorNameUiData
            ): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(
                oldItem: SectorNameUiData,
                newItem: SectorNameUiData
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onBindViewHolder(holder: RecordWallViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordWallViewHolder =
        RecordWallViewHolder(
            ItemSectorNameBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
}

class RecordWallViewHolder(private val binding: ItemSectorNameBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: SectorNameUiData) {
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