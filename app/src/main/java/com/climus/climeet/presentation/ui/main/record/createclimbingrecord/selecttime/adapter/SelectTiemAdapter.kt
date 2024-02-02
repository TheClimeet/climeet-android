package com.climus.climeet.presentation.ui.main.record.createclimbingrecord.selecttime.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.climus.climeet.databinding.ItemDateBinding

interface OnTimeItemClickListener {
    fun onTimeItemClick(position: Int)
}

class SelectTimeAdapter(private val list: List<Int>) :
    RecyclerView.Adapter<SelectTimeAdapter.SelectTimeViewHolder>() {

    var timeList: List<Int> = list
    var ontimeItemClickListener: OnTimeItemClickListener? = null
    var curPosition = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectTimeViewHolder =
        SelectTimeViewHolder(
            ItemDateBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun getItemCount(): Int = Int.MAX_VALUE

    override fun onBindViewHolder(holder: SelectTimeViewHolder, position: Int) {
        curPosition = position
        val realPosition = position % list.size
        holder.bind(timeList[realPosition])
    }

    fun updateList(newList: List<Int>) {
        timeList = newList
        notifyDataSetChanged()
    }

    fun getPosition(): Int {
        return curPosition
    }

    inner class SelectTimeViewHolder(val binding: ItemDateBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                ontimeItemClickListener?.onTimeItemClick(adapterPosition)
            }
        }

        fun bind(time: Int) {
            binding.tvPickDate.text = time.toString()
        }
    }
}