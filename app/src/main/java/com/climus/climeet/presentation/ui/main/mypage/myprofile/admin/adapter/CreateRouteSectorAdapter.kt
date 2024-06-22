package com.climus.climeet.presentation.ui.main.mypage.myprofile.admin.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.climus.climeet.databinding.ItemCreateRouteSectorBinding
import com.climus.climeet.presentation.ui.main.mypage.myprofile.admin.model.UiSectorItem

class CreateRouteSectorAdapter : ListAdapter<UiSectorItem, CreateRouteSectorViewHolder>(
    diffCallback
) {
    companion object {

        val diffCallback = object : DiffUtil.ItemCallback<UiSectorItem>() {
            override fun areItemsTheSame(
                oldItem: UiSectorItem,
                newItem: UiSectorItem
            ): Boolean {
                return oldItem.sectorName == newItem.sectorName
            }

            override fun areContentsTheSame(
                oldItem: UiSectorItem,
                newItem: UiSectorItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onBindViewHolder(holder: CreateRouteSectorViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CreateRouteSectorViewHolder =
        CreateRouteSectorViewHolder(
            ItemCreateRouteSectorBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
}

class CreateRouteSectorViewHolder(private val binding: ItemCreateRouteSectorBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: UiSectorItem) {
        binding.item = item
        binding.root.setOnClickListener {
            item.setSectorListener(item.sectorName, item.sectorImg)
        }
    }
}