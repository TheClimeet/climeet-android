package com.climus.climeet.presentation.ui.main.global.searchprofile.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.climus.climeet.R
import com.climus.climeet.data.model.response.BestRouteDetailInfoResponse
import com.climus.climeet.data.model.response.RouteSimpleInfo
import com.climus.climeet.databinding.ItemPopularRoutesBinding
import com.climus.climeet.presentation.util.Constants
import kotlin.math.min

class RouteRVAdapter (private val routeList: List<RouteSimpleInfo>, private val gymName: String) : RecyclerView.Adapter<RouteViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RouteViewHolder {
        val binding: ItemPopularRoutesBinding = ItemPopularRoutesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RouteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RouteViewHolder, position: Int) {
        holder.bind(routeList[position])
        holder.binding.tvPopularRoutesLocation.text = gymName
    }

    override fun getItemCount(): Int {
        return routeList.size
    }

}

class RouteViewHolder(val binding: ItemPopularRoutesBinding): RecyclerView.ViewHolder(binding.root){
    fun bind(route: RouteSimpleInfo) {

        binding.routeView.setRouteImgUrl(route.routeImgUrl)
        binding.routeView.setRouteLevelName(route.gymDifficultyName)
        binding.routeView.setRouteLevelColor(route.gymDifficultyColor)

        val holdImage = Constants.holdColor[route.holdColor] ?: run {
            R.drawable.ic_white_hold
        }

        binding.routeView.setHoldImage(holdImage)
        binding.tvPopularRoutesSector.text = route.sectorName
    }
}
