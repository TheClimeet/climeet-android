package com.climus.climeet.presentation.ui.main.mypage.myprofile.admin.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.climus.climeet.databinding.ItemCreateRouteChipBinding
import com.climus.climeet.presentation.ui.main.mypage.myprofile.admin.model.UiRouteChipData

class CreateRouteChipAdapter :
    ListAdapter<UiRouteChipData, CreateRouteChipViewHolder>(diffCallback) {

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<UiRouteChipData>() {
            override fun areItemsTheSame(
                oldItem: UiRouteChipData,
                newItem: UiRouteChipData
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: UiRouteChipData,
                newItem: UiRouteChipData
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onBindViewHolder(holder: CreateRouteChipViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CreateRouteChipViewHolder =
        CreateRouteChipViewHolder(
            ItemCreateRouteChipBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
}

class CreateRouteChipViewHolder(private val binding: ItemCreateRouteChipBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: UiRouteChipData) {
        binding.item = item
        binding.route.setHoldImage(item.holdImg)
    }
}