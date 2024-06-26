package com.climus.climeet.presentation.ui.main.mypage.myprofile.admin.routefinding.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.climus.climeet.databinding.ItemCreateRouteSectorBinding
import com.climus.climeet.databinding.ItemRouteFindingSectorBinding
import com.climus.climeet.presentation.ui.main.mypage.myprofile.admin.model.UiSectorItem
import com.climus.climeet.presentation.ui.main.mypage.myprofile.admin.routefinding.MyPageAdminRouteFindingViewModel

class RouteFindingSectorAdapter(
    private val viewModel: MyPageAdminRouteFindingViewModel
) : ListAdapter<UiSectorItem, CreateRouteSectorViewHolder>(
    diffCallback
) {
    companion object {

        val diffCallback = object : DiffUtil.ItemCallback<UiSectorItem>() {
            override fun areItemsTheSame(oldItem: UiSectorItem, newItem: UiSectorItem): Boolean {
                return oldItem.sectorName == newItem.sectorName
            }

            override fun areContentsTheSame(oldItem: UiSectorItem, newItem: UiSectorItem): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onBindViewHolder(holder: CreateRouteSectorViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CreateRouteSectorViewHolder =
        CreateRouteSectorViewHolder(
            ItemRouteFindingSectorBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
}

class CreateRouteSectorViewHolder(private val binding: ItemRouteFindingSectorBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: UiSectorItem) {
        binding.item = item
        binding.root.setOnClickListener {
            item.setSectorListener(item.sectorName, item.sectorImg)
        }
    }
}