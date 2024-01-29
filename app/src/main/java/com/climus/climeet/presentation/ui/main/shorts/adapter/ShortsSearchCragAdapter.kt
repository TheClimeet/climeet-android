package com.climus.climeet.presentation.ui.main.shorts.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.climus.climeet.databinding.ItemShortsCragSearchBinding
import com.climus.climeet.presentation.ui.intro.signup.admin.model.SearchCragUiData

class ShortsSearchCragAdapter() : RecyclerView.Adapter<ShortsSearchCragViewHolder>() {

    private var searchList: List<SearchCragUiData> = emptyList()
    private var keyword: String = ""

    override fun onBindViewHolder(holder: ShortsSearchCragViewHolder, position: Int) {
        holder.bind(searchList[position], keyword)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShortsSearchCragViewHolder =
        ShortsSearchCragViewHolder(
            ItemShortsCragSearchBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )

    override fun getItemCount(): Int = searchList.size

    @SuppressLint("NotifyDataSetChanged")
    fun setList(list: List<SearchCragUiData>, keyword: String) {
        searchList = list
        this.keyword = keyword
        notifyDataSetChanged()
    }
}

class ShortsSearchCragViewHolder(private val binding: ItemShortsCragSearchBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: SearchCragUiData, keyword: String) {
        binding.keyword = keyword
        binding.item = item

        binding.btnRegister.setOnClickListener {
            item.onClickListener(item.id, item.name, item.imgUrl)
        }
    }

}