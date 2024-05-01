package com.climus.climeet.presentation.ui.main.global.searchprofile.adapter


import android.annotation.SuppressLint
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.climus.climeet.databinding.ItemFollowingBinding
import com.climus.climeet.presentation.ui.main.global.searchprofile.model.UserFollowingUiData
import com.climus.climeet.presentation.ui.main.mypage.follow.FollowClimber

class FollowingRVAdapter(private val followingList: List<UserFollowingUiData>) : RecyclerView.Adapter<FollowingRVAdapter.ViewHolder>(){

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
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(list: List<FollowClimber>, keyword: String) {
        searchList = list
        this.keyword = keyword
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = followingList.size

    inner class ViewHolder(val binding: ItemFollowingBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(following: UserFollowingUiData) {

            binding.tvFollowing.text = following.followers.toString()

            if (following.imgUrl != null) {
                Glide.with(binding.root.context)
                    .load(following.imgUrl)
                    .into(binding.followingProfileArea)
            }

            binding.followingProfileArea.setOnClickListener {
                following.navigateToProfile(following.id)
            }

            binding.tvFollowingName.text = following.name

            val btnFollowing = binding.btnFollowing
            val btnFollow = binding.btnFollow
            val isFollow = followStatus[position]

            if (isFollow) {
                btnFollowing.visibility = View.INVISIBLE
                btnFollow.visibility = View.VISIBLE
            } else {
                btnFollowing.visibility = View.VISIBLE
                btnFollow.visibility = View.INVISIBLE
            }

            btnFollowing.setOnClickListener {
                followStatus.put(position, !isFollow) // 토글
                following.unFollow(following.id)
                btnFollowing.visibility = View.INVISIBLE
                btnFollow.visibility = View.VISIBLE
                notifyItemChanged(position)
                followingList[position].followers -= 1
            }

            btnFollow.setOnClickListener {
                followStatus.put(position, !isFollow) // 토글
                btnFollowing.visibility = View.VISIBLE
                btnFollow.visibility = View.INVISIBLE
                notifyItemChanged(position)
                followingList[position].followers += 1
            }
        }
    }
}