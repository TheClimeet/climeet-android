package com.climus.climeet.presentation.ui.intro.onboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.climus.climeet.databinding.ItemOnboardBinding

class OnBoardAdapter(val data: List<OnBoardItem>) : RecyclerView.Adapter<OnBoardViewHolder>() {

    override fun onBindViewHolder(holder: OnBoardViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnBoardViewHolder =
        OnBoardViewHolder(
            ItemOnboardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun getItemCount(): Int = data.size
}

class OnBoardViewHolder(private val binding: ItemOnboardBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: OnBoardItem) {
        binding.tvDescription.text = item.description
        binding.tvHeader.text = item.title
        binding.ivImg.setImageResource(item.img)
    }
}

data class OnBoardItem(
    val title: String,
    val description: String,
    val img: Int
)