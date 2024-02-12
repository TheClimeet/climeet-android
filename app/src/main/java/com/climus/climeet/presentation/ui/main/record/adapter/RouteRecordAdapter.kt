package com.climus.climeet.presentation.ui.main.record.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.climus.climeet.databinding.ItemRouteRecordBinding
import com.climus.climeet.presentation.ui.main.global.selectsector.model.RouteUiData
import com.climus.climeet.presentation.ui.main.record.calendar.createclimbingrecord.CreateClimbingRecordViewModel
import com.climus.climeet.presentation.ui.main.record.model.RouteRecordUiData

class RouteRecordAdapter(private val viewModel: CreateClimbingRecordViewModel) :
    RecyclerView.Adapter<RouteRecordViewHolder>() {
    var items: List<RouteUiData> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RouteRecordViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemRouteRecordBinding.inflate(inflater, parent, false)
        return RouteRecordViewHolder(binding, viewModel)
    }

    override fun onBindViewHolder(holder: RouteRecordViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size
}

class RouteRecordViewHolder(
    val binding: ItemRouteRecordBinding,
    private val viewModel: CreateClimbingRecordViewModel
) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(item: RouteUiData) {
        binding.item = item

        binding.root.setOnClickListener {
            viewModel.selectRoute(item)
            viewModel.setChallengeNum(item.challengeNum)
        }

        binding.btnIncrease.setOnClickListener {
            viewModel.itemIncrease(item.routeId)
        }

        binding.btnDecrease.setOnClickListener {
            viewModel.itemDecrease(item.routeId)
        }

        binding.ivDelete.setOnClickListener {
            viewModel.removeItem(item.routeId)
        }
    }
}