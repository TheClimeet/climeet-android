package com.climus.climeet.presentation.ui.main.mypage.follow


import android.annotation.SuppressLint
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.climus.climeet.data.model.response.UserFollowSimpleResponse
import com.climus.climeet.databinding.ItemFollowingBinding

class FollowingRVAdapter(private val followingList: List<UserFollowSimpleResponse>) : RecyclerView.Adapter<FollowingRVAdapter.ViewHolder>(){

    private val followStatus = SparseBooleanArray()
    private var searchList: List<FollowClimber> = emptyList()
    private var keyword: String = ""

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding: ItemFollowingBinding = ItemFollowingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bind(followingList[position])

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
            followingList[position].followerCount += 1
        }

        // 팔로우 -1
        btnFollow.setOnClickListener {
            followStatus.put(position, !isFollow) // 토글
            btnFollowing.visibility = View.VISIBLE
            btnFollow.visibility = View.INVISIBLE
            notifyItemChanged(position)
            followingList[position].followerCount -= 1
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(list: List<FollowClimber>, keyword: String) {
        searchList = list
        this.keyword = keyword
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = followingList.size

    inner class ViewHolder(val binding: ItemFollowingBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(following: UserFollowSimpleResponse) {

            binding.tvFollowing.text = following.followerCount.toString()

            if (following.userProfileUrl != null) {
                Glide.with(binding.root.context)
                    .load(following.userProfileUrl)
                    .into(binding.followingProfileArea)
            }
            binding.tvFollowingName.text = following.userName

        }
    }
}