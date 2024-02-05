package com.climus.climeet.presentation.ui.main.record.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.climus.climeet.R
import com.climus.climeet.databinding.ItemSectorImageBinding
import com.climus.climeet.presentation.ui.main.shorts.model.SectorImageUiData

class SectorImageAdapter :
    ListAdapter<SectorImageUiData, SectorImageViewHolder>(diffCallback) {

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<SectorImageUiData>() {
            override fun areItemsTheSame(
                oldItem: SectorImageUiData,
                newItem: SectorImageUiData
            ): Boolean {
                return oldItem.sectorId == newItem.sectorId
            }

            override fun areContentsTheSame(
                oldItem: SectorImageUiData,
                newItem: SectorImageUiData
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onBindViewHolder(holder: SectorImageViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SectorImageViewHolder =
        SectorImageViewHolder(
            ItemSectorImageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
}

class SectorImageViewHolder(private val binding: ItemSectorImageBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: SectorImageUiData) {
        binding.item = item
        if (item.isSelected) {
            binding.vStroke.setBackgroundResource(R.drawable.rect_nofill_mainstroke_6radius)
        } else {
            binding.vStroke.setBackgroundResource(R.drawable.rect_nofill_blackstroke_6radius)
        }
    }
}