package com.climus.climeet.presentation.ui.main.mypage.myprofile.admin.routefinding.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.climus.climeet.databinding.ItemRouteFindingLevelColorBinding
import com.climus.climeet.presentation.ui.main.mypage.myprofile.admin.routefinding.MyPageAdminRouteFindingViewModel
import com.climus.climeet.presentation.ui.main.mypage.myprofile.admin.routefinding.model.RouteColor

class LevelColorAdapter(
    private val viewModel: MyPageAdminRouteFindingViewModel
) : ListAdapter<RouteColor, LevelColorAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRouteFindingLevelColorBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DiffCallback : DiffUtil.ItemCallback<RouteColor>() {
        override fun areItemsTheSame(oldItem: RouteColor, newItem: RouteColor): Boolean =
            oldItem.color == newItem.color

        override fun areContentsTheSame(oldItem: RouteColor, newItem: RouteColor): Boolean =
            oldItem == newItem
    }

    inner class ViewHolder(private val binding: ItemRouteFindingLevelColorBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(routeColor: RouteColor) {
            binding.rc = routeColor
            binding.executePendingBindings()
            updateSelection(routeColor)

            itemView.setOnClickListener {
                viewModel.selectColor(routeColor)
                notifyDataSetChanged()
            }
        }

        private fun updateSelection(routeColor: RouteColor) {
            if(routeColor.name == viewModel.selectedColor.value.name) {
                binding.circleOutside.visibility = View.VISIBLE
            } else {
                binding.circleOutside.visibility = View.GONE
            }

            if(routeColor.name == "컴피") {
                binding.tvColorName.text = "C"
            } else {
                binding.tvColorName.text = ""
            }
        }
    }
}