package com.climus.climeet.presentation.ui.main.mypage.announce.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.climus.climeet.databinding.ItemAnnouncementBinding
import com.climus.climeet.presentation.ui.main.mypage.announce.MyPageAnnounceViewModel
import com.climus.climeet.presentation.ui.main.mypage.announce.model.AnnouncementUiData

class AnnouncementRVAdapter(private val viewModel : MyPageAnnounceViewModel) :
    RecyclerView.Adapter<AnnouncementViewHolder>() {

    var items: List<AnnouncementUiData> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnnouncementViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemAnnouncementBinding.inflate(inflater, parent, false)
        return AnnouncementViewHolder(binding, viewModel)
    }

    override fun onBindViewHolder(holder: AnnouncementViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}

class AnnouncementViewHolder(
    private val binding: ItemAnnouncementBinding,
    private val viewModel : MyPageAnnounceViewModel
) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(item: AnnouncementUiData) {

        // 해당 공지로 이동
        binding.root.setOnClickListener {
            viewModel.navigateToAnnounceDetail(item.boardId)
        }

        if (item.profileImageUrl != null) {
            Glide.with(binding.root.context)
                .load(item.profileImageUrl)
                .into(binding.ivAnnounceProfile)
        }

        if (item.image != null) {
            Glide.with(binding.root.context)
                .load(item.image)
                .into(binding.ivImage)
        }

        binding.tvCreatedDate.text = item.createdAt
        binding.tvAnnouncementTitle.text = item.title
        binding.tvAnnouncementContents.text = item.content
        binding.tvAnnouncementLikeCount.text = item.likeCount.toString()
    }

}


