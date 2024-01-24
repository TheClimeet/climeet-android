package com.climus.climeet.presentation.ui.intro.signup.admin.submitdoc

import android.net.Uri
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentBusinessRegistrationBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.climus.climeet.presentation.ui.intro.IntroViewModel
import com.climus.climeet.presentation.ui.intro.UrlType
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BusinessRegistrationFragment :
    BaseFragment<FragmentBusinessRegistrationBinding>(R.layout.fragment_business_registration) {

    private val parentViewModel: IntroViewModel by activityViewModels()
    private val viewModel: BusinessRegistrationViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        parentViewModel.adminSignUpProgress(2)
        binding.vm = viewModel
        binding.btnGotoGallery.setOnClickListener {
            parentViewModel.goToGallery(UrlType.ADMIN_BUSINESS_REGISTRATION)
        }
        binding.btnNext.isEnabled = false
        setDescriptionText()
        initEventObserve()
        initParentImageObserve()
    }

    private fun setDescriptionText() {
        val spannable = SpannableString("정확한 암장 확인을 위해 필요한 과정이에요.\n미디어 권한을 꼭 허용해주세요.")
            .apply {
                setSpan(
                    ForegroundColorSpan(requireContext().getColor(R.color.cm_main)),
                    35,
                    37,
                    Spannable.SPAN_EXCLUSIVE_INCLUSIVE
                )
            }
        binding.tvDescription.text = spannable
    }

    private fun initEventObserve() {
        repeatOnStarted {
            viewModel.event.collect {
                when (it) {
                    is BusinessRegistrationEvent.NavigateToSetAdminIdPw -> findNavController().toSetAdminIdPw()
                    is BusinessRegistrationEvent.NavigateToBack -> {}
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
        binding.btnGotoGallery.setImageURI(uri)
        binding.btnNext.isEnabled = true
        binding.btnNext.setImageResource(R.drawable.ic_next_on)
    }

    private fun NavController.toSetAdminIdPw() {
        val action =
            BusinessRegistrationFragmentDirections.actionBusinessRegistrationFragmentToSetAdminIdPwFragment()
        navigate(action)
    }
}