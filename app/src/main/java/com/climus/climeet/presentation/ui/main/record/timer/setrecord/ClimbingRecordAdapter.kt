package com.climus.climeet.presentation.ui.main.record.timer.setrecord

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.climus.climeet.databinding.ItemTimerRouteRecordBinding
import com.climus.climeet.presentation.ui.main.global.selectsector.model.RouteUiData

class ClimbingRecordAdapter(private val viewModel: SetTimerClimbingRecordViewModel) :
    RecyclerView.Adapter<ClimbingRecordViewHolder>() {
    var items: List<RouteUiData> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClimbingRecordViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemTimerRouteRecordBinding.inflate(inflater, parent, false)
        return ClimbingRecordViewHolder(binding, viewModel)
    }

    override fun onBindViewHolder(holder: ClimbingRecordViewHolder, position: Int) {
        val context = holder.itemView.context
        holder.bind(items[position], context)
    }

    override fun getItemCount() = items.size
}

class ClimbingRecordViewHolder(
    val binding: ItemTimerRouteRecordBinding,
    private val viewModel: SetTimerClimbingRecordViewModel
) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(item: RouteUiData, context: Context) {
        binding.item = item

        binding.root.setOnClickListener {
            viewModel.selectRoute(item)
            viewModel.setChallengeNum(item.challengeNum)
        }

        binding.btnIncrease.setOnClickListener {
            viewModel.selectRoute(item)
            viewModel.itemIncrease(item.routeId, 1)
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