package com.climus.climeet.presentation.ui.main.global.climerprofile.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.climus.climeet.databinding.ItemHomeGymBinding
import com.climus.climeet.presentation.ui.main.global.climerprofile.model.ProfileHomeGymUiData
import com.climus.climeet.presentation.util.DefaultDiffUtil

class HomeGymAdapter: ListAdapter<ProfileHomeGymUiData, HomeGymViewHolder>(DefaultDiffUtil<ProfileHomeGymUiData>()) {

    override fun onBindViewHolder(holder: HomeGymViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeGymViewHolder = HomeGymViewHolder(
        ItemHomeGymBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )
}

class HomeGymViewHolder(private val binding: ItemHomeGymBinding): RecyclerView.ViewHolder(binding.root){

    fun bind(item: ProfileHomeGymUiData){
        binding.item = item
        binding.root.setOnClickListener {
            item.onClickListener(item.gymId)
        }
    }

}