package com.climus.climeet.presentation.ui.main.mypage.myprofile.admin.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.climus.climeet.databinding.ItemCreateRouteHoldBinding
import com.climus.climeet.presentation.ui.main.mypage.myprofile.admin.model.UiHoldItem
import com.climus.climeet.presentation.util.DefaultDiffUtil

class CreateRouteHoldAdapter : ListAdapter<UiHoldItem, CreateRouteHoldViewHolder>(
    DefaultDiffUtil<UiHoldItem>()
) {

    override fun onBindViewHolder(holder: CreateRouteHoldViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CreateRouteHoldViewHolder =
        CreateRouteHoldViewHolder(
            ItemCreateRouteHoldBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
}

class CreateRouteHoldViewHolder(private val binding: ItemCreateRouteHoldBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: UiHoldItem) {
        binding.ivHold.setImageResource(item.holdImage)
        binding.ivHold.setOnClickListener {
            item.setImageListener(item.holdImage)
        }
    }
}