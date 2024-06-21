package com.climus.climeet.presentation.ui.main.mypage.myprofile.admin.editprofile

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentMypageAdminProfileEditBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.climus.climeet.presentation.ui.main.MainViewModel
import com.climus.climeet.presentation.ui.main.mypage.myprofile.admin.MyPageAdminMyProfileViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyPageAdminProfileEditFragment :
    BaseFragment<FragmentMypageAdminProfileEditBinding>(R.layout.fragment_mypage_admin_profile_edit) {

    private val mainViewModel: MainViewModel by activityViewModels()
    private val parentViewModel: MyPageAdminMyProfileViewModel by activityViewModels()
    private val viewModel: MyPageAdminProfileEditViewModel by viewModels()

    private var gymId: Long = 0

    private val args: MyPageAdminProfileEditFragmentArgs by navArgs()
    private val gymName by lazy { args.gymName }
    private val gymProfile by lazy { args.gymProfile }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.svm = mainViewModel
        binding.vm = viewModel

        gymId = parentViewModel.getGymId()

        initEventObserve()
    }

    private fun initEventObserve() {
        repeatOnStarted {
            viewModel.event.collect {
                when (it) {
                    AdminProfileEditEvent.NavigateToBack -> {
                        parentViewModel.setSnackBarState(true)
                        findNavController().toMyPageAdminProfile()
                    }
                    AdminProfileEditEvent.NavigateToProfile -> findNavController().toMyPageAdminProfile()
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
            .circleCrop()
            .into(binding.ivProfile)
    }

    private fun NavController.toMyPageAdminProfile() {
        val action = MyPageAdminProfileEditFragmentDirections.actionMyPageAdminProfileEditFragmentToMyPageAdminMyProfileFragment(gymId)
        navigate(action)
    }
}