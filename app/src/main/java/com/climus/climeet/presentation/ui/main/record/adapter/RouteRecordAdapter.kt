package com.climus.climeet.presentation.ui.main.record.adapter

import android.content.Context
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
        val context = parent.context
        return RouteRecordViewHolder(binding, viewModel)
    }

    override fun onBindViewHolder(holder: RouteRecordViewHolder, position: Int) {
        val context = holder.itemView.context
        holder.bind(items[position], context)
    }

    override fun getItemCount() = items.size
}

class RouteRecordViewHolder(
    val binding: ItemRouteRecordBinding,
    private val viewModel: CreateClimbingRecordViewModel
) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(item: RouteUiData, context: Context) {
        binding.item = item

        binding.root.setOnClickListener {
            viewModel.selectRoute(item)
        }

        binding.btnIncrease.setOnClickListener {
            viewModel.selectRoute(item)
            viewModel.itemIncrease(item.routeId)
        }

        binding.btnDecrease.setOnClickListener {
            viewModel.selectRoute(item)
            viewModel.itemDecrease(item.routeId)
        }

        binding.ivDelete.setOnClickListener {
            viewModel.showDeleteDialog(context, item.routeId)
        }

        binding.ivClear.setOnClickListener {
            viewModel.selectRoute(item)
            viewModel.setBtnState(item.routeId)
        }
    }
}