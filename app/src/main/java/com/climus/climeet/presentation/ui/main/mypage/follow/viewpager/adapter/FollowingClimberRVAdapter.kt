package com.climus.climeet.presentation.ui.main.mypage.follow.viewpager.adapter

import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.climus.climeet.R
import com.climus.climeet.databinding.ItemMypageFollowingUserBinding
import com.climus.climeet.presentation.ui.main.mypage.follow.model.FollowUiData
import com.climus.climeet.presentation.ui.main.mypage.follow.model.FollowingUiData

class FollowingClimberRVAdapter(
    private val followingClimberList: MutableList<FollowingUiData>
) : RecyclerView.Adapter<FollowingClimberViewHolder>() {

    private val followStatus =  mutableMapOf<Int, Boolean>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowingClimberViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemMypageFollowingUserBinding.inflate(inflater, parent, false)
        return FollowingClimberViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FollowingClimberViewHolder, position: Int) {
        val data = followingClimberList[position]
        holder.bind(data)

        val btnFollowing = holder.binding.btnFollowing
        val btnFollow = holder.binding.btnFollow
        val isFollow = followStatus[position] ?: false

        if (isFollow) {
            btnFollowing.visibility = View.GONE
            btnFollow.visibility = View.VISIBLE
        } else {
            btnFollowing.visibility = View.VISIBLE
            btnFollow.visibility = View.GONE
        }

        btnFollowing.setOnClickListener {
            followStatus.put(position, !isFollow)
            data.followerCount -= 1
            notifyItemChanged(position)
        }

        btnFollow.setOnClickListener {
            followStatus.put(position, !isFollow) // 토글
            data.followerCount += 1
            notifyItemChanged(position)
        }
    }

    override fun getItemCount(): Int = followingClimberList.size
}

class FollowingClimberViewHolder(val binding: ItemMypageFollowingUserBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(data: FollowingUiData) {

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