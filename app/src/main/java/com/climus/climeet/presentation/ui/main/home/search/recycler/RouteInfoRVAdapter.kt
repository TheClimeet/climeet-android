package com.climus.climeet.presentation.ui.main.home.search.recycler

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.climus.climeet.data.model.response.RouteSimpleInfo
import com.climus.climeet.data.model.response.UserHomeGymDetailResponse
import com.climus.climeet.databinding.ItemFollowGymBinding
import com.climus.climeet.databinding.ItemPopularRoutesBinding
import com.climus.climeet.presentation.ui.main.home.popularroutes.adapter.PopularRoutesAllRVadapter
import com.climus.climeet.presentation.util.Constants

class RouteInfoRVAdapter (private val routeInfos: List<RouteSimpleInfo>, private val gymName : String) : RecyclerView.Adapter<RouteInfoRVAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RouteInfoRVAdapter.ViewHolder {
        val binding: ItemPopularRoutesBinding = ItemPopularRoutesBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RouteInfoRVAdapter.ViewHolder, position: Int) {
        holder.bind(routeInfos[position], position)
    }

    override fun getItemCount(): Int = routeInfos.size

    inner class ViewHolder(val binding: ItemPopularRoutesBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(routeInfo: RouteSimpleInfo, position: Int){
            if(routeInfo.routeImgUrl != null) {
                Glide.with(binding.root.context)
                    .load(routeInfo.routeImgUrl)
                    .into(binding.ivPopularRoutesImg)
            }
            val colorCode = Constants.climeetColor[routeInfo.difficultyName] ?: ""
            var color: Int? = null
            if(colorCode != "") {
                color = Color.parseColor(colorCode)
            }

            binding.tvPopularRoutesLocation.text = gymName
            binding.tvPopularRoutesSector.text = routeInfo.sectorName
            binding.tvPopularRoutesLevel.text = routeInfo.difficultyName
            if(color != null) {
                binding.itemPopularRoutesCardView.strokeColor = color
                binding.tvPopularRoutesLevel.setTextColor(color)
            }

        }
    }
}
