package com.climus.climeet.presentation.ui.main.home.recycler.popularcrag

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.climus.climeet.data.model.response.BestFollowGymSimpleResponse
import com.climus.climeet.data.model.response.BestRecordGymDetailInfoResponse
import com.climus.climeet.databinding.ItemPopularCragsBinding
import kotlin.math.min

class RecordOrderPopularCragRVAdapter (private val cragList: List<BestRecordGymDetailInfoResponse>,
    private val onClickListener: (Long) -> Unit) : RecyclerView.Adapter<RecordOrderPopularCragRVAdapter.ViewHolder>(){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecordOrderPopularCragRVAdapter.ViewHolder {
        val binding: ItemPopularCragsBinding = ItemPopularCragsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecordOrderPopularCragRVAdapter.ViewHolder, position: Int) {
        holder.bind(cragList[position])
    }

    override fun getItemCount(): Int {
        // 최대 10개의 아이템으로 제한
        return min(cragList.size, 10)
    }

    inner class ViewHolder(val binding: ItemPopularCragsBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(crag: BestRecordGymDetailInfoResponse) {

            binding.root.setOnClickListener {
                onClickListener(crag.gymId)
            }
            if(crag.profileImageUrl != null) {
                Glide.with(binding.root.context)
                    .load(crag.profileImageUrl)
                    .into(binding.ivPopularCragProfile)
            }
            binding.tvItemCragName.text = crag.gymName
            binding.popularCragRanking.text = crag.ranking.toString()
            binding.tvItemFollowers.text = "팔로워 " + crag.thisWeekSelectionCount.toString()
//            if(crag.isSoaring) {
//                binding.ivSoaring.visibility = View.VISIBLE
//                binding.tvSoaring.visibility = View.VISIBLE
//            }
        }
    }
}