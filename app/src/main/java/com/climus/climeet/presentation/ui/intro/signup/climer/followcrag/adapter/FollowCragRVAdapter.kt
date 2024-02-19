package com.climus.climeet.presentation.ui.intro.signup.climer.followcrag.adapter

import android.annotation.SuppressLint
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.climus.climeet.R
import com.climus.climeet.databinding.ItemFollowCragsBinding
import com.climus.climeet.presentation.ui.intro.signup.admin.model.SearchCragUiData
import com.climus.climeet.presentation.ui.intro.signup.climer.ClimerSignupForm
import com.climus.climeet.presentation.ui.intro.signup.climer.model.FollowCrag

class FollowCragRVAdapter() : RecyclerView.Adapter<FollowCragRVAdapter.ViewHolder>(){

    private val followStatus = SparseBooleanArray()
    private var searchList: List<FollowCrag> = emptyList()
    private var keyword: String = ""

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding: ItemFollowCragsBinding = ItemFollowCragsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
            val cragItem = searchList[position]
            cragItem.followers += 1
            ClimerSignupForm.addFollowGym(searchList[position].id)
        }

        // 팔로우 -1
        btnFollow.setOnClickListener {
            followStatus.put(position, !isFollow) // 토글
            btnFollowing.visibility = View.VISIBLE
            btnFollow.visibility = View.INVISIBLE
            notifyItemChanged(position)
            val cragItem = searchList[position]
            cragItem.followers -= 1
            ClimerSignupForm.removeFollowGym(searchList[position].id)
        }
    }

    override fun getItemCount(): Int = searchList.size

    @SuppressLint("NotifyDataSetChanged")
    fun setList(list: List<FollowCrag>, keyword: String) {
        searchList = list
        this.keyword = keyword
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ItemFollowCragsBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(followCrag: FollowCrag, keyword: String) {
            binding.keyword = keyword
            binding.crag = followCrag

            binding.tvCragsFollow.text = followCrag.followers.toString()

            if (followCrag.imgUrl != null) {
                Glide.with(binding.root.context)
                    .load(followCrag.imgUrl)
                    .into(binding.cragsProfileArea)
            } else {
                binding.cragsProfileArea.setImageResource(R.drawable.oval_lightgreyfill_nostroke_noradius)
            }
            binding.tvCragName.text = followCrag.name
            binding.tvCragsFollow.text = followCrag.followers.toString()
            if(followCrag.isFollowing) {
                binding.btnFollowing.visibility = View.INVISIBLE
                binding.btnFollow.visibility = View.VISIBLE
            }
        }
    }
}