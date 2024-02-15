package com.climus.climeet.presentation.ui.main.record.calendar

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.climus.climeet.databinding.ItemRecordBinding
import com.climus.climeet.presentation.ui.main.record.model.ClimbingRecordData

class CalendarAdapter() : RecyclerView.Adapter<CalendarViewHolder>() {

    private var climbingRecordList: List<ClimbingRecordData> = emptyList()

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        holder.bind(climbingRecordList[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder =
        CalendarViewHolder(
            ItemRecordBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )

    override fun getItemCount(): Int = climbingRecordList.size

    @SuppressLint("NotifyDataSetChanged")
    fun setList(list: List<ClimbingRecordData>){
        climbingRecordList = list
        notifyDataSetChanged()
    }

}

class CalendarViewHolder(private val binding: ItemRecordBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: ClimbingRecordData) {
        binding.item = item
    }
}