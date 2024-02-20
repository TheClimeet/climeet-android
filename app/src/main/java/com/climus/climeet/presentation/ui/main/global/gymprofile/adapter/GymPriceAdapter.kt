package com.climus.climeet.presentation.ui.main.global.gymprofile.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.climus.climeet.databinding.ItemProfilePriceBinding
import com.climus.climeet.presentation.ui.main.global.gymprofile.model.GymPrice
import com.climus.climeet.presentation.ui.main.global.gymprofile.model.GymService

class GymPriceAdapter: RecyclerView.Adapter<GymPriceViewHolder>() {

    private var priceList : List<GymPrice> = emptyList()

    override fun onBindViewHolder(holder: GymPriceViewHolder, position: Int) {
        holder.bind(priceList[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GymPriceViewHolder =
        GymPriceViewHolder(
            ItemProfilePriceBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    override fun getItemCount(): Int = priceList.size

    @SuppressLint("NotifyDataSetChanged")
    fun setList(list: List<GymPrice>){
        priceList = list
        notifyDataSetChanged()
    }
}

class GymPriceViewHolder(private val binding: ItemProfilePriceBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: GymPrice) {
        binding.tvType.text = item.productName
        binding.tvPrice.text = item.productPrice
    }
}