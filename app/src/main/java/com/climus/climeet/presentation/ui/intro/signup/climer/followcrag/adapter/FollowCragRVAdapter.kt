package com.climus.climeet.presentation.ui.intro.signup.climer.followcrag.adapter

import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.climus.climeet.R
import com.climus.climeet.databinding.ItemFollowCragsBinding
import com.climus.climeet.presentation.ui.intro.signup.climer.model.Crag

class FollowCragRVAdapter(private val items: MutableList<Crag>) : RecyclerView.Adapter<FollowCragRVAdapter.ViewHolder>(){

    private val followStatus = SparseBooleanArray()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding: ItemFollowCragsBinding = ItemFollowCragsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])

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
            val cragItem = items[position]
            cragItem.followers += 1
        }

        // 팔로우 -1
        btnFollow.setOnClickListener {
            followStatus.put(position, !isFollow) // 토글
            btnFollowing.visibility = View.VISIBLE
            btnFollow.visibility = View.INVISIBLE
            notifyItemChanged(position)
            val cragItem = items[position]
            cragItem.followers -= 1
        }
    }

    override fun getItemCount(): Int = items.size

    inner class ViewHolder(val binding: ItemFollowCragsBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(crag: Crag) {
            if (crag.profileUrl != null) {
                Glide.with(binding.root.context)
                    .load(crag.profileUrl)
                    .into(binding.cragsProfileArea)
            } else {
                binding.cragsProfileArea.setImageResource(R.drawable.shape_light_gray_circle)
            }
            binding.tvCragName.text = crag.name
            binding.tvCragsFollow.text = crag.followers.toString()
            if(crag.isFollowing) {
                binding.btnFollowing.visibility = View.INVISIBLE
                binding.btnFollow.visibility = View.VISIBLE
            }
        }
    }
}