package com.climus.climeet.presentation.ui.main.shorts.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.climus.climeet.databinding.ItemShortsThumbnailBinding
import com.climus.climeet.presentation.ui.main.shorts.model.ShortsThumbnailUiData
import com.climus.climeet.presentation.util.Constants.TAG
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
        binding.root.setOnClickListener {
            item.onClickListener(item.shortsId, absoluteAdapterPosition)
        }

        with(binding) {
            item.originLevelColor?.let {
                vCragLevelColor.cvColor = it.toColorInt()
            } ?: run {
                layoutLabel.visibility = View.INVISIBLE
            }

            ivClimeetLevel.ecText = item.climeetDifficultyName.toString()


            if (item.climeetLevelColor.isNotBlank()) {
                ivClimeetLevel.ecColor = item.climeetLevelColor.toColorInt()
            } else {
                ivClimeetLevel.visibility = View.INVISIBLE
            }
        }
    }

}