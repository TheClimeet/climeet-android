package com.climus.climeet.presentation.ui.main.record.timer.setrecord

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.climus.climeet.databinding.ItemTimerAvgCompleteBinding
import com.climus.climeet.presentation.ui.main.record.model.LevelItemForAvg
import kotlin.math.roundToInt

// ------------------------- 평균 완등률 드롭 다운 -------------------------------
class RecordAverageAdapter(
    private val viewModel: SetTimerClimbingRecordViewModel
) : RecyclerView.Adapter<RecordAverageAdapter.RecordAvgViewHolder>() {

    private var items: List<LevelItemForAvg> = emptyList()

    // 레벨별 완등 기록 TimerFragment에서 넘겨받아 저장
    fun setItems(items: List<LevelItemForAvg>) {
        this.items = items
        notifyDataSetChanged()
    }

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordAvgViewHolder {
        val binding =
            ItemTimerAvgCompleteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecordAvgViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecordAvgViewHolder, position: Int) {
        holder.bind(items[position])
    }

    class RecordAvgViewHolder(val binding: ItemTimerAvgCompleteBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: LevelItemForAvg) {
            val successRate =
                if (item.attemptCount > 0) item.successCount / item.attemptCount.toFloat() else 0f

            // 레벨 이름과 색상 설정
            binding.levelName = item.levelName
            binding.levelColor = item.levelColor

            // 각 레벨의 평균 완등률 progress 설정
            binding.pbAvgComplete.progress = (successRate * 100).roundToInt()
            binding.tvAvgComplete.text = "${item.successCount}/${item.attemptCount}"
        }
    }
}