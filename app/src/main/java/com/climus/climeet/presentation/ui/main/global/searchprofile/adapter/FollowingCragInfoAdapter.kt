package com.climus.climeet.presentation.ui.main.global.searchprofile.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.climus.climeet.R
import com.climus.climeet.data.model.response.RouteSimpleInfo
import com.climus.climeet.data.model.response.UserHomeGymDetailResponse
import com.climus.climeet.databinding.ItemHomegymRouteBinding
import com.climus.climeet.databinding.ItemSearchProfileBinding
import com.climus.climeet.presentation.ui.main.home.recycler.popularroute.PopularRouteRVAdapter

class FollowingCragInfoAdapter(private var followingCrags: List<UserHomeGymDetailResponse>,
                               private val itemClickAction: (Long) -> Unit)
    : RecyclerView.Adapter<FollowingCragInfoAdapter.FollowingCragInfoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowingCragInfoViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemHomegymRouteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FollowingCragInfoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FollowingCragInfoViewHolder, position: Int) {
        holder.bind(followingCrags[position])
        holder.itemView.setOnClickListener {
            itemClickAction(followingCrags[position].gymId)
        }
    }

    override fun getItemCount(): Int = followingCrags.size

    fun updateGyms(newFollowingCrags: List<UserHomeGymDetailResponse>) {
        followingCrags = newFollowingCrags
        notifyDataSetChanged()
    }

    class FollowingCragInfoViewHolder(private val binding: ItemHomegymRouteBinding) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var routeAdapter: RouteRVAdapter

        fun bind(gym: UserHomeGymDetailResponse) {
            binding.tvHomgymName.text = gym.gymName
            if (gym.gymProfileUrl != null) {
                Glide.with(binding.root.context)
                    .load(gym.gymProfileUrl)
                    .into(binding.ivCragProfileArea)
            }

            setupRouteRecyclerView(gym.routeSimpleInfos, gym.gymName)

        }

        private fun setupRouteRecyclerView(routes: List<RouteSimpleInfo>, gymName: String) {
            routeAdapter = RouteRVAdapter(routes, gymName)
            binding.rvSearchPopularRoutes.apply {
                layoutManager = LinearLayoutManager(binding.root.context, RecyclerView.HORIZONTAL, false)
                adapter = routeAdapter
                setRecycledViewPool(RecyclerView.RecycledViewPool())
            }
        }

    }
}
