package com.climus.climeet.presentation.ui.main.record.timer.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.climus.climeet.R
import com.climus.climeet.databinding.ItemTimerSectorNameBinding
import com.climus.climeet.presentation.ui.main.record.model.RecordWallData

class RecordWallNameAdapter :
    ListAdapter<RecordWallData, RecordWallViewHolder>(diffCallback) {

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<RecordWallData>() {
            override fun areItemsTheSame(
                oldItem: RecordWallData,
                newItem: RecordWallData
            ): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(
                oldItem: RecordWallData,
                newItem: RecordWallData
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
            ItemTimerSectorNameBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
}

class RecordWallViewHolder(private val binding: ItemTimerSectorNameBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: RecordWallData) {
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