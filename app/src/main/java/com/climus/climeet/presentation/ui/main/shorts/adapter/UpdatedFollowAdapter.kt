package com.climus.climeet.presentation.ui.main.shorts.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.climus.climeet.databinding.ItemUpdatedFollowBinding
import com.climus.climeet.presentation.ui.main.shorts.model.UpdatedFollowUiData
import com.climus.climeet.presentation.util.DefaultDiffUtil

class UpdatedFollowAdapter :
    ListAdapter<UpdatedFollowUiData, UpdatedFollowViewHolder>(DefaultDiffUtil<UpdatedFollowUiData>()) {

    override fun onBindViewHolder(holder: UpdatedFollowViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UpdatedFollowViewHolder =
        UpdatedFollowViewHolder(
            ItemUpdatedFollowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
}

class UpdatedFollowViewHolder(private val binding: ItemUpdatedFollowBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: UpdatedFollowUiData) {

        binding.item = item
    }
}