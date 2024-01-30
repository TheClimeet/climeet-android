package com.climus.climeet.presentation.ui.main.record.createclimbingrecord.selectdate.adpater

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.climus.climeet.databinding.ItemDateBinding

interface OnDateItemClickListener {
    fun onDateItemClick(position: Int)
}

class SelectDateAdapter(private val list: List<String>) :
    RecyclerView.Adapter<SelectDateAdapter.SelectDateYearViewHolder>() {

    private var dateList: List<String> = list
    var onDateItemClickListener: OnDateItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectDateYearViewHolder =
        SelectDateYearViewHolder(
            ItemDateBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun getItemCount(): Int = dateList.size

    override fun onBindViewHolder(holder: SelectDateYearViewHolder, position: Int) {
        holder.bind(dateList[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(list: List<String>) {
        dateList = list
        notifyDataSetChanged()
    }

    inner class SelectDateYearViewHolder(val binding: ItemDateBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                onDateItemClickListener?.onDateItemClick(adapterPosition)
            }
        }
        fun bind(date: String) {
            binding.tvPickDate.text = date
        }
    }
}