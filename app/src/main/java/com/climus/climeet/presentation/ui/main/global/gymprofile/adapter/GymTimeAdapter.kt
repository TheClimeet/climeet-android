package com.climus.climeet.presentation.ui.main.global.gymprofile.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.climus.climeet.databinding.ItemProfileTimeBinding
import com.climus.climeet.presentation.ui.main.global.gymprofile.model.GymBusinessHour
import com.climus.climeet.presentation.ui.main.global.gymprofile.model.GymPrice

class GymTimeAdapter(): RecyclerView.Adapter<GymTimeViewHolder>() {

    private var timeList : List<GymBusinessHour> = emptyList()

    override fun onBindViewHolder(holder: GymTimeViewHolder, position: Int) {
        holder.bind(timeList[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GymTimeViewHolder =
        GymTimeViewHolder(
            ItemProfileTimeBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    override fun getItemCount(): Int = timeList.size

    @SuppressLint("NotifyDataSetChanged")
    fun setList(list: List<GymBusinessHour>){
        timeList = list
        notifyDataSetChanged()
    }
}

class GymTimeViewHolder(private val binding: ItemProfileTimeBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: GymBusinessHour) {
        var formattedTime = ""

        if (item.hours.size >= 2) {
            val openingTime = item.hours[0]
            val closingTime = item.hours[1]

            formattedTime = "$openingTime - $closingTime"
        }
        binding.tvTime.text = item.day + " " + formattedTime
    }
}