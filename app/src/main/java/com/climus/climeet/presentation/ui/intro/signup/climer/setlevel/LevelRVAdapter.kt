package com.climus.climeet.presentation.ui.intro.signup.climer.setlevel

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.climus.climeet.R
import com.climus.climeet.databinding.ItemLevelBinding
import com.climus.climeet.presentation.ui.intro.signup.climer.model.LevelItem

class LevelRVAdapter(private val viewModel: SetClimerLevelViewModel) :
    RecyclerView.Adapter<LevelRVAdapter.LevelViewHolder>() {

    var levels: List<LevelItem> = listOf()

    override fun onBindViewHolder(holder: LevelViewHolder, position: Int) {
        val levelItem = levels[position]
        holder.bind(levelItem, viewModel)
    }

    class LevelViewHolder(private val binding: ItemLevelBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(levelItem: LevelItem, viewModel: SetClimerLevelViewModel) {
            binding.tvTitle.text = levelItem.title
            binding.tvDescription.text = levelItem.description
            binding.root.setOnClickListener {
                viewModel.selectLevel(adapterPosition)
            }

            if (levelItem.isSelected) {
                binding.root.setBackgroundResource(R.drawable.rect_mainfill_nostroke_8radius)
                binding.tvTitle.setTextColor(Color.BLACK)
                binding.tvDescription.setTextColor(Color.BLACK)
            } else {
                binding.root.setBackgroundResource(R.drawable.rect_grey7fill_nostroke_8radius)
                binding.tvTitle.setTextColor(Color.WHITE)
                binding.tvDescription.setTextColor(Color.WHITE)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LevelViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemLevelBinding.inflate(inflater, parent, false)
        return LevelViewHolder(binding)
    }
    override fun getItemCount(): Int {
        return levels.size
    }

}