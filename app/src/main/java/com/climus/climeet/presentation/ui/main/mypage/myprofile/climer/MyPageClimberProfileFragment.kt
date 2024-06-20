package com.climus.climeet.presentation.ui.main.mypage.myprofile.climer

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentMypageClimberProfileBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.climus.climeet.presentation.ui.main.global.climerprofile.ClimberProfileViewModel
import com.climus.climeet.presentation.ui.main.mypage.myprofile.climer.viewpager.MyPageClimberProfileVPAdapter
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyPageClimberProfileFragment :
    BaseFragment<FragmentMypageClimberProfileBinding>(R.layout.fragment_mypage_climber_profile) {

    private val sharedViewModel: ClimberProfileViewModel by activityViewModels()
    private val viewModel: MyPageClimberProfileViewModel by activityViewModels()

    private val args: MyPageClimberProfileFragmentArgs by navArgs()
    private val userId by lazy { args.userId }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.svm = sharedViewModel
        binding.vm = viewModel

        Log.d("mypage_climber", "현재 유저 id : $userId")

        sharedViewModel.setUserId(userId)
        viewModel.setClimberId(userId)

        setupTabLayout()
        initEventObserve()
        initStateObserve()
    }

    override fun onResume() {
        super.onResume()
        // 상단 정보 설정
        sharedViewModel.setUserId(userId)
    }

    private fun setupTabLayout() {
        val myPageClimberProfileAdapter = MyPageClimberProfileVPAdapter(this, userId)
        binding.vpClimberProfile.adapter = myPageClimberProfileAdapter

        val tabMenu = arrayListOf(" 숏츠 ", " 정보 ")
        TabLayoutMediator(binding.tbClimberProfile, binding.vpClimberProfile) { tab, position ->
            tab.text = tabMenu[position]
        }.attach()
    }

    private fun initEventObserve(){
        repeatOnStarted {
            viewModel.event.collect{
                when(it){
                    is MyPageClimberProfileEvent.NavigateToEditClimberProfile -> findNavController().toEditPage()
                }
            }
        }
    }

    private fun initStateObserve() {
        repeatOnStarted {
            viewModel.showSnackbar.collect { state ->
                if (state) {
                    showCustomSnackBar(binding.snackGuide, "프로필 수정이 완료되었어요!") // 원하는 메세지로 수정
                    viewModel.setSnackBarState(false)
                }
            }
        }
    }

    private fun NavController.toEditPage(){
        val name = sharedViewModel.uiState.value.userName
        val image = sharedViewModel.uiState.value.userProfileImg
        val action = MyPageClimberProfileFragmentDirections.actionMyPageClimberProfileFragmentToMyPageClimberProfileEditFragment(name, image)
        navigate(action)
    }
}