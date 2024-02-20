package com.climus.climeet.presentation.ui.main.mypage.follow.viewpager.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.climus.climeet.data.model.response.UserHomeGymDetailResponse
import com.climus.climeet.databinding.ItemFollowGymBinding
import com.climus.climeet.databinding.ItemSearchFollowingBinding
import com.climus.climeet.presentation.ui.main.home.search.recycler.RouteInfoRVAdapter

class FollowingGymRVAdapter (private val followGymList: List<UserHomeGymDetailResponse>) : RecyclerView.Adapter<FollowingGymRVAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowingGymRVAdapter.ViewHolder {
        val binding: ItemSearchFollowingBinding = ItemSearchFollowingBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FollowingGymRVAdapter.ViewHolder, position: Int) {
        holder.bind(followGymList[position], position)
    }

    override fun getItemCount(): Int = followGymList.size

    inner class ViewHolder(val binding: ItemSearchFollowingBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(followGym: UserHomeGymDetailResponse, position: Int){
            if(followGym.gymProfileUrl != null) {
                Glide.with(binding.root.context)
                    .load(followGym.gymProfileUrl)
                    .into(binding.followingProfileArea)
            }

            binding.tvFollowingName.text = followGym.gymName
            binding.tvFollowing.text = "25,883 | 팔로잉 11"

        }
    }
}