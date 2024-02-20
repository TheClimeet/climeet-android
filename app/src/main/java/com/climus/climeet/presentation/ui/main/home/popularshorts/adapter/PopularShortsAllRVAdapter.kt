package com.climus.climeet.presentation.ui.main.home.popularshorts.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.climus.climeet.databinding.ItemPopularShortsAllBinding
import com.climus.climeet.presentation.ui.main.shorts.model.ShortsThumbnailUiData

class PopularShortsAllRVAdapter(private val shortsList: List<ShortsThumbnailUiData>) :
    RecyclerView.Adapter<PopularShortsAllRVAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PopularShortsAllRVAdapter.ViewHolder {
        val binding: ItemPopularShortsAllBinding =
            ItemPopularShortsAllBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PopularShortsAllRVAdapter.ViewHolder, position: Int) {
        holder.bind(shortsList[position])
    }

    override fun getItemCount(): Int = shortsList.size

    inner class ViewHolder(val binding: ItemPopularShortsAllBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(shorts: ShortsThumbnailUiData) {
            binding.root.setOnClickListener {
                shorts.onClickListener(shorts.shortsId, absoluteAdapterPosition)
            }

            if (shorts.thumbnailImg != null) {
                Glide.with(binding.root.context)
                    .load(shorts.thumbnailImg)
                    .into(binding.ivShortsAllThumbnail)
            }
            binding.tvShortsAllCragName.text = shorts.gymName

            if (shorts.originLevelColor != null) {
                val circleColor: Int = Color.parseColor(shorts.originLevelColor)
                binding.ivPopularShortsAllCircle.setColorFilter(circleColor)

            }
        }
    }

}