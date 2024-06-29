package com.climus.climeet.presentation.ui.main.mypage.myprofile.admin.routefinding.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.climus.climeet.databinding.ItemRouteFindingRouteBinding
import com.climus.climeet.presentation.ui.main.mypage.myprofile.admin.adapter.CreateRouteChipAdapter
import com.climus.climeet.presentation.ui.main.mypage.myprofile.admin.model.UiRouteItem
import com.climus.climeet.presentation.ui.main.mypage.myprofile.admin.model.UiSectorItem
import com.climus.climeet.presentation.ui.main.mypage.myprofile.admin.routefinding.MyPageAdminRouteFindingViewModel

class RouteFindingRouteAdapter(
    private val viewModel: MyPageAdminRouteFindingViewModel
) :
    ListAdapter<UiRouteItem, RouteFindingRouteAdapter.RouteFindingRouteViewHolder>(
        diffCallback
    ) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RouteFindingRouteViewHolder =
        RouteFindingRouteViewHolder(
            ItemRouteFindingRouteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )


    override fun onBindViewHolder(holder: RouteFindingRouteViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class RouteFindingRouteViewHolder(private val binding: ItemRouteFindingRouteBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: UiRouteItem) {
            binding.item = item
            binding.rvRouteChip.adapter = CreateRouteChipAdapter(viewModel)
        }
    }

    companion object {

        val diffCallback = object : DiffUtil.ItemCallback<UiRouteItem>() {
            override fun areItemsTheSame(oldItem: UiRouteItem, newItem: UiRouteItem): Boolean {
                return oldItem.sectorName == newItem.sectorName
            }

            override fun areContentsTheSame(oldItem: UiRouteItem, newItem: UiRouteItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}