package com.climus.climeet.presentation.ui.intro.signup.climer.Adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.climus.climeet.R
import com.climus.climeet.databinding.ItemWayBinding
import com.climus.climeet.presentation.ui.intro.signup.climer.howtoknow.HowToKnowViewModel
import com.climus.climeet.presentation.ui.intro.signup.climer.model.WayItem

class WayAdapter (private val viewModel: HowToKnowViewModel) :
    RecyclerView.Adapter<WayAdapter.WaysViewHolder>() {

    var ways: List<WayItem> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WayAdapter.WaysViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemWayBinding.inflate(inflater, parent, false)
        return WayAdapter.WaysViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WaysViewHolder, position: Int) {
        val levelItem = ways[position]
        holder.bind(levelItem, viewModel)
    }

    override fun getItemCount(): Int {
        return ways.size
    }
    class WaysViewHolder(private val binding: ItemWayBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(wayItem: WayItem, viewModel: HowToKnowViewModel) {
            binding.tvWay.text = wayItem.way
            binding.root.setOnClickListener {
                viewModel.selectLevel(adapterPosition)
            }

            if (wayItem.isSelected) {
                binding.root.setBackgroundResource(R.drawable.rect_mainfill_nostroke_8radius)
                binding.tvWay.setTextColor(Color.BLACK)
            } else {
                binding.root.setBackgroundResource(R.drawable.rect_grey7fill_nostroke_8radius)
                binding.tvWay.setTextColor(Color.WHITE)
            }
        }
    }
}