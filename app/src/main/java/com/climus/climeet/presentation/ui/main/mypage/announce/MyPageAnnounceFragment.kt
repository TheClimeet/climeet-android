package com.climus.climeet.presentation.ui.main.mypage.announce

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentMypageAnnounceBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.climus.climeet.presentation.ui.main.mypage.announce.adapter.AnnouncementRVAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyPageAnnounceFragment: BaseFragment<FragmentMypageAnnounceBinding>(R.layout.fragment_mypage_announce) {

    lateinit var itemAdapter: AnnouncementRVAdapter
    private val viewModel: MyPageAnnounceViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        itemAdapter = AnnouncementRVAdapter(viewModel)

        observeUiState()
        initEventObserve()
        setRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAnnouncement()
    }

    private fun observeUiState() {
        repeatOnStarted {
            viewModel.uiState.collect { state ->
                itemAdapter.items = state.announceList
            }
        }
    }

    private fun initEventObserve() {
        repeatOnStarted {
            viewModel.event.collect {
                when (it) {
                    is MyPageAnnounceEvent.NavigateToAnnounceDetail -> navigateToAnnounceDetail(it.boardId)
                    is MyPageAnnounceEvent.ShowToastMessage -> showToastMessage(it.msg)
                }
            }
        }
    }

    private fun setRecyclerView() {
        binding.rvMypageAnnouncement.adapter = itemAdapter
        binding.rvMypageAnnouncement.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
    }

    private fun navigateToAnnounceDetail(boardId: Long) {
        val action = MyPageAnnounceFragmentDirections.actionMyPageAnnounceFragmentToAnnounceDetailFragment(boardId)
        findNavController().navigate(action)
    }

}