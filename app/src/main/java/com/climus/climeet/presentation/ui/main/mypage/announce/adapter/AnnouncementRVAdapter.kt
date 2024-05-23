package com.climus.climeet.presentation.ui.main.mypage.announce.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.climus.climeet.data.model.response.GetAnnouncementResponse
import com.climus.climeet.databinding.ItemAnnouncementBinding

@Suppress("DEPRECATION")
class AnnouncementRVAdapter(private val announcementList: List<GetAnnouncementResponse>) :
    RecyclerView.Adapter<AnnouncementViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AnnouncementViewHolder {
        val binding: ItemAnnouncementBinding =
            ItemAnnouncementBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AnnouncementViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AnnouncementViewHolder, position: Int) {
        holder.bind(announcementList[position])
        holder.itemView.setOnClickListener {
            itemClickListener.onItemClick(announcementList[position])
        }
    }

    override fun getItemCount(): Int = announcementList.size

    interface OnItemClickListener {
        fun onItemClick(item: GetAnnouncementResponse)
    }

    private lateinit var itemClickListener: OnItemClickListener

    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }

}

class AnnouncementViewHolder(private val binding: ItemAnnouncementBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(item: GetAnnouncementResponse) {

        if (item.profileImageUrl != null) {
            Glide.with(binding.root.context)
                .load(item.profileImageUrl)
                .into(binding.ivAnnounceProfile)
        }

        if (item.image != null) {
            Glide.with(binding.root.context)
                .load(item.image)
                .into(binding.imageFilterView)
        }

        binding.tvCreatedDate.text = item.createdAt
        binding.tvAnnouncementTitle.text = item.title
        binding.tvAnnouncementSummary.text = item.content
        binding.tvAnnouncementContents.text = item.content
        binding.tvAnnouncementLikeCount.text = item.likeCount.toString()

    }

}


