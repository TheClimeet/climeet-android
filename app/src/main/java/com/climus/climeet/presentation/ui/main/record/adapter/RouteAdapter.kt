package com.climus.climeet.presentation.ui.main.record.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.climus.climeet.databinding.ItemRouteImageBinding
import com.climus.climeet.presentation.ui.main.global.selectsector.model.RouteUiData

class RouteAdapter :
    ListAdapter<RouteUiData, SectorImageViewHolder>(diffCallback) {

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

    override fun onBindViewHolder(holder: SectorImageViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SectorImageViewHolder =
        SectorImageViewHolder(
            ItemRouteImageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
}

class SectorImageViewHolder(private val binding: ItemRouteImageBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: RouteUiData) {
        binding.item = item
    }
}