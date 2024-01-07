package com.climus.climeet.presentation.ui.intro.signup.climer.followcrag

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.recyclerview.widget.LinearLayoutManager
import com.climus.climeet.R
import com.climus.climeet.databinding.ActivityFollowCragsBinding
import com.climus.climeet.presentation.base.BaseActivity
import com.climus.climeet.presentation.ui.intro.signup.climer.climbinggoal.ClimbingGoalActivity
import com.climus.climeet.presentation.ui.intro.signup.climer.model.Crag

class FollowCragsActivity : BaseActivity<ActivityFollowCragsBinding>(ActivityFollowCragsBinding::inflate) {

    private var cragList = ArrayList<Crag>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.ivFollowCragsBack.setOnClickListener {

        }

        val sortOptions = listOf("가나다순","팔로우순")

        binding.spinner.adapter = CustomSpinnerAdaptor(this, R.layout.item_spinner_sort_option, sortOptions)
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val value = binding.spinner.getItemAtPosition(p2).toString()
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
                // 선택되지 않은 경우
            }
        }

        cragList.apply {
            add(Crag(null, "더클라임 클라이밍 강남점", 70, false))
            add(Crag(null, "더클라임 클라이밍 논현점", 30, false))
            add(Crag(null, "더클라임 클라이밍 마곡점", 120, false))
            add(Crag(null, "더클라임 클라이밍 홍대점", 12, false))
            add(Crag(null, "더클라임 클라이밍 강남점", 70, false))
            add(Crag(null, "더클라임 클라이밍 논현점", 30, false))
            add(Crag(null, "더클라임 클라이밍 마곡점", 120, false))
            add(Crag(null, "더클라임 클라이밍 홍대점", 12, false))
            add(Crag(null, "더클라임 클라이밍 강남점", 70, false))
            add(Crag(null, "더클라임 클라이밍 논현점", 30, false))
            add(Crag(null, "더클라임 클라이밍 마곡점", 120, false))
            add(Crag(null, "더클라임 클라이밍 홍대점", 12, false))
        }

        val followCragRVAdapter = FollowCragRVAdapter(cragList)
        binding.rvFollowCrags.adapter = followCragRVAdapter
        binding.rvFollowCrags.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        binding.ivSearchCrags.setOnClickListener {
            binding.rvFollowCrags.visibility = View.VISIBLE
        }

        followCragRVAdapter.setItemClickListener(
            object : FollowCragRVAdapter.OnItemClickListener {
            override fun onItemClick(crag : Crag) {
                crag.isFollowing = !crag.isFollowing
            }
        })

        binding.btnNextValid.setOnClickListener {
            val intent = Intent(this, ClimbingGoalActivity::class.java)
            startActivity(intent)
        }
    }
}