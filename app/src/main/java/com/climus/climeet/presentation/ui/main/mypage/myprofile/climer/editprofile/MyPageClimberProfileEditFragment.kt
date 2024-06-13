package com.climus.climeet.presentation.ui.main.mypage.myprofile.climer.editprofile

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentMypageClimberProfileEditBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.climus.climeet.presentation.ui.main.MainViewModel
import com.climus.climeet.presentation.ui.main.mypage.myprofile.climer.MyPageClimberProfileViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MyPageClimberProfileEditFragment : BaseFragment<FragmentMypageClimberProfileEditBinding>(R.layout.fragment_mypage_climber_profile_edit) {

    private val mainViewModel : MainViewModel by activityViewModels()
    private val idViewModel: MyPageClimberProfileViewModel by activityViewModels()
    private val viewModel: MyPageClimberProfileEditViewModel by viewModels()

    private var climberId: Long = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.svm = mainViewModel
        binding.vm = viewModel

        climberId = idViewModel.getClimberId()

        initEventObserve()
        initImageObserve()
        setOnClickListener()
    }

    private fun initEventObserve() {
        repeatOnStarted {
            viewModel.event.collect {
                when (it) {
                    is EditClimberProfileEvent.NavigateToProfile -> findNavController().toMyPageClimberProfile()
                    is EditClimberProfileEvent.NavigateToBack -> findNavController().navigateUp()
                    is EditClimberProfileEvent.ShowToastMessage -> showToastMessage(it.msg)
                }
            }
        }
    }

    private fun initImageObserve() {
        repeatOnStarted {
            mainViewModel.imageUri.collect {
                setImage(it)
            }
        }
    }


    private fun setImage(uri: Uri) {
        Glide.with(this)
            .load(uri)
            .circleCrop() // 기본 이미지
            .into(binding.ivProfile)
    }

    fun setImageState(boolean: Boolean){
        viewModel.setImageUpdated(boolean)
    }

    private fun setOnClickListener(){
        binding.ivProfile.setOnClickListener {
            context?.let { it1 -> mainViewModel.goToSetProfileImage(it1) }
        }
    }

    private fun NavController.toMyPageClimberProfile() {
        val action = MyPageClimberProfileEditFragmentDirections.actionMyPageClimberProfileEditFragmetnToMyPageClimberProfileFragment(climberId)
        navigate(action)
    }

}