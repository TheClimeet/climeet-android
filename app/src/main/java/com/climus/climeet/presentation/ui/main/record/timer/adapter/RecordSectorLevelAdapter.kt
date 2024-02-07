package com.climus.climeet.presentation.ui.main.record.timer.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.climus.climeet.R
import com.climus.climeet.databinding.ItemGymLevelBinding
import com.climus.climeet.presentation.ui.main.global.selectsector.model.GymLevelUiData

class RecordSectorLevelAdapter  :
    ListAdapter<GymLevelUiData, RecordSectorLevelViewHolder>(diffCallback) {

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<GymLevelUiData>() {
            override fun areItemsTheSame(
                oldItem: GymLevelUiData,
                newItem: GymLevelUiData
            ): Boolean {
                return oldItem.levelName == newItem.levelName
            }

            override fun areContentsTheSame(
                oldItem: GymLevelUiData,
                newItem: GymLevelUiData
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
            ItemGymLevelBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
}

class RecordSectorLevelViewHolder(private val binding: ItemGymLevelBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: GymLevelUiData) {
        binding.item = item
        if (item.isSelected) {
            binding.tvLevel.setBackgroundResource(R.drawable.oval_silver2fill_mainstroke)
        } else {
            binding.tvLevel.setBackgroundResource(R.drawable.oval_silver2fill_nostroke)
        }
    }
}