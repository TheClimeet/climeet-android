package com.climus.climeet.presentation.ui.main.record.createclimbingrecord.selecttime.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.climus.climeet.databinding.ItemDateBinding

class AMPMAdapter() :
    RecyclerView.Adapter<AMPMAdapter.AMPMViewHolder>() {

    var ampmList: List<String> = listOf("", "", "", "", "오전", "오후", "", "", "", "")
    var ontimeItemClickListener: OnTimeItemClickListener? = null
    var curPosition = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AMPMViewHolder =
        AMPMViewHolder(
            ItemDateBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun onBindViewHolder(holder: AMPMViewHolder, position: Int) {
        curPosition = position
        val realPosition = position % ampmList.size
        holder.bind(ampmList[realPosition])
    }

    override fun getItemCount(): Int = ampmList.size

    fun getPosition(): Int {
        return curPosition
    }

    inner class AMPMViewHolder(val binding: ItemDateBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                ontimeItemClickListener?.onTimeItemClick(adapterPosition)
            }
        }

        fun bind(ampm: String) {
            binding.tvPickDate.text = ampm
        }
    }
}