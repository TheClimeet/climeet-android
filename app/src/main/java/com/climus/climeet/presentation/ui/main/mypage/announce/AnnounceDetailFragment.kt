package com.climus.climeet.presentation.ui.main.mypage.announce

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentAnnounceDetailBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.climus.climeet.presentation.ui.main.mypage.announce.adapter.AnnounceImageRVAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AnnounceDetailFragment :
    BaseFragment<FragmentAnnounceDetailBinding>(R.layout.fragment_announce_detail) {

    private lateinit var itemAdapter: AnnounceImageRVAdapter
    private val viewModel: AnnounceDetailViewModel by viewModels()

    private val args: AnnounceDetailFragmentArgs by navArgs()
    private val boardId by lazy { args.boardId }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = viewModel
        viewModel.getAnnouncement(boardId)

        initEventObserve()
    }

    private fun initEventObserve() {
        repeatOnStarted {
            viewModel.event.collect {
                when (it) {
                    is AnnouncementDetailEvent.SetRVAdapter -> initRVAdapter(it.imageList)
                    is AnnouncementDetailEvent.ShowToastMessage -> showToastMessage(it.msg)
                }
            }
        }
    }

    private fun initRVAdapter(images: List<String>) {
        itemAdapter = AnnounceImageRVAdapter(images)
        binding.rvImage.adapter = itemAdapter
    }
}