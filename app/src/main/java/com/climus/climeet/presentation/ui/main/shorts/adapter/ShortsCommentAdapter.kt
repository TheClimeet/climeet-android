package com.climus.climeet.presentation.ui.main.shorts.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.climus.climeet.databinding.ItemMainCommentBinding
import com.climus.climeet.databinding.ItemSubCommentBinding
import com.climus.climeet.presentation.ui.main.shorts.model.ShortsCommentUiData
import com.climus.climeet.presentation.util.DefaultDiffUtil

class ShortsCommentAdapter :
    ListAdapter<ShortsCommentUiData, RecyclerView.ViewHolder>(DefaultDiffUtil<ShortsCommentUiData>()) {

    companion object {
        const val MAIN_COMMENT = 0
        const val SUB_COMMENT = 1
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ShortsMainCommentViewHolder -> holder.bind(getItem(position))
            is ShortsSubCommentViewHolder -> holder.bind(getItem(position))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            MAIN_COMMENT -> {
                ShortsMainCommentViewHolder(
                    ItemMainCommentBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }

            SUB_COMMENT -> {
                ShortsSubCommentViewHolder(
                    ItemSubCommentBinding.inflate(
                        LayoutInflater.from(
                            parent.context
                        ), parent, false
                    )
                )
            }

            else -> {
                ShortsSubCommentViewHolder(
                    ItemSubCommentBinding.inflate(
                        LayoutInflater.from(
                            parent.context
                        ), parent, false
                    )
                )
            }
        }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        return item.type
    }
}

class ShortsMainCommentViewHolder(private val binding: ItemMainCommentBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: ShortsCommentUiData) {
        binding.item = item
        with(binding){
            tvThumbsDownCount.text = item.dislikeCount.toString()
            tvThumbsUpCount.text = item.likeCount.toString()
            btnThumbsDown.setOnClickListener {

            }

            btnThumbsUp.setOnClickListener {

            }
        }
    }
}

class ShortsSubCommentViewHolder(private val binding: ItemSubCommentBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: ShortsCommentUiData) {
        binding.item = item
        with(binding){
            tvThumbsDownCount.text = item.dislikeCount.toString()
            tvThumbsUpCount.text = item.likeCount.toString()

            btnThumbsDown.setOnClickListener {

            }

            btnThumbsUp.setOnClickListener {

            }

            btnShowMoreComment.setOnClickListener {

            }
        }
    }
}