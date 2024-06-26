package com.climus.climeet.presentation.ui.main.mypage.follow.viewpager.adapter

import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.climus.climeet.R
import com.climus.climeet.databinding.ItemMypageFollowingCragBinding
import com.climus.climeet.presentation.ui.main.mypage.follow.model.FollowingUiData
import com.climus.climeet.presentation.ui.main.mypage.follow.viewpager.FollowingViewModel

class FollowingGymRVAdapter(
    private val followingGymList: MutableList<FollowingUiData>,
    private val viewModel: FollowingViewModel
) : RecyclerView.Adapter<FollowingGymViewHolder>() {

    private val followStatus =  mutableMapOf<Int, Boolean>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowingGymViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemMypageFollowingCragBinding.inflate(inflater, parent, false)
        return FollowingGymViewHolder(binding, viewModel)
    }

    override fun onBindViewHolder(
        holder: FollowingGymViewHolder,
        position: Int,
    ) {
        val data = followingGymList[position]
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
            viewModel.unfollow(data.userId)
        }

        btnFollow.setOnClickListener {
            followStatus.put(position, !isFollow) // 토글
            data.followerCount += 1
            notifyItemChanged(position)
            viewModel.follow(data.userId)
        }
    }

    override fun getItemCount(): Int = followingGymList.size
}

class FollowingGymViewHolder(
    val binding: ItemMypageFollowingCragBinding,
    val viewModel: FollowingViewModel
) :
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

        binding.root.setOnClickListener{
            viewModel.navigateToProfile(data.userId)
        }
    }
}