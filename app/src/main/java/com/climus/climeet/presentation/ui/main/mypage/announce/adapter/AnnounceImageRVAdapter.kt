package com.climus.climeet.presentation.ui.main.mypage.announce.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.climus.climeet.databinding.ItemAnnouncementImageBinding

class AnnounceImageRVAdapter(private val items: List<String>) :
    RecyclerView.Adapter<ImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemAnnouncementImageBinding.inflate(inflater, parent, false)
        return ImageViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val uri = items[position]
        Glide.with(holder.itemView.context)
            .load(uri)
            .into(holder.binding.ivImage)
    }
}

class ImageViewHolder(val binding: ItemAnnouncementImageBinding) :
    RecyclerView.ViewHolder(binding.root)