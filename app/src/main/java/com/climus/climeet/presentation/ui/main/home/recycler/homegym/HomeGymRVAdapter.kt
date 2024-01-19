package com.climus.climeet.presentation.ui.main.home.recycler.homegym

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.climus.climeet.databinding.ItemHomeGymBinding
import com.climus.climeet.presentation.ui.main.home.model.HomeGym

class HomeGymRVAdapter(private val homeGymList: ArrayList<HomeGym>) : RecyclerView.Adapter<HomeGymRVAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemHomeGymBinding = ItemHomeGymBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(homeGymList[position])
    }

    override fun getItemCount(): Int = homeGymList.size

    inner class ViewHolder(val binding: ItemHomeGymBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(homeGym: HomeGym){
            if(homeGym.imgUrl != null) {
                Glide.with(binding.root.context)
                    .load(homeGym.imgUrl)
                    .into(binding.cragProfileArea)
            }
            binding.tvItemCragName.text = homeGym.name
            binding.tvItemFollowers.text = "팔로워 " + homeGym.followers.toString()
        }
    }
}