package com.climus.climeet.presentation.ui.main.global.searchprofile.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.climus.climeet.data.model.response.UserHomeGymDetailResponse
import com.climus.climeet.databinding.ItemFollowGymBinding

class FollowGymRVAdapter (private val followGymList: List<UserHomeGymDetailResponse>) : RecyclerView.Adapter<FollowGymRVAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemFollowGymBinding = ItemFollowGymBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(followGymList[position], position)
    }

    override fun getItemCount(): Int = followGymList.size

    inner class ViewHolder(val binding: ItemFollowGymBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(followGym: UserHomeGymDetailResponse, position: Int){
            if(followGym.gymProfileUrl != null) {
                Glide.with(binding.root.context)
                    .load(followGym.gymProfileUrl)
                    .into(binding.followProfileArea)
            }

            binding.tvFollowGymName.text = followGym.gymName
            binding.rvSearchCragRoutes.adapter = RouteInfoRVAdapter(followGym.routeSimpleInfos, followGym.gymName)

        }
    }
}