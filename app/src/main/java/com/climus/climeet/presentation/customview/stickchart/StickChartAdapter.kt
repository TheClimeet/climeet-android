package com.climus.climeet.presentation.customview.stickchart

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintLayout.LayoutParams
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.climus.climeet.databinding.ItemStickChartBinding
import com.climus.climeet.presentation.util.Constants.TAG
import com.climus.climeet.presentation.util.DefaultDiffUtil

class StickChartAdapter: ListAdapter<StickChartUiData,StickChartViewHolder>(DefaultDiffUtil<StickChartUiData>()) {

    override fun onBindViewHolder(holder: StickChartViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StickChartViewHolder =
        StickChartViewHolder(
            ItemStickChartBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

}

class StickChartViewHolder(private val binding: ItemStickChartBinding): RecyclerView.ViewHolder(binding.root){

    fun bind(item: StickChartUiData){
        binding.item = item

        if(item.levelStringColor.isNotBlank()){
            binding.tvLevelName.setTextColor(item.levelStringColor.toColorInt())
        } else {
            item.levelHex?.let{
                if (it.isNotBlank()) {
                    binding.tvLevelName.setTextColor(it.toColorInt())
                }
            }
        }


        val percent = if(item.percent == 0f) 0.01f else item.percent
        val layoutParams: LayoutParams = binding.vStick.layoutParams as LayoutParams
        layoutParams.height = 0
        layoutParams.matchConstraintPercentHeight = percent
        binding.vStick.layoutParams = layoutParams
    }
}