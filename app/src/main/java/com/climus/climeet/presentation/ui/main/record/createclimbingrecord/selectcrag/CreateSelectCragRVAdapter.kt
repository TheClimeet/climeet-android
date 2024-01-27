package com.climus.climeet.presentation.ui.main.record.createclimbingrecord.selectcrag

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.climus.climeet.databinding.ItemCragSearchTimerBinding
import com.climus.climeet.presentation.ui.main.record.model.RecordCragData

class CreateSelectCragRVAdapter (
    private val onCragSelected: (RecordCragData) -> Unit
) : RecyclerView.Adapter<CreateSelectCragRVAdapter.CragSelectViewHolder>() {

    private var searchList: List<RecordCragData> = emptyList()
    private var keyword: String = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CragSelectViewHolder =
        CragSelectViewHolder(
            ItemCragSearchTimerBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun onBindViewHolder(holder: CragSelectViewHolder, position: Int) {
        val cragData = searchList[position]
        holder.bind(searchList[position], keyword)

        // 암장을 선택하는 버튼 클릭 시
        holder.binding.btnSelect.setOnClickListener {
            onCragSelected(cragData)
        }
    }

    override fun getItemCount(): Int = searchList.size

    @SuppressLint("NotifyDataSetChanged")
    fun setList(list: List<RecordCragData>, keyword: String) {
        searchList = list
        this.keyword = keyword
        notifyDataSetChanged()
    }

    class CragSelectViewHolder(val binding: ItemCragSearchTimerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: RecordCragData, keyword: String) {
            binding.keyword = keyword
            binding.data = data
        }
    }
}