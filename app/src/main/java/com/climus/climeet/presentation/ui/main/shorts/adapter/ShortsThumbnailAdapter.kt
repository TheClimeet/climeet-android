package com.climus.climeet.presentation.ui.main.shorts.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.climus.climeet.databinding.ItemShortsThumbnailBinding
import com.climus.climeet.presentation.ui.main.shorts.model.ShortsThumbnailUiData
import com.climus.climeet.presentation.util.DefaultDiffUtil

class ShortsThumbnailAdapter :
    ListAdapter<ShortsThumbnailUiData, ShortsThumbnailViewHolder>(DefaultDiffUtil<ShortsThumbnailUiData>()) {

    override fun onBindViewHolder(holder: ShortsThumbnailViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShortsThumbnailViewHolder =
        ShortsThumbnailViewHolder(
            ItemShortsThumbnailBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
}

class ShortsThumbnailViewHolder(private val binding: ItemShortsThumbnailBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: ShortsThumbnailUiData) {

        binding.item = item

        with(binding) {
            vCragLevelColor.cvColor = item.originLevelColor.toColorInt()
            ivClimeetLevel.ecColor = item.climeetLevelColor.toColorInt()
        }
    }

}