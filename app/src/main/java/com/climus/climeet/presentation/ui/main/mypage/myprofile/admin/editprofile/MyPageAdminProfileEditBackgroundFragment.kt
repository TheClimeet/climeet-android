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
import com.climus.climeet.databinding.FragmentMypageAdminProfileBackgroundEditBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.climus.climeet.presentation.ui.main.DataType
import com.climus.climeet.presentation.ui.main.MainViewModel
import com.climus.climeet.presentation.ui.main.mypage.CameraImageForm
import com.climus.climeet.presentation.ui.main.mypage.myprofile.admin.MyPageAdminMyProfileViewModel
import com.climus.climeet.presentation.ui.toMultiPart

class MyPageAdminProfileEditBackgroundFragment :
    BaseFragment<FragmentMypageAdminProfileBackgroundEditBinding>(R.layout.fragment_mypage_admin_profile_background_edit) {

    private val parentViewModel: MyPageAdminMyProfileViewModel by activityViewModels()
    private val mainViewModel: MainViewModel by activityViewModels()
    private val viewModel: MyPageAdminProfileEditBackgroundViewModel by viewModels()

    private val args: MyPageAdminProfileEditBackgroundFragmentArgs by navArgs()
    private val gymName by lazy { args.gymName }
    private val gymProfile by lazy { args.gymProfile }
    private val background by lazy { args.gymBackground }

    private var gymId: Long = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.svm = mainViewModel
        binding.vm = viewModel

        gymId = parentViewModel.getGymId()

        // 기존 배경 이미지 설정
        viewModel.initBackground(background)

        initEventObserve()
        initImageObserve()
        setOnClickListener()
    }

    private fun initEventObserve() {
        repeatOnStarted {
            viewModel.event.collect {
                when (it) {
                    AdminBackgroundEditEvent.NavigateToBack -> findNavController().toMyPageAdminProfile()
                    AdminBackgroundEditEvent.NavigateToNext -> findNavController().toEditProfileNext()
                }
            }
        }
    }

    // 배경 사진 설정됨
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
            .into(binding.ivBackground)

        if (mainViewModel.cameraImage.value) {
            // 촬영된 이미지
            val image = CameraImageForm.getImagePath()
            mainViewModel.fileToUrl(image, DataType.ADMIN_BACKGROUND_IMAGE)
        } else {
            // 갤러리 이미지
            uri.toMultiPart(requireContext())?.let { image ->
               mainViewModel.fileToUrl(image, DataType.ADMIN_BACKGROUND_IMAGE)
            } ?: run {
                showToastMessage("이미지 파일 변환 실패")
            }
        }
    }

    private fun setOnClickListener() {
        binding.ivBackground.setOnClickListener {
            context?.let { it1 -> mainViewModel.goToSetProfileImage(it1) }
        }
    }

    private fun NavController.toMyPageAdminProfile() {
        val action = MyPageAdminProfileEditBackgroundFragmentDirections.actionMyPageAdminProfileEditBackgroundFragmentToMyPageAdminMyProfileFragment(gymId)
        navigate(action)
    }

    private fun NavController.toEditProfileNext() {
        val action =
            MyPageAdminProfileEditBackgroundFragmentDirections.actionMyPageAdminProfileEditBackgroundFragmentToMyPageAdminProfileEditFragment(
                viewModel.imageUpdated.value,
                gymName,
                gymProfile,
                background
            )
        navigate(action)
    }
}