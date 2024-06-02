package com.climus.climeet.presentation.ui.main.record.stats

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.climus.climeet.R
import com.climus.climeet.databinding.ItemStatsGymBinding
import com.climus.climeet.presentation.ui.main.record.model.SelectGymData

class SelectGymAdapter(
    private val selectedItemId: Long,
    private val itemClickListener: (SelectGymData) -> Unit
) :
    ListAdapter<SelectGymData, SelectGymAdapter.SelectGymViewHolder>(diffCallback) {

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<SelectGymData>() {
            override fun areItemsTheSame(
                oldItem: SelectGymData,
                newItem: SelectGymData
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: SelectGymData,
                newItem: SelectGymData
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onBindViewHolder(holder: SelectGymViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectGymViewHolder =
        SelectGymViewHolder(
            ItemStatsGymBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    inner class SelectGymViewHolder(private val binding: ItemStatsGymBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SelectGymData) {
            if (item.id == selectedItemId) {
                binding.tvGymName.setBackgroundResource(R.drawable.rect_mainfill_nostroke_999radius_8elevation)
                binding.tvGymName.setTextColor(Color.BLACK)
            } else {
                binding.tvGymName.setBackgroundResource(com.kakao.sdk.friend.R.color.transparent)
                binding.tvGymName.setTextColor(Color.WHITE)
            }

            binding.item = item
            binding.root.setOnClickListener {
                item.onClickListener(item)
                itemClickListener(item)
            }
        }
    }

}