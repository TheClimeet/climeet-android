package com.climus.climeet.presentation.ui.main.home.recycler.popularroute

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.climus.climeet.databinding.ItemPopularRoutesBinding
import com.climus.climeet.presentation.ui.main.home.model.PopularRoute

class PopularRouteRVAdapter (private val routeList: ArrayList<PopularRoute>) : RecyclerView.Adapter<PopularRouteRVAdapter.ViewHolder>() {
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

    override fun getItemCount(): Int = routeList.size

    inner class ViewHolder(val binding: ItemPopularRoutesBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(route: PopularRoute) {
            if(route.routeImg != null) {
                Glide.with(binding.root.context)
                    .load(route.routeImg)
                    .into(binding.ivPopularRoutesImg)
            }

            binding.tvPopularRoutesLevel.text = route.level
            val colorCode = route.color
            val color: Int = Color.parseColor(colorCode)
            binding.tvPopularRoutesLevel.setTextColor(color)
            binding.itemPopularRoutesCardView.strokeColor = color
            binding.tvPopularRoutesLocation.text = route.location
            binding.tvPopularRoutesSector.text = route.sector
        }
    }
}