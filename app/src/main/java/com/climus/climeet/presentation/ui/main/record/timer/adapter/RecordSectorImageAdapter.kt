package com.climus.climeet.presentation.ui.main.record.timer.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.climus.climeet.R
import com.climus.climeet.databinding.ItemTimerSectorImageBinding
import com.climus.climeet.presentation.ui.main.record.model.RecordSectorData

class RecordSectorImageAdapter :
    ListAdapter<RecordSectorData, RecordSectorImageViewHolder>(diffCallback) {

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<RecordSectorData>() {
            override fun areItemsTheSame(
                oldItem: RecordSectorData,
                newItem: RecordSectorData
            ): Boolean {
                return oldItem.sectorId == newItem.sectorId
            }

            override fun areContentsTheSame(
                oldItem: RecordSectorData,
                newItem: RecordSectorData
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onBindViewHolder(holder: RecordSectorImageViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordSectorImageViewHolder =
        RecordSectorImageViewHolder(
            ItemTimerSectorImageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
}

class RecordSectorImageViewHolder(private val binding: ItemTimerSectorImageBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: RecordSectorData) {
        binding.item = item
        if (item.isSelected) {
            binding.vStroke.setBackgroundResource(R.drawable.rect_nofill_mainstroke_6radius)
        } else {
            binding.vStroke.setBackgroundResource(R.drawable.rect_nofill_blackstroke_6radius)
        }
    }
}