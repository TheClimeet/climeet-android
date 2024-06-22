package com.climus.climeet.presentation.ui.main.mypage.myprofile.admin.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.climus.climeet.databinding.ItemCreateRouteLevelBinding
import com.climus.climeet.presentation.ui.main.mypage.myprofile.admin.model.UiLevelItem
import com.climus.climeet.presentation.util.DefaultDiffUtil

class CreateRouteLevelAdapter : ListAdapter<UiLevelItem, CreateRouteLevelViewHolder>(
    DefaultDiffUtil<UiLevelItem>()
) {

    override fun onBindViewHolder(holder: CreateRouteLevelViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CreateRouteLevelViewHolder =
        CreateRouteLevelViewHolder(
            ItemCreateRouteLevelBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
}

class CreateRouteLevelViewHolder(private val binding: ItemCreateRouteLevelBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: UiLevelItem) {
        binding.item = item
        binding.root.setOnClickListener {
            item.setLevelListener(item.colorName, item.colorHex)
        }
    }
}