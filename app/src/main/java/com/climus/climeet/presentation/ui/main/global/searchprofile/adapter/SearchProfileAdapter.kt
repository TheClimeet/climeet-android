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
import com.climus.climeet.presentation.ui.intro.signup.climer.model.FollowCrag
import com.climus.climeet.presentation.ui.main.global.searchprofile.model.SearchProfileUiData

@Suppress("DEPRECATION")
class SearchProfileAdapter() : RecyclerView.Adapter<SearchProfileViewHolder>() {

    private var searchList: List<SearchProfileUiData> = emptyList()
    private var keyword: String = ""
    private val followStatus = SparseBooleanArray()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SearchProfileViewHolder {
        val binding: ItemSearchProfileBinding =
            ItemSearchProfileBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchProfileViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchProfileViewHolder, position: Int) {
        val item = searchList[position]
        holder.bind(searchList[position], keyword, position)

        holder.binding.btnFollowing.setOnClickListener {
            item.unFollow(item.id)
            if(item.followers > 0) {
                item.followers -= 1 // 팔로워 수 감소
            }
            item.isFollowing = !item.isFollowing
            followStatus.put(position, false)
            holder.binding.tvCragsFollow.text = item.followers.toString()
            notifyItemChanged(position)
        }

        holder.binding.btnFollow.setOnClickListener {
            item.follow(item.id)
            item.followers += 1 // 팔로워 수 증가
            item.isFollowing = !item.isFollowing
            followStatus.put(position, true) // 팔로우 상태 변경
            holder.binding.tvCragsFollow.text = item.followers.toString()
            notifyItemChanged(position)
        }
    }

    override fun getItemCount(): Int = searchList.size

    @SuppressLint("NotifyDataSetChanged")
    fun setList(list: List<SearchProfileUiData>, keyword: String) {
        searchList = list
        this.keyword = keyword
        notifyDataSetChanged()
    }

}

class SearchProfileViewHolder(val binding: ItemSearchProfileBinding): RecyclerView.ViewHolder(binding.root) {

    fun bind(item: SearchProfileUiData, keyword: String, position: Int) {
        binding.keyword = keyword
        binding.item = item
        binding.tvCragsFollow.text = item.followers.toString()

        binding.ivProfile.setOnClickListener {
            item.navigateToProfile(item.id)
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
    }
}
