package com.climus.climeet.presentation.ui.main.record.createclimbingrecord.selectcrag

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.climus.climeet.databinding.ItemCragSearchTimerBinding
import com.climus.climeet.presentation.ui.intro.signup.climer.model.FollowCrag
import com.climus.climeet.presentation.ui.main.record.model.RecordCragData

class CreateSelectCragRVAdapter () : RecyclerView.Adapter<CreateSelectCragRVAdapter.CragSelectViewHolder>() {

    private var searchList: List<FollowCrag> = emptyList()
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

//        // 암장을 선택하는 버튼 클릭 시
//        holder.binding.btnSelect.setOnClickListener {
//            onCragSelected(cragData)
//        }
    }

    override fun getItemCount(): Int = searchList.size

    @SuppressLint("NotifyDataSetChanged")
    fun setList(list: List<FollowCrag>, keyword: String) {
        searchList = list
        this.keyword = keyword
        notifyDataSetChanged()
    }

    class CragSelectViewHolder(val binding: ItemCragSearchTimerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: FollowCrag, keyword: String) {
            binding.keyword = keyword
            binding.crag = data
        }
    }
}