package com.climus.climeet.presentation.ui.main.mypage.follow.viewpager.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.climus.climeet.R
import com.climus.climeet.data.model.response.RouteSimpleInfo
import com.climus.climeet.databinding.ItemPopularRoutesBinding
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

            binding.routeView.setRouteImgUrl(routeInfo.routeImgUrl)
            binding.routeView.setRouteLevelName(routeInfo.gymDifficultyName)
            binding.routeView.setRouteLevelColor(routeInfo.gymDifficultyColor)

            val holdImage = Constants.holdColor[routeInfo.holdColor] ?: run {
                R.drawable.ic_white_hold
            }

            binding.routeView.setHoldImage(holdImage)


            binding.tvPopularRoutesLocation.text = gymName
            binding.tvPopularRoutesSector.text = routeInfo.sectorName

        }
    }
}