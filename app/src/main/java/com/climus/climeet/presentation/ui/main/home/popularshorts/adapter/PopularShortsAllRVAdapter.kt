package com.climus.climeet.presentation.ui.main.home.popularshorts.adapter

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.climus.climeet.data.model.response.ShortsItem
import com.climus.climeet.databinding.ItemPopularShortsAllBinding
import com.climus.climeet.presentation.ui.main.home.model.PopularShorts
import kotlin.math.min

class PopularShortsAllRVAdapter (private val shortsList: List<ShortsItem>) : RecyclerView.Adapter<PopularShortsAllRVAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularShortsAllRVAdapter.ViewHolder {
        val binding: ItemPopularShortsAllBinding = ItemPopularShortsAllBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PopularShortsAllRVAdapter.ViewHolder, position: Int) {
        holder.bind(shortsList[position])
    }

    override fun getItemCount(): Int = shortsList.size

    inner class ViewHolder(val binding: ItemPopularShortsAllBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(shorts: ShortsItem){
            if(shorts.thumbnailImageUrl != null) {
                Glide.with(binding.root.context)
                    .load(shorts.thumbnailImageUrl)
                    .into(binding.ivShortsAllThumbnail)
            }
            binding.tvShortsAllCragName.text = shorts.gymName
            val circleColorCode = shorts.gymDifficultyColor
            val circleColor: Int = Color.parseColor(circleColorCode)
            binding.ivPopularShortsAllCircle.setColorFilter(circleColor)

            binding.tvPopularShortsAllLevel.text = shorts.climeetDifficultyName
            val levelColorCode = shorts.gymDifficultyColor
            val levelColor: Int = Color.parseColor(levelColorCode)
            val gradientDrawable = GradientDrawable()
            gradientDrawable.shape = GradientDrawable.OVAL
            gradientDrawable.setColor(Color.TRANSPARENT)
            gradientDrawable.setStroke(6, levelColor) // 테두리의 너비와 색상

            binding.tvPopularShortsAllLevel.background = gradientDrawable
            binding.tvPopularShortsAllLevel.setTextColor(levelColor)
        }
    }

}