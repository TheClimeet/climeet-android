package com.climus.climeet.presentation.ui.main.mypage.myprofile.admin.info

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentMypageAdminProfileInfoBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.climus.climeet.presentation.ui.main.global.gymprofile.adapter.GymPriceAdapter
import com.climus.climeet.presentation.ui.main.global.gymprofile.adapter.GymReviewAdapter
import com.climus.climeet.presentation.ui.main.global.gymprofile.adapter.GymServiceAdapter
import com.climus.climeet.presentation.ui.main.global.gymprofile.adapter.GymTimeAdapter
import com.climus.climeet.presentation.ui.main.global.gymprofile.info.GymProfileInfoViewModel
import com.climus.climeet.presentation.ui.main.global.gymprofile.model.GymReview
import com.climus.climeet.presentation.ui.main.mypage.myprofile.admin.MyPageAdminMyProfileViewModel

class MyPageAdminProfileInfoFragment(val gymId: Long) :
    BaseFragment<FragmentMypageAdminProfileInfoBinding>(R.layout.fragment_mypage_admin_profile_info) {

    val sharedViewModel: GymProfileInfoViewModel by activityViewModels()
    val viewModel: MyPageAdminMyProfileViewModel by activityViewModels()

    private var timeAdapter: GymTimeAdapter? = null
    private var serviceAdapter: GymServiceAdapter? = null
    private var priceAdapter: GymPriceAdapter? = null
    private var reviewAdapter: GymReviewAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.svm = sharedViewModel
        binding.vm = viewModel

        initStateObserve()
        setAdapters()

        sharedViewModel.setCragId(gymId)
        sharedViewModel.getGymTabInfo()
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
            sharedViewModel.uiState.collect {

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
                if (it.gymServiceList?.isEmpty() == true) {
                    binding.rvService.visibility = View.GONE
                    binding.tvServiceError.visibility = View.VISIBLE
                } else {
                    binding.rvService.visibility = View.VISIBLE
                    binding.tvServiceError.visibility = View.GONE
                    serviceAdapter?.setList(it.gymServiceList!!)
                }

                // 영업 시간
                if (it.gymBusinessHours?.isNotEmpty() == true) {
                    timeAdapter?.setList(it.gymBusinessHours!!)
                }

                // 요금 정보
                if (it.gymPriceList?.isEmpty() == true) {
                    binding.rvPrice.visibility = View.GONE
                    binding.tvPriceError.visibility = View.VISIBLE
                } else {
                    binding.rvPrice.visibility = View.VISIBLE
                    binding.tvPriceError.visibility = View.GONE
                    priceAdapter?.setList(it.gymPriceList!!)
                }

                // 리뷰
                if (it.reviewNum == 0) {
                    // 리뷰가 0개일 때
                    binding.rvReview.visibility = View.GONE
                    binding.layoutEmptyReview.visibility = View.VISIBLE
                } else {
                    // 리뷰 있을 때
                    binding.rvReview.visibility = View.VISIBLE
                    binding.layoutEmptyReview.visibility = View.GONE
                    reviewAdapter?.setList(combinedReviewList)
                }

                if (it.myGymReview == null) {
                    binding.btnCreateReview.text = "리뷰 남기기"
                } else {
                    binding.btnCreateReview.text = "리뷰 수정하기"
                }
            }
        }
    }
}
