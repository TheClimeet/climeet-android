package com.climus.climeet.presentation.ui.main.global.gymprofile.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.climus.climeet.databinding.ItemProfileServiceBinding
import com.climus.climeet.presentation.ui.main.global.gymprofile.model.GymService

class GymServiceAdapter() : RecyclerView.Adapter<GymServiceViewHolder>() {

    private var serviceList: List<GymService> = emptyList()

    override fun onBindViewHolder(holder: GymServiceViewHolder, position: Int) {
        holder.bind(serviceList[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GymServiceViewHolder =
        GymServiceViewHolder(
            ItemProfileServiceBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )

    override fun getItemCount() = serviceList.size

    @SuppressLint("NotifyDataSetChanged")
    fun setList(list: List<GymService>) {
        serviceList = list
        notifyDataSetChanged()
    }
}

class GymServiceViewHolder(private val binding: ItemProfileServiceBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: GymService) {
        binding.tvService.text = item.name
    }
}