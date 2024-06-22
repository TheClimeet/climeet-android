package com.climus.climeet.presentation.ui.main.mypage.myshorts.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.climus.climeet.databinding.ItemMypageShortsCommentBinding
import com.climus.climeet.presentation.ui.main.mypage.myshorts.model.MyPageShortsCommentUiData
import com.climus.climeet.presentation.ui.main.mypage.myshorts.viewpager.MyPageMyShortsCommentViewModel
import java.text.SimpleDateFormat
import java.util.Locale

class MyPageShortsCommentRVAdapter(private val viewModel : MyPageMyShortsCommentViewModel) :
    RecyclerView.Adapter<ShortsCommentViewHolder>() {

    var items: List<MyPageShortsCommentUiData> = emptyList()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShortsCommentViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemMypageShortsCommentBinding.inflate(inflater, parent, false)
        return ShortsCommentViewHolder(binding, viewModel)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ShortsCommentViewHolder, position: Int) {
        holder.bind(items[position])
    }
}

class ShortsCommentViewHolder(
    private val binding: ItemMypageShortsCommentBinding,
    private val viewModel : MyPageMyShortsCommentViewModel
) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(item: MyPageShortsCommentUiData) {

        // 해당 댓글로 이동
        binding.root.setOnClickListener {
            // todo: 뷰모델 -> 해당 숏츠 댓글로 이동

        }

        if (item.profileImage != null) {
            Glide.with(binding.root.context)
                .load(item.profileImage)
                .into(binding.cvImage)
        }

        binding.tvComment.text = item.contents
        binding.tvDate.text = formatDate(item.createdAt)
    }

    private fun formatDate(isoDateString: String): String {
        val isoFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val date = isoFormatter.parse(isoDateString)

        val targetFormat = SimpleDateFormat("yyyy.MM.dd HH:mm", Locale.getDefault())
        return targetFormat.format(date)
    }

}


