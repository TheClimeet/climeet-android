package com.climus.climeet.presentation.ui.main.mypage.myshorts.viewpager

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentMypageMyshortsCommentBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.climus.climeet.presentation.ui.main.mypage.myshorts.adapter.MyPageShortsCommentRVAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyPageMyShortsCommentFragment :
    BaseFragment<FragmentMypageMyshortsCommentBinding>(R.layout.fragment_mypage_myshorts_comment) {

    private val viewModel: MyPageMyShortsCommentViewModel by viewModels()

    private lateinit var adapter: MyPageShortsCommentRVAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = MyPageShortsCommentRVAdapter(viewModel)
        binding.rvComments.adapter = adapter
        binding.vm = viewModel

        initRecyclerView()
        initEventObserve()
        initStateObserve()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getComment(0)
    }

    private fun initRecyclerView() {
        binding.rvComments.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!recyclerView.canScrollVertically(1)) {
                    viewModel.getComment(viewModel.currentPage + 1)
                }
            }
        })
    }

    private fun initEventObserve() {
        repeatOnStarted {
            viewModel.event.collect {
                when (it) {
                    is MyPageMyShortsCommentEvent.NavigateToShortsComment -> navigateToShorts()
                    is MyPageMyShortsCommentEvent.ShowToastMessage -> showToastMessage(it.msg)
                }
            }
        }
    }

    private fun initStateObserve() {
        repeatOnStarted {
            viewModel.uiState.collect { state ->
                adapter.items = state.commentList

                if (state.commentList.isEmpty()) {
                    binding.layoutNoItem.visibility = View.VISIBLE
                } else {
                    binding.layoutNoItem.visibility = View.INVISIBLE
                }
            }
        }
    }

    private fun navigateToShorts() {
        // todo : 해당 숏츠의 댓글창 열기
    }
}