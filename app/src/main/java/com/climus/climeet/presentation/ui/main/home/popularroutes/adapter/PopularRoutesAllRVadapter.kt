package com.climus.climeet.presentation.ui.main.home.popularroutes.adapter

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.climus.climeet.R
import com.climus.climeet.databinding.ItemPopularRoutesAllBinding
import com.climus.climeet.databinding.ItemPopularShortsAllBinding
import com.climus.climeet.presentation.ui.main.home.model.PopularRoute
import com.climus.climeet.presentation.ui.main.home.model.PopularShorts
import com.climus.climeet.presentation.ui.main.home.popularshorts.adapter.PopularShortsAllRVAdapter
import kotlin.math.min

class PopularRoutesAllRVadapter (private val routesList: ArrayList<PopularRoute>) : RecyclerView.Adapter<PopularRoutesAllRVadapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularRoutesAllRVadapter.ViewHolder {
        val binding: ItemPopularRoutesAllBinding = ItemPopularRoutesAllBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PopularRoutesAllRVadapter.ViewHolder, position: Int) {
        holder.bind(routesList[position], position)
    }

    override fun getItemCount(): Int = routesList.size

    inner class ViewHolder(val binding: ItemPopularRoutesAllBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(route: PopularRoute, position: Int){
            if(route.routeImg != null) {
                Glide.with(binding.root.context)
                    .load(route.routeImg)
                    .into(binding.ivPopularRoutesAllImg)
            }

            binding.tvPopularRoutesAllRankingNumber1.text = (position + 1).toString()
            if(position < 3) {
                binding.tvPopularRoutesAllRankingNumber1.setBackgroundResource(R.drawable.oval_mainfill_nostroke_noradius)
                val black: Int = Color.parseColor("#000000")
                binding.tvPopularRoutesAllRankingNumber1.setTextColor(black)
            }
            binding.tvPopularRoutesAllLevel.text = route.level
            binding.tvPopularRoutesAllLocation.text = route.location
            binding.tvPopularRoutesAllSector.text = route.sector
            val colorCode = route.color
            val color: Int = Color.parseColor(colorCode)
            binding.tvPopularRoutesAllLevel.setTextColor(color)
            binding.itemPopularRoutesAllCardView.strokeColor = color

        }
    }

}