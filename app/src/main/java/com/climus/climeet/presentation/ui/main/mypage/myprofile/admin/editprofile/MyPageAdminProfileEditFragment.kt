package com.climus.climeet.presentation.ui.main.mypage.myprofile.admin.editprofile

import android.net.Uri
import android.os.Bundle
import android.util.Log
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
import com.climus.climeet.presentation.ui.main.DataType
import com.climus.climeet.presentation.ui.main.MainViewModel
import com.climus.climeet.presentation.ui.main.mypage.CameraImageForm
import com.climus.climeet.presentation.ui.main.mypage.myprofile.admin.MyPageAdminMyProfileViewModel
import com.climus.climeet.presentation.ui.toMultiPart
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyPageAdminProfileEditFragment :
    BaseFragment<FragmentMypageAdminProfileEditBinding>(R.layout.fragment_mypage_admin_profile_edit) {

    private val mainViewModel: MainViewModel by activityViewModels()
    private val parentViewModel: MyPageAdminMyProfileViewModel by activityViewModels()
    private val viewModel: MyPageAdminProfileEditViewModel by viewModels()

    private var gymId: Long = 0

    private val args: MyPageAdminProfileEditFragmentArgs by navArgs()
    private val backgroundState by lazy { args.backgroundState }
    private val gymName by lazy { args.gymName }
    private val gymProfile by lazy { args.gymProfile }
    private val background by lazy { args.gymBackground }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.svm = mainViewModel
        binding.vm = viewModel

        Log.d("tqlkf", "${backgroundState}")
        viewModel.initState(backgroundState, gymName, gymProfile)

        gymId = parentViewModel.getGymId()

        initEventObserve()
        initImageObserve()
        setOnClickListener()
    }

    private fun initEventObserve() {
        repeatOnStarted {
            viewModel.event.collect {
                when (it) {
                    AdminProfileEditEvent.NavigateToBack -> findNavController().toMyPageAdminEditBackground()
                    AdminProfileEditEvent.NavigateToProfile -> {
                        parentViewModel.setSnackBarState(true)
                        findNavController().toMyPageAdminProfile()
                    }
                    is AdminProfileEditEvent.ShowToastMessage -> showToastMessage(it.msg)
                }
            }
        }
    }

    // 프로필 사진 변경
    private fun initImageObserve() {
        repeatOnStarted {
            mainViewModel.imageUri.collect {
                setImage(it)
            }
        }
    }

    private fun setOnClickListener() {
        binding.ivProfile.setOnClickListener {
            context?.let { it1 -> mainViewModel.goToSetProfileImage(it1) }
        }
    }

    private fun setImage(uri: Uri) {
        Glide.with(this)
            .load(uri)
            .circleCrop()
            .into(binding.ivProfile)

        if (mainViewModel.cameraImage.value) {
            val image = CameraImageForm.getImagePath()
            mainViewModel.fileToUrl(image, DataType.ADMIN_PROFILE_IMAGE)
        } else {
            // 갤러리 이미지
            uri.toMultiPart(requireContext())?.let { image ->
                mainViewModel.fileToUrl(image, DataType.ADMIN_PROFILE_IMAGE)
            } ?: run {
                showToastMessage("이미지 파일 변환 실패")
            }
        }
    }

    private fun NavController.toMyPageAdminEditBackground(){
        val action =
            MyPageAdminProfileEditFragmentDirections.actionMyPageAdminProfileEditFragmentToMyPageAdminProfileEditBackgroundFragment(gymName, gymProfile, background)
        navigate(action)
    }

    private fun NavController.toMyPageAdminProfile() {
        val action =
            MyPageAdminProfileEditFragmentDirections.actionMyPageAdminProfileEditFragmentToMyPageAdminMyProfileFragment(
                gymId
            )
        navigate(action)
    }
}
