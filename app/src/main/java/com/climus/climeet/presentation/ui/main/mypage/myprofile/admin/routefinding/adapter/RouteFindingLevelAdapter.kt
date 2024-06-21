package com.climus.climeet.presentation.ui.main.mypage.myprofile.admin.routefinding.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.climus.climeet.databinding.ItemRouteFindingLevelBinding
import com.climus.climeet.presentation.ui.main.mypage.myprofile.admin.routefinding.MyPageAdminRouteFindingViewModel
import com.climus.climeet.presentation.ui.main.mypage.myprofile.admin.routefinding.model.LevelColor
import com.climus.climeet.presentation.ui.main.mypage.myprofile.admin.routefinding.model.RouteColor

class RouteFindingLevelAdapter(
    private val viewModel: MyPageAdminRouteFindingViewModel
) : ListAdapter<LevelColor, RouteFindingLevelAdapter.ViewHolder>(DiffCallback()) {

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

    class DiffCallback : DiffUtil.ItemCallback<LevelColor>() {
        override fun areItemsTheSame(oldItem: LevelColor, newItem: LevelColor): Boolean =
            oldItem.color == newItem.color

        override fun areContentsTheSame(oldItem: LevelColor, newItem: LevelColor): Boolean =
            oldItem == newItem
    }

    inner class ViewHolder(private val binding: ItemRouteFindingLevelBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(levelColor: LevelColor) {
            binding.lc = levelColor
            binding.root.setOnClickListener {
                viewModel.selectLevel(levelColor.level)
                viewModel.selectColor(RouteColor(levelColor.color.name, levelColor.color.color))
            }
        }

    }
}