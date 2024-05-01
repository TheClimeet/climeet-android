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
class SearchProfileAdapter() : RecyclerView.Adapter<SearchProfileAdapter.ViewHolder>() {

    private val followStatus = SparseBooleanArray()
    private var searchList: List<SearchProfileUiData> = emptyList()
    private var keyword: String = ""

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding: ItemSearchProfileBinding =
            ItemSearchProfileBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(searchList[position], keyword, position)
    }

    override fun getItemCount(): Int = searchList.size

    @SuppressLint("NotifyDataSetChanged")
    fun setList(list: List<SearchProfileUiData>, keyword: String) {
        searchList = list
        this.keyword = keyword
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ItemSearchProfileBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SearchProfileUiData, keyword: String, position: Int) {
            binding.keyword = keyword
            binding.item = item
            binding.tvCragsFollow.text = item.followers.toString()

            val btnFollowing = binding.btnFollowing
            val btnFollow = binding.btnFollow

            if (item.isFollowing) {
                binding.btnFollowing.visibility = View.VISIBLE
                binding.btnFollow.visibility = View.INVISIBLE
            } else {
                binding.btnFollowing.visibility = View.INVISIBLE
                binding.btnFollow.visibility = View.VISIBLE
            }

            binding.ivProfile.setOnClickListener {
                item.navigateToProfile(item.id)
            }

            btnFollowing.setOnClickListener {
                item.unFollow(item.id)
                if(item.followers > 0) {
                    item.followers -= 1 // 팔로워 수 감소
                }
                item.isFollowing = !item.isFollowing
                followStatus.put(position, false)
                binding.tvCragsFollow.text = item.followers.toString()
                notifyItemChanged(position)
            }

            btnFollow.setOnClickListener {
                item.follow(item.id)
                item.followers += 1 // 팔로워 수 증가
                item.isFollowing = !item.isFollowing
                followStatus.put(position, true) // 팔로우 상태 변경
                binding.tvCragsFollow.text = item.followers.toString()
                notifyItemChanged(position)
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

}

//class SearchProfileViewHolder(val binding: ItemSearchProfileBinding) :
//    RecyclerView.ViewHolder(binding.root) {
//    fun bind(item: SearchProfileUiData, keyword: String) {
//        binding.keyword = keyword
//        binding.item = item
//        binding.tvCragsFollow.text = item.followers.toString()
//
////        var existingFollowers = item.followers
////        val btnFollowing = binding.btnFollowing
////        val btnFollow = binding.btnFollow
////        val isFollow = item.isFollowing
////
////        if (isFollow) {
////            btnFollowing.visibility = View.VISIBLE
////            btnFollow.visibility = View.INVISIBLE
////        } else {
////            btnFollowing.visibility = View.INVISIBLE
////            btnFollow.visibility = View.VISIBLE
////        }
//
//        binding.ivProfile.setOnClickListener {
//            item.navigateToProfile(item.id)
//        }
//
////        btnFollowing.setOnClickListener {
////            item.follow(item.id)
////            binding.tvCragsFollow.text = (existingFollowers - 1).toString()
////            existingFollowers -= 1
////            btnFollowing.visibility = View.INVISIBLE
////            btnFollow.visibility = View.VISIBLE
////            item.isFollowing = false
////        }
////
////        btnFollow.setOnClickListener {
////            item.unFollow(item.id)
////            binding.tvCragsFollow.text = (existingFollowers + 1).toString()
////            existingFollowers += 1
////            btnFollowing.visibility = View.VISIBLE
////            btnFollow.visibility = View.INVISIBLE
////            item.isFollowing = true
////        }
//
//        if (item.imgUrl != null) {
//            Glide.with(binding.root.context)
//                .load(item.imgUrl)
//                .into(binding.ivProfile)
//        } else {
//            binding.ivProfile.setImageResource(R.drawable.oval_lightgreyfill_nostroke_noradius)
//        }
//        binding.tvCragName.text = item.name
//        binding.tvCragsFollow.text = item.followers.toString()
//        if (item.isFollowing) {
//            binding.btnFollowing.visibility = View.INVISIBLE
//            binding.btnFollow.visibility = View.VISIBLE
//        }
//    }
//}