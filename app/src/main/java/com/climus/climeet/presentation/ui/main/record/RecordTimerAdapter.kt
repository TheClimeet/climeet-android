package com.climus.climeet.presentation.ui.main.record

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.climus.climeet.databinding.ItemRecordBinding
import com.climus.climeet.databinding.ItemTimerBinding

class RecordTimerAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var indicatorVisibilityListener: (() -> Unit)? = null

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is RecordTimerViewHolder) {
            holder.bind()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 0) {
            val binding = ItemTimerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            RecordTimerViewHolder(binding, indicatorVisibilityListener)
        } else {
            val binding = ItemRecordBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            RecordViewHolder(binding)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int = 2
}

// 스톱워치 화면
class RecordTimerViewHolder(private val binding: ItemTimerBinding,
                            private val indicatorVisibilityListener: (() -> Unit)?)
    : RecyclerView.ViewHolder(binding.root) {
    fun bind() {
        binding.ivStart.setOnClickListener {
            indicatorVisibilityListener?.invoke()
        }
    }
}

// 루트 기록 화면
class RecordViewHolder(private val binding: ItemRecordBinding) : RecyclerView.ViewHolder(binding.root)
