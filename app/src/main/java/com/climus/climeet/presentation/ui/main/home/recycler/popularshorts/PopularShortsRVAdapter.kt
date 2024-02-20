package com.climus.climeet.presentation.ui.main.home.recycler.popularshorts

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.climus.climeet.data.model.response.ShortsItem
import com.climus.climeet.data.model.response.ShortsSimpleResponse
import com.climus.climeet.databinding.ItemPopularShortsBinding
import com.climus.climeet.presentation.ui.main.home.model.PopularShorts
import com.climus.climeet.presentation.ui.main.shorts.model.ShortsThumbnailUiData
import com.climus.climeet.presentation.ui.main.shorts.model.ShortsUiData
import kotlin.math.min


class PopularShortsRVAdapter (private val shortsList: List<ShortsUiData>) : RecyclerView.Adapter<PopularShortsRVAdapter.ViewHolder>() {
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
        fun bind(shorts: ShortsUiData){
            if(shorts.thumbnailImg != null) {
                Glide.with(binding.root.context)
                    .load(shorts.thumbnailImg)
                    .into(binding.ivShortsThumbnail)
            }
            binding.tvShortsCragName.text = shorts.gymName

            if (shorts.gymLevelColor != null) {
                val circleColor: Int = Color.parseColor(shorts.gymLevelColor)
                binding.ivPopularShortsCircle.setColorFilter(circleColor)

                binding.tvPopularShortsLevel.text = shorts.gymLevelName

                val levelColor: Int = Color.parseColor(shorts.gymLevelColor)
                val gradientDrawable = GradientDrawable()
                gradientDrawable.shape = GradientDrawable.OVAL
                gradientDrawable.setColor(Color.TRANSPARENT)
                gradientDrawable.setStroke(6, levelColor) // 테두리의 너비와 색상

                binding.tvPopularShortsLevel.background = gradientDrawable
                binding.tvPopularShortsLevel.setTextColor(levelColor)
            }

        }
    }

}