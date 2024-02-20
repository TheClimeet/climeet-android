package com.climus.climeet.presentation.ui.main.global.searchprofile.adapter

import android.annotation.SuppressLint
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.climus.climeet.R
import com.climus.climeet.databinding.ItemFollowCragsBinding
import com.climus.climeet.databinding.ItemSearchProfileBinding
import com.climus.climeet.presentation.ui.intro.signup.climer.ClimerSignupForm
import com.climus.climeet.presentation.ui.main.global.searchprofile.model.SearchProfileUiData

class SearchProfileAdapter() : RecyclerView.Adapter<SearchProfileViewHolder>() {

    private var searchList: List<SearchProfileUiData> = emptyList()
    private var keyword: String = ""

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SearchProfileViewHolder {
        val binding: ItemSearchProfileBinding =
            ItemSearchProfileBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchProfileViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchProfileViewHolder, position: Int) {
        holder.bind(searchList[position], keyword)
    }

    override fun getItemCount(): Int = searchList.size

    @SuppressLint("NotifyDataSetChanged")
    fun setList(list: List<SearchProfileUiData>, keyword: String) {
        searchList = list
        this.keyword = keyword
        notifyDataSetChanged()
    }
}

class SearchProfileViewHolder(val binding: ItemSearchProfileBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(item: SearchProfileUiData, keyword: String) {
        binding.keyword = keyword
        binding.item = item

        binding.tvCragsFollow.text = item.followers.toString()


        val btnFollowing = binding.btnFollowing
        val btnFollow = binding.btnFollow
        val isFollow = item.isFollowing

        if (isFollow) {
            btnFollowing.visibility = View.INVISIBLE
            btnFollow.visibility = View.VISIBLE
        } else {
            btnFollowing.visibility = View.VISIBLE
            btnFollow.visibility = View.INVISIBLE
        }

        binding.ivProfile.setOnClickListener {
            item.navigateToProfile(item.id)
        }

        // 팔로우 +1
        btnFollowing.setOnClickListener {
            item.follow(item.id)
            binding.tvCragsFollow.text = (item.followers + 1).toString()
            btnFollowing.visibility = View.INVISIBLE
            btnFollow.visibility = View.VISIBLE
        }

        // 팔로우 -1
        btnFollow.setOnClickListener {
            item.unFollow(item.id)
            binding.tvCragsFollow.text = (item.followers - 1).toString()
            btnFollowing.visibility = View.VISIBLE
            btnFollow.visibility = View.INVISIBLE
        }

        if (item.imgUrl != null) {
            Glide.with(binding.root.context)
                .load(item.imgUrl)
                .into(binding.ivProfile)
        } else {
            binding.ivProfile.setImageResource(R.drawable.oval_lightgreyfill_nostroke_noradius)
        }
        binding.tvCragName.text = item.name
        binding.tvCragsFollow.text = item.followers.toString()
        if (item.isFollowing) {
            binding.btnFollowing.visibility = View.INVISIBLE
            binding.btnFollow.visibility = View.VISIBLE
        }
    }
}