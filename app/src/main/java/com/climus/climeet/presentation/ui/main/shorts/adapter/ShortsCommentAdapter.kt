package com.climus.climeet.presentation.ui.main.shorts.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.climus.climeet.databinding.ItemMainCommentBinding
import com.climus.climeet.databinding.ItemSubCommentBinding
import com.climus.climeet.presentation.ui.main.shorts.model.ShortsCommentUiData
import com.climus.climeet.presentation.util.DefaultDiffUtil

class ShortsCommentAdapter :
    ListAdapter<ShortsCommentUiData, RecyclerView.ViewHolder>(diffCallback) {

    companion object {
        const val MAIN_COMMENT = 0
        const val SUB_COMMENT = 1

        val diffCallback = object : DiffUtil.ItemCallback<ShortsCommentUiData>(){
            override fun areItemsTheSame(oldItem: ShortsCommentUiData, newItem: ShortsCommentUiData): Boolean {
                return oldItem.commentId == newItem.commentId
            }

            override fun areContentsTheSame(oldItem: ShortsCommentUiData, newItem: ShortsCommentUiData): Boolean {
                return oldItem == newItem
            }
        }
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
        with(binding) {
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

        if (item.isLastSubComment) {
            binding.btnShowMoreComment.visibility = View.VISIBLE
            binding.btnShowMoreComment.text = "답글 ${item.remainSubCommentCount}개 더보기"
        } else {
            binding.btnShowMoreComment.visibility = View.GONE
        }

        with(binding) {
            tvThumbsDownCount.text = item.dislikeCount.toString()
            tvThumbsUpCount.text = item.likeCount.toString()

            btnThumbsDown.setOnClickListener {

            }

            btnThumbsUp.setOnClickListener {

            }

            btnShowMoreComment.setOnClickListener {
                item.showMoreComment(item.parentCommentId, absoluteAdapterPosition, item.remainSubCommentCount, item.subCommentPage)
            }
        }
    }
}