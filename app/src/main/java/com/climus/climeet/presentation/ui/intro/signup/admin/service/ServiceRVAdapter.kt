package com.climus.climeet.presentation.ui.intro.signup.admin.service

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.climus.climeet.R
import com.climus.climeet.databinding.ItemCragSearchBinding
import com.climus.climeet.databinding.ItemServiceBinding
import com.climus.climeet.presentation.ui.intro.signup.admin.model.ServiceUiData
import com.climus.climeet.presentation.ui.intro.signup.admin.searchname.SearchCragNameViewHolder

// 클릭 이벤트를 처리하는 인터페이스 정의
interface OnServiceClickListener {
    fun onServiceClick(position: Int)
}

class ServiceRVAdapter(
    private val serviceList: List<ServiceUiData>,
    private val onServiceClickListener: OnServiceClickListener
) : RecyclerView.Adapter<ServiceRVAdapter.ServiceViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceViewHolder =
        ServiceViewHolder(
            ItemServiceBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun onBindViewHolder(holder: ServiceViewHolder, position: Int) {
        holder.bind(serviceList[position])

        holder.binding.btnItem.setOnClickListener {
            onServiceClickListener.onServiceClick(position)
        }
    }

    override fun getItemCount(): Int = serviceList.size

    class ServiceViewHolder(val binding: ItemServiceBinding) : RecyclerView.ViewHolder(binding.root) {
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

