package com.climus.climeet.presentation.ui.main.global.gymprofile.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.climus.climeet.databinding.ItemProfileReviewBinding
import com.climus.climeet.presentation.ui.main.global.gymprofile.model.GymReview
import java.text.SimpleDateFormat
import java.util.Locale

class GymReviewAdapter() : RecyclerView.Adapter<GymReviewViewHolder>() {

    private var reviewList: List<GymReview> = emptyList()

    override fun onBindViewHolder(holder: GymReviewViewHolder, position: Int) {
        holder.bind(reviewList[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GymReviewViewHolder =
        GymReviewViewHolder(
            ItemProfileReviewBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )

    override fun getItemCount(): Int = reviewList.size

    @SuppressLint("NotifyDataSetChanged")
    fun setList(list: List<GymReview>) {
        reviewList = list
        notifyDataSetChanged()
    }
}

class GymReviewViewHolder(private val binding: ItemProfileReviewBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(item: GymReview) {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.getDefault())
        val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        val date = inputFormat.parse(item.updatedAt)
        val formattedDate = outputFormat.format(date)

        // 년, 월, 일로만 보이게 수정
        binding.tvDate.text = formattedDate
        binding.tvName.text = item.profileName
        binding.tvReview.text = item.content
        binding.starRate.rating = item.rating
        Glide.with(binding.root)
            .load(item.profileImageUrl)
            .into(binding.ivProfile)
    }
}
