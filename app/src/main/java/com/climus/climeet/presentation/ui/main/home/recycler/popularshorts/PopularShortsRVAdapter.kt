package com.climus.climeet.presentation.ui.main.home.recycler.popularshorts

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.climus.climeet.databinding.ItemPopularShortsBinding
import com.climus.climeet.presentation.ui.main.shorts.model.ShortsThumbnailUiData
import kotlin.math.min


class PopularShortsRVAdapter (private val shortsList: List<ShortsThumbnailUiData>) : RecyclerView.Adapter<PopularShortsRVAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularShortsRVAdapter.ViewHolder {
        val binding: ItemPopularShortsBinding = ItemPopularShortsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PopularShortsRVAdapter.ViewHolder, position: Int) {
        holder.bind(shortsList[position])
    }

    override fun getItemCount(): Int {
        // 최대 10개의 아이템으로 제한
        return min(shortsList.size, 10)
    }

    inner class ViewHolder(val binding: ItemPopularShortsBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(shorts: ShortsThumbnailUiData){

            binding.root.setOnClickListener {
                shorts.onClickListener(shorts.shortsId, absoluteAdapterPosition)
            }
            if(shorts.thumbnailImg != null) {
                Glide.with(binding.root.context)
                    .load(shorts.thumbnailImg)
                    .into(binding.ivShortsThumbnail)
            }
            binding.tvShortsCragName.text = shorts.gymName

            if (shorts.originLevelColor != null) {
                val circleColor: Int = Color.parseColor(shorts.originLevelColor)
                binding.ivPopularShortsCircle.setColorFilter(circleColor)
            }

        }
    }

}