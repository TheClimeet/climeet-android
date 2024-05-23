package com.climus.climeet.presentation.ui.main.mypage.announce

import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.climus.climeet.R
import com.climus.climeet.data.model.response.GetAnnouncementResponse
import com.climus.climeet.databinding.FragmentMypageAnnounceBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.climus.climeet.presentation.ui.main.home.HomeFragmentDirections
import com.climus.climeet.presentation.ui.main.mypage.announce.adapter.AnnouncementRVAdapter
import com.climus.climeet.presentation.ui.main.mypage.follow.viewpager.FollowVPAdapter
import com.google.android.material.tabs.TabLayoutMediator

class MyPageAnnounceFragment: BaseFragment<FragmentMypageAnnounceBinding>(R.layout.fragment_mypage_announce) {

    private var dummyDatas = ArrayList<GetAnnouncementResponse>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setDummyList()

    }

    private fun setDummyList() {
        dummyDatas.apply {
            add(GetAnnouncementResponse(1, "2024.03.15", 3, "안녕하세요", "드디어 클밋이 출시되었습니다.", null, null))
            add(GetAnnouncementResponse(2, "2024.04.03", 4, "안녕하세요", "드디어 클밋이 출시되었습니다.", null, null))
            add(GetAnnouncementResponse(3, "2024.04.12", 2, "안녕하세요", "드디어 클밋이 출시되었습니다.", null, null))
            add(GetAnnouncementResponse(4, "2024.05.08", 8, "안녕하세요", "드디어 클밋이 출시되었습니다.", null, null))
        }

        val announcementRVAdapter = AnnouncementRVAdapter(dummyDatas)
        binding.rvMypageAnnouncement.adapter = announcementRVAdapter
        binding.rvMypageAnnouncement.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        announcementRVAdapter.setItemClickListener(object : AnnouncementRVAdapter.OnItemClickListener {
            override fun onItemClick(item: GetAnnouncementResponse) {
                navigateToAnnounceDetail(item.boardId)
            }

        })
    }

    private fun navigateToAnnounceDetail(boardId: Long) {
        val action = MyPageAnnounceFragmentDirections.actionMyPageAnnounceFragmentToAnnounceDetailFragment(boardId)
        findNavController().navigate(action)
    }

}