package com.climus.climeet.presentation.ui.main.shorts.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.climus.climeet.R
import com.climus.climeet.databinding.ItemSectorLevelBinding
import com.climus.climeet.presentation.ui.main.shorts.model.SectorLevelUiData

class SectorLevelAdapter :
    ListAdapter<SectorLevelUiData, SectorLevelViewHolder>(diffCallback) {

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<SectorLevelUiData>() {
            override fun areItemsTheSame(
                oldItem: SectorLevelUiData,
                newItem: SectorLevelUiData
            ): Boolean {
                return oldItem.levelName == newItem.levelName
            }

            override fun areContentsTheSame(
                oldItem: SectorLevelUiData,
                newItem: SectorLevelUiData
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onBindViewHolder(holder: SectorLevelViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SectorLevelViewHolder =
        SectorLevelViewHolder(
            ItemSectorLevelBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
}

class SectorLevelViewHolder(private val binding: ItemSectorLevelBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: SectorLevelUiData) {
        binding.item = item
        binding.tvLevel.setTextColor(item.levelColor.toColorInt())
        if (item.isSelected) {
            binding.tvLevel.setBackgroundResource(R.drawable.oval_silver2fill_mainstroke)
        } else {
            binding.tvLevel.setBackgroundResource(R.drawable.oval_silver2fill_nostroke)
        }
    }
}