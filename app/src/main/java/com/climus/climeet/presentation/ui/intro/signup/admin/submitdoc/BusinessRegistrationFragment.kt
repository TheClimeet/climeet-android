package com.climus.climeet.presentation.ui.intro.signup.admin.submitdoc

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentBusinessRegistrationBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.climus.climeet.presentation.ui.intro.IntroViewModel
import com.climus.climeet.presentation.ui.intro.signup.admin.AdminSignupForm
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BusinessRegistrationFragment :
    BaseFragment<FragmentBusinessRegistrationBinding>(R.layout.fragment_business_registration) {

    private val parentViewModel: IntroViewModel by activityViewModels()
    private val viewModel: BusinessRegistrationViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = viewModel
        binding.pvm = parentViewModel
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

    private fun initEventObserve(){
        repeatOnStarted {
            viewModel.event.collect{
                when(it){
                    is BusinessRegistrationEvent.NavigateToNext -> {}
                    is BusinessRegistrationEvent.NavigateToBack -> {}
                }
            }
        }
    }

    private fun initParentImageObserve(){
        repeatOnStarted {
            parentViewModel.imageUri.collect{
                binding.btnGotoGallery.setImageURI(it)
                AdminSignupForm.setBusinessRegistrationUri(it)
            }
        }
    }
}