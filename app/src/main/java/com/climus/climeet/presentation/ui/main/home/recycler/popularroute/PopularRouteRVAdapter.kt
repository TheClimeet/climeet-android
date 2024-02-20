package com.climus.climeet.presentation.ui.main.home.recycler.popularroute

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.climus.climeet.R
import com.climus.climeet.data.model.response.BestRouteDetailInfoResponse
import com.climus.climeet.data.model.response.BestRouteSimpleResponse
import com.climus.climeet.databinding.ItemPopularRoutesBinding
import com.climus.climeet.presentation.ui.main.home.model.PopularRoute
import com.climus.climeet.presentation.util.Constants
import kotlin.math.min

class PopularRouteRVAdapter (private val routeList: List<BestRouteDetailInfoResponse>) : RecyclerView.Adapter<PopularRouteRVAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PopularRouteRVAdapter.ViewHolder {
        val binding: ItemPopularRoutesBinding = ItemPopularRoutesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PopularRouteRVAdapter.ViewHolder, position: Int) {
        holder.bind(routeList[position])
    }

    override fun getItemCount(): Int {
        // 최대 10개의 아이템으로 제한
        return min(routeList.size, 10)
    }

    inner class ViewHolder(val binding: ItemPopularRoutesBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(route: BestRouteDetailInfoResponse) {

            binding.routeView.setRouteImgUrl(route.routeImageUrl)
            binding.routeView.setRouteLevelName(route.gymDifficultyName)
            binding.routeView.setRouteLevelColor(route.gymDifficultyColor)

            val holdImage = Constants.holdColor[route.holdColor] ?: run {
                R.drawable.ic_white_hold
            }

            binding.routeView.setHoldImage(holdImage)

            binding.tvPopularRoutesLocation.text = route.gymName
            binding.tvPopularRoutesSector.text = route.sectorName
        }
    }
}