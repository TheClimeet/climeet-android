package com.climus.climeet.presentation.ui.main.mypage.follow.viewpager.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.climus.climeet.data.model.response.UserFollowingInfoResponse
import com.climus.climeet.databinding.ItemSearchFollowingBinding

class FollowerUserRVAdapter (private val followGymList: List<UserFollowingInfoResponse>) : RecyclerView.Adapter<FollowerUserRVAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowerUserRVAdapter.ViewHolder {
        val binding: ItemSearchFollowingBinding = ItemSearchFollowingBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FollowerUserRVAdapter.ViewHolder, position: Int) {
        holder.bind(followGymList[position], position)
    }

    override fun getItemCount(): Int = followGymList.size

    inner class ViewHolder(val binding: ItemSearchFollowingBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(followGym: UserFollowingInfoResponse, position: Int){
            if(followGym.profileImgUrl != null) {
                Glide.with(binding.root.context)
                    .load(followGym.profileImgUrl)
                    .into(binding.followingProfileArea)
            }
            if(followGym.isFollower) {
                binding.btnFollow.visibility = View.VISIBLE
                binding.btnFollowing.visibility = View.INVISIBLE
            }

            binding.tvFollowingName.text = followGym.userName
            binding.tvFollowing.text = followGym.followerCount.toString() + " | 팔로잉 " + followGym.followingCount.toString()

        }
    }
}