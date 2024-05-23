package com.climus.climeet.presentation.ui.main.mypage.follow.viewpager.adapter

import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.climus.climeet.data.model.response.UserHomeGymDetailResponse
import com.climus.climeet.data.model.response.UserHomeGymSimpleResponse
import com.climus.climeet.databinding.ItemFollowCragsBinding
import com.climus.climeet.databinding.ItemFollowGymBinding
import com.climus.climeet.presentation.ui.main.global.searchprofile.model.UserFollowingUiData

class FollowGymRVAdapter (private val followGymList: List<UserFollowingUiData>,
                          private val itemClickAction: (Long) -> Unit)
    : RecyclerView.Adapter<FollowGymRVAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowGymRVAdapter.ViewHolder {
        val binding: ItemFollowCragsBinding = ItemFollowCragsBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FollowGymRVAdapter.ViewHolder, position: Int) {
        holder.bind(followGymList[position])
        holder.itemView.setOnClickListener {
            itemClickAction(followGymList[position].id)
        }
    }

    override fun getItemCount(): Int = followGymList.size

    inner class ViewHolder(val binding: ItemFollowCragsBinding): RecyclerView.ViewHolder(binding.root){

        private val followStatus = SparseBooleanArray()

        fun bind(followGym:UserFollowingUiData){

            val btnFollowing = binding.btnFollowing
            val btnFollow = binding.btnFollow
            val isFollow = followStatus[position]
            binding.tvCragsFollow.text = (followGym.followers).toString()

            binding.btnFollowing.setOnClickListener {
                followStatus.put(position, !isFollow) // 토글
                followGym.unFollow(followGym.id)
                btnFollowing.visibility = View.INVISIBLE
                btnFollow.visibility = View.VISIBLE
                binding.tvCragsFollow.text = (followGym.followers - 1).toString()
            }

            binding.btnFollow.setOnClickListener {
                followStatus.put(position, !isFollow) // 토글
                followGym.follow(followGym.id)
                btnFollowing.visibility = View.VISIBLE
                btnFollow.visibility = View.INVISIBLE
                binding.tvCragsFollow.text = (followGym.followers).toString()
            }
            if(followGym.imgUrl != null) {
                Glide.with(binding.root.context)
                    .load(followGym.imgUrl)
                    .into(binding.cragsProfileArea)
            }

            binding.tvCragName.text = followGym.name

        }
    }
}