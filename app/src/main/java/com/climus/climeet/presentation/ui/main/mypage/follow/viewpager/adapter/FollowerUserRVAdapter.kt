package com.climus.climeet.presentation.ui.main.mypage.follow.viewpager.adapter

import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.climus.climeet.data.model.response.UserFollowingInfoResponse
import com.climus.climeet.databinding.ItemSearchFollowingBinding
import com.climus.climeet.presentation.ui.main.global.searchprofile.model.UserFollowerUiData
import com.climus.climeet.presentation.ui.main.global.searchprofile.model.UserFollowingUiData

class FollowerUserRVAdapter(private val followGymList: List<UserFollowerUiData>) :
    RecyclerView.Adapter<ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding: ItemSearchFollowingBinding = ItemSearchFollowingBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(followGymList[position], position)
    }

    override fun getItemCount(): Int = followGymList.size

}

class ViewHolder(val binding: ItemSearchFollowingBinding) :
    RecyclerView.ViewHolder(binding.root) {

    private val followStatus = SparseBooleanArray()

    fun bind(followGym: UserFollowerUiData, position: Int) {
        if (followGym.imgUrl != null) {
            Glide.with(binding.root.context)
                .load(followGym.imgUrl)
                .into(binding.followingProfileArea)
        }
        if (followGym.isFollowing) {
            binding.btnFollow.visibility = View.VISIBLE
            binding.btnFollowing.visibility = View.INVISIBLE
        }
        binding.tvFollowingName.text = followGym.name
        binding.tvFollowing.text =
            followGym.followers.toString() + "  |  팔로잉 " + followGym.followings.toString()

        val btnFollowing = binding.btnFollowing
        val btnFollow = binding.btnFollow
        val isFollow = followStatus[position]

        binding.btnFollowing.setOnClickListener {
            followStatus.put(position, !isFollow)
            followGym.unFollow(followGym.id)
            btnFollowing.visibility = View.INVISIBLE
            btnFollow.visibility = View.VISIBLE
            binding.tvFollowing.text = (followGym.followers - 1).toString() + "  |  팔로잉 " + followGym.followings.toString()
        }

        binding.btnFollow.setOnClickListener {
            followStatus.put(position, !isFollow)
            followGym.follow(followGym.id)
            btnFollowing.visibility = View.VISIBLE
            btnFollow.visibility = View.INVISIBLE
            binding.tvFollowing.text = (followGym.followers).toString() + "  |  팔로잉 " + followGym.followings.toString()
        }

    }
}