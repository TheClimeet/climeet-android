package com.climus.climeet.presentation.ui.main.global.selectsector.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.climus.climeet.databinding.ItemRouteImageBinding
import com.climus.climeet.presentation.ui.main.global.selectsector.model.RouteUiData

class RouteImageAdapter :
    ListAdapter<RouteUiData, RouteImageViewHolder>(diffCallback) {

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<RouteUiData>() {
            override fun areItemsTheSame(
                oldItem: RouteUiData,
                newItem: RouteUiData
            ): Boolean {
                return oldItem.sectorId == newItem.sectorId
            }

            override fun areContentsTheSame(
                oldItem: RouteUiData,
                newItem: RouteUiData
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onBindViewHolder(holder: RouteImageViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RouteImageViewHolder =
        RouteImageViewHolder(
            ItemRouteImageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
}

class RouteImageViewHolder(private val binding: ItemRouteImageBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: RouteUiData) {
        binding.item = item
        binding.route.setHoldImage(item.holdImg)
        binding.root.setOnClickListener {
            item.onClickListener(item)
        }
    }
}