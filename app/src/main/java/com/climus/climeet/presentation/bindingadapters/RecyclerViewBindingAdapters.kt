package com.climus.climeet.presentation.bindingadapters

import android.annotation.SuppressLint
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.climus.climeet.presentation.ui.main.shorts.adapter.ShortsDetailAdapter
import com.climus.climeet.presentation.ui.main.shorts.model.ShortsUiData

@BindingAdapter("list")
fun <T, VH : RecyclerView.ViewHolder> bindList(recyclerView: RecyclerView, list: List<T>) {
    val adapter = recyclerView.adapter as ListAdapter<T, VH>
    adapter.submitList(list)
}

@SuppressLint("NotifyDataSetChanged")
@BindingAdapter("shortsList", "currentPosition")
fun bindShortsList(view: ViewPager2, list: List<ShortsUiData>, position: Int?){
    (view.adapter as ShortsDetailAdapter).apply{
        data = list
        notifyDataSetChanged()
    }
    position?.let{
        view.setCurrentItem(it, false)
    }
}