package com.climus.climeet.presentation.ui.main.mypage.follow.viewpager.adapter

import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.climus.climeet.R
import com.climus.climeet.databinding.ItemMypageFollowerUserBinding
import com.climus.climeet.presentation.ui.main.mypage.follow.model.FollowUiData

class FollowerClimberRVAdapter(private val followerClimberList: List<FollowUiData>) :
    RecyclerView.Adapter<FollowerClimberViewHolder>() {

    private val followStatus = SparseBooleanArray()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowerClimberViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemMypageFollowerUserBinding.inflate(inflater, parent, false)
        return FollowerClimberViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: FollowerClimberViewHolder,
        position: Int,
    ) {
        holder.bind(followerClimberList[position])

        val btnFollowing = holder.binding.btnFollowing
        val btnFollow = holder.binding.btnFollow
        val isFollow = followStatus[position]

        if (isFollow) {
            btnFollowing.visibility = View.GONE
            btnFollow.visibility = View.VISIBLE
        } else {
            btnFollowing.visibility = View.VISIBLE
            btnFollow.visibility = View.GONE
        }

        btnFollowing.setOnClickListener {
            followStatus.put(position, !isFollow) // 토글
            btnFollowing.visibility = View.GONE
            btnFollow.visibility = View.VISIBLE
            notifyItemChanged(position)
            followerClimberList[position].followerCount -= 1
        }

        btnFollow.setOnClickListener {
            followStatus.put(position, !isFollow) // 토글
            btnFollowing.visibility = View.VISIBLE
            btnFollow.visibility = View.GONE
            notifyItemChanged(position)
            followerClimberList[position].followerCount += 1
        }
    }

    override fun getItemCount(): Int = followerClimberList.size

}

class FollowerClimberViewHolder(val binding: ItemMypageFollowerUserBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(data: FollowUiData) {
        if (data.profileImageUrl != null) {
            Glide.with(binding.root.context)
                .load(data.profileImageUrl)
                .error(R.drawable.img_profile)
                .into(binding.cvProfile)
        }

        binding.tvName.text = data.userName
        binding.tvFollower.text = data.followerCount.toString()
        binding.tvFollowing.text = data.followingCount.toString()
    }
}