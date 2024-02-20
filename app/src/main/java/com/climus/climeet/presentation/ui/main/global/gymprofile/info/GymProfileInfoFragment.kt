package com.climus.climeet.presentation.ui.main.global.gymprofile.info

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentGymProfileInfoBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.climus.climeet.presentation.ui.main.global.gymprofile.GymProfileFragmentDirections
import com.climus.climeet.presentation.ui.main.global.gymprofile.GymProfileViewModel
import com.climus.climeet.presentation.ui.main.global.gymprofile.adapter.GymPriceAdapter
import com.climus.climeet.presentation.ui.main.global.gymprofile.adapter.GymReviewAdapter
import com.climus.climeet.presentation.ui.main.global.gymprofile.adapter.GymServiceAdapter
import com.climus.climeet.presentation.ui.main.global.gymprofile.adapter.GymTimeAdapter
import com.climus.climeet.presentation.ui.main.global.gymprofile.model.GymReview
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GymProfileInfoFragment :
    BaseFragment<FragmentGymProfileInfoBinding>(R.layout.fragment_gym_profile_info) {

    val parentViewModel: GymProfileViewModel by activityViewModels()
    val viewModel: GymProfileInfoViewModel by activityViewModels()

    private var timeAdapter: GymTimeAdapter? = null
    private var serviceAdapter: GymServiceAdapter? = null
    private var priceAdapter: GymPriceAdapter? = null
    private var reviewAdapter: GymReviewAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.parentVM = parentViewModel
        binding.vm = viewModel

        initStateObserve()
        initEventObserve()
        setAdapters()
        setGymInfo()
    }

    private fun setGymInfo() {
        // 암장 id, 이름 설정
        parentViewModel.gymId.observe(viewLifecycleOwner, Observer { id ->
            viewModel.setCragId(id)
            viewModel.getGymTabInfo()
        })
    }

    private fun setAdapters() {
        timeAdapter = GymTimeAdapter()
        serviceAdapter = GymServiceAdapter()
        priceAdapter = GymPriceAdapter()
        reviewAdapter = GymReviewAdapter()

        binding.rvTime.adapter = timeAdapter
        binding.rvPrice.adapter = priceAdapter
        binding.rvService.adapter = serviceAdapter
        binding.rvReview.adapter = reviewAdapter
    }

    private fun initStateObserve() {
        repeatOnStarted {
            viewModel.uiState.collect {

                val combinedReviewList = mutableListOf<GymReview>()

                // 내 리뷰가 있을 경우, 리스트의 첫 번째 요소로 추가
                it.myGymReview?.let {
                    combinedReviewList.add(
                        GymReview(
                            profileImageUrl = it.profileImageUrl,
                            profileName = it.profileName,
                            rating = it.rating,
                            content = it.content,
                            updatedAt = it.updatedAt
                        )
                    )
                }

                // 다른 사람들의 리뷰를 리스트에 추가
                it.gymReviewList?.forEach { review ->
                    combinedReviewList.add(
                        GymReview(
                            profileImageUrl = review.profileImageUrl,
                            profileName = review.profileName,
                            rating = review.rating,
                            content = review.content,
                            updatedAt = review.updatedAt
                        )
                    )
                }

                // 서비스
                if(it.gymServiceList?.isEmpty() == true) {
                    binding.rvService.visibility = View.GONE
                    binding.tvServiceError.visibility = View.VISIBLE
                } else {
                    binding.rvService.visibility = View.VISIBLE
                    binding.tvServiceError.visibility = View.GONE
                    serviceAdapter?.setList(it.gymServiceList!!)
                }

                // 영업 시간
                if(it.gymBusinessHours?.isEmpty() == true) {
                    binding.rvTime.visibility = View.GONE
                    binding.tvTimeError.visibility = View.VISIBLE
                } else {
                    binding.rvTime.visibility = View.VISIBLE
                    binding.tvTimeError.visibility = View.GONE
                    timeAdapter?.setList(it.gymBusinessHours!!)
                }

                // 요금 정보
                if(it.gymPriceList?.isEmpty() == true) {
                    binding.rvPrice.visibility = View.GONE
                    binding.tvPriceError.visibility = View.VISIBLE
                } else {
                    binding.rvPrice.visibility = View.VISIBLE
                    binding.tvPriceError.visibility = View.GONE
                    priceAdapter?.setList(it.gymPriceList!!)
                }

                // 리뷰
                if(it.reviewNum == 0){
                    // 리뷰가 0개일 때
                    binding.rvReview.visibility = View.GONE
                    binding.layoutEmptyReview.visibility = View.VISIBLE
                } else {
                    // 리뷰 있을 때
                    binding.rvReview.visibility = View.VISIBLE
                    binding.layoutEmptyReview.visibility = View.GONE
                    reviewAdapter?.setList(combinedReviewList)
                }

                if(it.myGymReview == null){
                    binding.btnCreateReview.text = "리뷰 남기기"
                } else {
                    binding.btnCreateReview.text = "리뷰 수정하기"
                }
            }
        }
    }

    private fun initEventObserve() {
        repeatOnStarted {
            viewModel.event.collect {
                when (it) {
                    is GymProfileInfoEvent.NavigateToGymReviewBottomSheetFragment -> findNavController().toGymReviewBottomSheetFragment()
                }
            }
        }
    }

    private fun NavController.toGymReviewBottomSheetFragment() {
        val action =
            GymProfileFragmentDirections.actionGymProfileFragmentToGymReviewBottomSheetFragment2()
        navigate(action)
    }
}