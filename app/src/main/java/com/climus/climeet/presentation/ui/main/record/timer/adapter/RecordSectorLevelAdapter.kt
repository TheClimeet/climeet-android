package com.climus.climeet.presentation.ui.main.record.timer.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.climus.climeet.R
import com.climus.climeet.databinding.ItemTimerSectorLevelBinding
import com.climus.climeet.presentation.ui.main.record.model.RecordLevelData

class RecordSectorLevelAdapter  :
    ListAdapter<RecordLevelData, RecordSectorLevelViewHolder>(diffCallback) {

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<RecordLevelData>() {
            override fun areItemsTheSame(
                oldItem: RecordLevelData,
                newItem: RecordLevelData
            ): Boolean {
                return oldItem.levelName == newItem.levelName
            }

            override fun areContentsTheSame(
                oldItem: RecordLevelData,
                newItem: RecordLevelData
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onBindViewHolder(holder: RecordSectorLevelViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordSectorLevelViewHolder =
        RecordSectorLevelViewHolder(
            ItemTimerSectorLevelBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
}

class RecordSectorLevelViewHolder(private val binding: ItemTimerSectorLevelBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: RecordLevelData) {
        binding.item = item
        if (item.isSelected) {
            binding.tvLevel.setBackgroundResource(R.drawable.oval_silver2fill_mainstroke)
        } else {
            binding.tvLevel.setBackgroundResource(R.drawable.oval_silver2fill_nostroke)
        }
    }
}