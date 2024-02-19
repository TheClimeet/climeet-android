package com.climus.climeet.presentation.ui.main.shorts.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.climus.climeet.databinding.ItemAddFollowBinding
import com.climus.climeet.databinding.ItemUpdatedFollowBinding
import com.climus.climeet.presentation.ui.main.shorts.model.UpdatedFollowUiData
import com.climus.climeet.presentation.util.DefaultDiffUtil

class UpdatedFollowAdapter :
    ListAdapter<UpdatedFollowUiData, RecyclerView.ViewHolder>(DefaultDiffUtil<UpdatedFollowUiData>()) {

    companion object {
        const val ITEM = 0
        const val GOTO_FOLLOW = 1
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is UpdatedFollowViewHolder -> holder.bind(getItem(position))
            is NavigateToAddFollowViewHolder -> holder.bind(getItem(position))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =

        when (viewType) {
            ITEM -> {
                UpdatedFollowViewHolder(
                    ItemUpdatedFollowBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }

            GOTO_FOLLOW -> {
                NavigateToAddFollowViewHolder(
                    ItemAddFollowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                )
            }

            else -> {
                UpdatedFollowViewHolder(
                    ItemUpdatedFollowBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
        }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        return item.viewType
    }
}

class UpdatedFollowViewHolder(private val binding: ItemUpdatedFollowBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: UpdatedFollowUiData) {

        if(item.name.length > 6){
            binding.tvNick.text = item.name.substring(0..6) + "\n" + item.name.substring(7)
        } else {
            binding.tvNick.text = item.name
        }
        binding.item = item
    }
}

class NavigateToAddFollowViewHolder(private val binding: ItemAddFollowBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: UpdatedFollowUiData) {

        binding.root.setOnClickListener {
            item.navigateToAddFollow
        }
    }
}