package com.climus.climeet.presentation.ui.intro.signup.admin.service

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.climus.climeet.R
import com.climus.climeet.databinding.ItemServiceBinding
import com.climus.climeet.presentation.ui.intro.signup.admin.model.ServiceUiData

class ServiceRVAdapter(private val vm: SetAdminServiceViewModel, private val serviceList: List<ServiceUiData>) : RecyclerView.Adapter<ServiceRVAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemServiceBinding = ItemServiceBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = serviceList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(serviceList[position])
    }

    inner class ViewHolder(val binding: ItemServiceBinding) : RecyclerView.ViewHolder(binding.root) {

        // 아이템 연결
        init {
            binding.btnItem.setOnClickListener {
                vm.toggleServiceSelection(adapterPosition)
                Log.d("admin", "${serviceList[adapterPosition]} 눌림")
                notifyItemChanged(adapterPosition) // 선택 상태가 변경되면 해당 아이템만 갱신 (다른 아이템이 영향받지 않게)
            }
        }

        fun bind(service: ServiceUiData){
            binding.btnItem.text = service.title

            if(service.isSelected) {
                val drawable = ContextCompat.getDrawable(binding.root.context, R.drawable.rect_mainfill_nostroke_5radius)
                binding.btnItem.background = drawable
                binding.btnItem.setTextColor(Color.BLACK)
            } else {
                val drawable = ContextCompat.getDrawable(binding.root.context, R.drawable.rect_grey7fill_nostroke_5radius)
                binding.btnItem.background = drawable
                binding.btnItem.setTextColor(Color.WHITE)
            }
        }
    }
}

