package com.climus.climeet.presentation.ui.intro.signup.climer.followcrag

import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.climus.climeet.R
import com.climus.climeet.databinding.FollowCragsRvItemBinding
import com.climus.climeet.presentation.ui.intro.signup.climer.model.Crag

class FollowCragRVAdapter(private val items: MutableList<Crag>) : RecyclerView.Adapter<FollowCragRVAdapter.ViewHolder>(){

    private val followStatus = SparseBooleanArray()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding: FollowCragsRvItemBinding = FollowCragsRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
        holder.itemView.setOnClickListener {
            itemClickListener.onItemClick(items[position])
        }

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

        btnFollowing.setOnClickListener {
            followStatus.put(position, !isFollow) // 토글
            btnFollowing.visibility = View.INVISIBLE
            btnFollow.visibility = View.VISIBLE
            notifyItemChanged(position)
        }

        btnFollow.setOnClickListener {
            followStatus.put(position, !isFollow) // 토글
            btnFollowing.visibility = View.VISIBLE
            btnFollow.visibility = View.INVISIBLE
            notifyItemChanged(position)
        }
    }

    override fun getItemCount(): Int = items.size

    inner class ViewHolder(val binding: FollowCragsRvItemBinding): RecyclerView.ViewHolder(binding.root){
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

    interface OnItemClickListener {
        fun onItemClick(crag : Crag)
    }

    private lateinit var itemClickListener : OnItemClickListener

    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }
}