package com.climus.climeet.presentation.ui.main.home.recycler.popularcrag

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.climus.climeet.databinding.ItemPopularCragsBinding
import com.climus.climeet.presentation.ui.main.home.model.PopularCrag

class PopularCragRVAdapter (private val cragList: ArrayList<PopularCrag>) : RecyclerView.Adapter<PopularCragRVAdapter.ViewHolder>(){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PopularCragRVAdapter.ViewHolder {
        val binding: ItemPopularCragsBinding = ItemPopularCragsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PopularCragRVAdapter.ViewHolder, position: Int) {
        holder.bind(cragList[position])
    }

    override fun getItemCount(): Int = cragList.size

    inner class ViewHolder(val binding: ItemPopularCragsBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(crag: PopularCrag) {
            if(crag.profileImg != null) {
                Glide.with(binding.root.context)
                    .load(crag.profileImg)
                    .into(binding.ivPopularCragProfile)
            }
            binding.tvItemCragName.text = crag.cragName
            binding.popularCragRanking.text = (position + 1).toString()
            binding.tvItemFollowers.text = "팔로워 " + crag.followers.toString()
            if(crag.isSoaring) {
                binding.ivSoaring.visibility = View.VISIBLE
                binding.tvSoaring.visibility = View.VISIBLE
            }
        }
    }
}