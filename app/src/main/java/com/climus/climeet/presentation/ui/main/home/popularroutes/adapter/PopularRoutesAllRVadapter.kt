package com.climus.climeet.presentation.ui.main.home.popularroutes.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.climus.climeet.R
import com.climus.climeet.data.model.response.BestRouteDetailInfoResponse
import com.climus.climeet.databinding.ItemPopularRoutesAllBinding
import com.climus.climeet.presentation.util.Constants

class PopularRoutesAllRVadapter (private val routesList: List<BestRouteDetailInfoResponse>) : RecyclerView.Adapter<PopularRoutesAllRVadapter.ViewHolder>() {

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
        fun bind(route: BestRouteDetailInfoResponse, position: Int){

            binding.routeView.setRouteImgUrl(route.routeImageUrl)
            binding.routeView.setRouteLevelName(route.gymDifficultyName)
            binding.routeView.setRouteLevelColor(route.gymDifficultyColor)

            val holdImage = Constants.holdColor[route.holdColor] ?: run {
                R.drawable.ic_white_hold
            }

            binding.routeView.setHoldImage(holdImage)


            binding.tvPopularRoutesAllRankingNumber1.text = (position + 1).toString()
            if(position < 3) {
                binding.tvPopularRoutesAllRankingNumber1.setBackgroundResource(R.drawable.oval_mainfill_nostroke_noradius)
                val black: Int = Color.parseColor("#000000")
                binding.tvPopularRoutesAllRankingNumber1.setTextColor(black)
            }

            binding.tvPopularRoutesAllLocation.text = route.gymName
            binding.tvPopularRoutesAllSector.text = route.sectorName
        }
    }

}