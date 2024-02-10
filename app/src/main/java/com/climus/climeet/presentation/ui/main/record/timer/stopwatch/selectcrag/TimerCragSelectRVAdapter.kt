package com.climus.climeet.presentation.ui.main.record.timer.stopwatch.selectcrag

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.climus.climeet.databinding.ItemCragSearchTimerBinding
import com.climus.climeet.presentation.ui.intro.signup.admin.model.SearchCragUiData

class TimerCragSelectRVAdapter(
    private val viewModel: TimerCragSelectBottomSheetViewModel
) : RecyclerView.Adapter<TimerCragSelectRVAdapter.CragSelectViewHolder>() {

    private var searchList: List<SearchCragUiData> = emptyList()
    private var keyword: String = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CragSelectViewHolder =
        CragSelectViewHolder(
            ItemCragSearchTimerBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun onBindViewHolder(holder: CragSelectViewHolder, position: Int) {
        val cragData = searchList[position]
        holder.bind(cragData, keyword)

        // 선택된 암장 정보 TimerFragment로 전달
        holder.binding.btnSelect.setOnClickListener {
            viewModel.selectItem(cragData)
            // 암장 정보 roomDB에 저장은 TimerFragment에서 진행함
        }
    }

    override fun getItemCount(): Int = searchList.size

    @SuppressLint("NotifyDataSetChanged")
    fun setList(list: List<SearchCragUiData>, keyword: String) {
        searchList = list
        this.keyword = keyword
        notifyDataSetChanged()
    }

    class CragSelectViewHolder(val binding: ItemCragSearchTimerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: SearchCragUiData, keyword: String) {
            binding.keyword = keyword
            binding.data = data

            binding.btnSelect.setOnClickListener {
                data.onClickListener(data.id, data.name, data.imgUrl)
            }
        }
    }
}
