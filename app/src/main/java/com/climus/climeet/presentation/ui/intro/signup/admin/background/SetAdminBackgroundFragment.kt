package com.climus.climeet.presentation.ui.intro.signup.admin.background

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentSetAdminBackgroundBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.climus.climeet.presentation.ui.intro.IntroViewModel
import com.climus.climeet.presentation.ui.intro.UrlType

class SetAdminBackgroundFragment :
    BaseFragment<FragmentSetAdminBackgroundBinding>(R.layout.fragment_set_admin_background) {

    private val parentViewModel: IntroViewModel by activityViewModels()
    private val viewModel: SetAdminBackgroundViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        parentViewModel.adminSignUpProgress(5)
        binding.vm = viewModel

        binding.ivImgIcon.setOnClickListener {
            parentViewModel.goToGallery(UrlType.ADMIN_BACKGROUND)
        }

        initEventObserve()
        initParentImageObserve()
    }

    private fun initEventObserve() {
        repeatOnStarted {
            viewModel.event.collect {
                when (it) {
                    is SetAdminBackgroundEvent.NavigateToBack -> findNavController().navigateUp()  // 개인정보 설정으로 이동
                    is SetAdminBackgroundEvent.NavigateToNext -> navigateNext()
                }
            }
        }
    }

    private fun initParentImageObserve() {
        repeatOnStarted {
            parentViewModel.imageUri.collect {
                setImage(it)
            }
        }
    }

    private fun setImage(uri: Uri) {
        Glide.with(this)
            .load(uri)
            .placeholder(R.drawable.ic_add_img) // 기본 이미지
            .into(binding.ivImgIcon)
    }

    // 서비스 설정으로 이동
    private fun navigateNext() {
        val action =
            SetAdminBackgroundFragmentDirections.actionSetAdminBackgroundFragmentToSetAdminServiceFragment()
        findNavController().navigate(action)
    }
}