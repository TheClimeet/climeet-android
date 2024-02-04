package com.climus.climeet.presentation.ui.main.record.createclimbingrecord.selectcrag

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.climus.climeet.databinding.ItemCragSearchCalendarBinding
import com.climus.climeet.presentation.ui.intro.signup.climer.model.FollowCrag
import com.climus.climeet.presentation.ui.main.record.createclimbingrecord.CreateClimbingRecordViewModel
import com.climus.climeet.presentation.ui.main.record.createclimbingrecord.CreateRecordData

class CreateSelectCragRVAdapter(
    private val viewModel: CreateSelectCragViewModel,
    private val parentViewModel: CreateClimbingRecordViewModel
) : RecyclerView.Adapter<CreateSelectCragRVAdapter.CragSelectViewHolder>() {

    private var searchList: List<FollowCrag> = emptyList()
    private var keyword: String = ""


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CragSelectViewHolder =
        CragSelectViewHolder(
            ItemCragSearchCalendarBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun onBindViewHolder(holder: CragSelectViewHolder, position: Int) {
        val cragData = searchList[position]
        holder.bind(searchList[position], keyword)

        // 암장을 선택하는 버튼 클릭 시
        holder.binding.btnSelect.setOnClickListener {
            CreateRecordData.setSelecetedCrag(cragData)
            viewModel.exitSignal.value = true
            parentViewModel.selectCrag(cragData.id, cragData.name)
            parentViewModel.isSelectedCrag.value = true
        }
    }

    override fun getItemCount(): Int = searchList.size

    @SuppressLint("NotifyDataSetChanged")
    fun setList(list: List<FollowCrag>, keyword: String) {
        searchList = list
        this.keyword = keyword
        notifyDataSetChanged()
    }

    class CragSelectViewHolder(val binding: ItemCragSearchCalendarBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: FollowCrag, keyword: String) {
            binding.keyword = keyword
            binding.crag = data
        }
    }
}