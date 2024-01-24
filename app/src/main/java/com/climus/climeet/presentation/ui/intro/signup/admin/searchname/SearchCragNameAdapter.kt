package com.climus.climeet.presentation.ui.intro.signup.admin.searchname


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.climus.climeet.databinding.ItemCragSearchBinding
import com.climus.climeet.presentation.ui.intro.signup.admin.model.SearchCragUiData

class SearchCragNameAdapter() : RecyclerView.Adapter<SearchCragNameViewHolder>() {

    private var searchList: List<SearchCragUiData> = emptyList()
    private var keyword: String = ""

    override fun onBindViewHolder(holder: SearchCragNameViewHolder, position: Int) {
        holder.bind(searchList[position], keyword)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchCragNameViewHolder =
        SearchCragNameViewHolder(
            ItemCragSearchBinding.inflate(
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

class SearchCragNameViewHolder(private val binding: ItemCragSearchBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: SearchCragUiData, keyword: String) {
        binding.keyword = keyword
        binding.item = item

        binding.btnRegister.setOnClickListener {
            item.onClickListener(item.id, item.name, item.imgUrl)
        }
    }

}