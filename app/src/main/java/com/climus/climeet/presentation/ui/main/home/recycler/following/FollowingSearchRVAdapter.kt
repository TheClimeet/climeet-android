package com.climus.climeet.presentation.ui.main.home.recycler.following

import android.annotation.SuppressLint
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.climus.climeet.databinding.ItemSearchFollowingBinding
import com.climus.climeet.presentation.ui.main.global.searchprofile.model.FollowClimber

class FollowingSearchRVAdapter() : RecyclerView.Adapter<FollowingSearchRVAdapter.ViewHolder>(){

    private val followStatus = SparseBooleanArray()
    private var searchList: List<FollowClimber> = emptyList()
    private var keyword: String = ""

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding: ItemSearchFollowingBinding = ItemSearchFollowingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(searchList[position], keyword)

        val btnFollowing = holder.binding.btnFollowing
        val btnFollow = holder.binding.btnFollow
        val isFollow = followStatus[position]

        if (isFollow) {
            btnFollowing.visibility = View.INVISIBLE
            btnFollow.visibility = View.VISIBLE
        } else {
            btnFollowing.visibility = View.VISIBLE
            btnFollow.visibility = View.INVISIBLE
        }

        // 팔로우 +1
        btnFollowing.setOnClickListener {
            followStatus.put(position, !isFollow) // 토글
            btnFollowing.visibility = View.INVISIBLE
            btnFollow.visibility = View.VISIBLE
            notifyItemChanged(position)
            val followingItem = searchList[position]
            followingItem.followerCount += 1
        }

        // 팔로우 -1
        btnFollow.setOnClickListener {
            followStatus.put(position, !isFollow) // 토글
            btnFollowing.visibility = View.VISIBLE
            btnFollow.visibility = View.INVISIBLE
            notifyItemChanged(position)
            val followingItem = searchList[position]
            followingItem.followerCount -= 1
        }
    }

    override fun getItemCount(): Int = searchList.size

    @SuppressLint("NotifyDataSetChanged")
    fun setList(list: List<FollowClimber>, keyword: String) {
        searchList = list
        this.keyword = keyword
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ItemSearchFollowingBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(following: FollowClimber, keyword: String) {
            binding.keyword = keyword
            binding.following = following

            binding.tvFollowing.text = following.followerCount.toString()

            if (following.profileImageUrl != null) {
                Glide.with(binding.root.context)
                    .load(following.profileImageUrl)
                    .into(binding.followingProfileArea)
            }
            binding.tvFollowingName.text = following.climberName

            if(following.isFollowing) {
                binding.btnFollowing.visibility = View.INVISIBLE
                binding.btnFollow.visibility = View.VISIBLE
            }

        }
    }
}