package com.climus.climeet.presentation.ui.main.mypage.myprofile.admin.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.climus.climeet.databinding.ItemRouteFindingLevelBinding
import com.climus.climeet.presentation.ui.main.mypage.myprofile.admin.model.UiLevelItem
import com.climus.climeet.presentation.ui.main.mypage.myprofile.admin.routefinding.MyPageAdminRouteFindingViewModel
import com.climus.climeet.presentation.ui.main.mypage.myprofile.admin.model.RouteColor

class RouteFindingLevelAdapter(
    private val viewModel: MyPageAdminRouteFindingViewModel
) : ListAdapter<UiLevelItem, RouteFindingLevelAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRouteFindingLevelBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DiffCallback : DiffUtil.ItemCallback<UiLevelItem>() {
        override fun areItemsTheSame(oldItem: UiLevelItem, newItem: UiLevelItem): Boolean =
            oldItem.colorName == newItem.colorName

        override fun areContentsTheSame(oldItem: UiLevelItem, newItem: UiLevelItem): Boolean =
            oldItem == newItem
    }

    inner class ViewHolder(private val binding: ItemRouteFindingLevelBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: UiLevelItem) {
            binding.item = item
            binding.root.setOnClickListener {
                viewModel.updateModifingLevel(item)
                viewModel.selectLevel(item.climeetLevel)
                viewModel.selectColor(RouteColor(item.colorName, item.colorHex))
                viewModel.updateIsLevelAdd(false)
            }
        }

    }
}