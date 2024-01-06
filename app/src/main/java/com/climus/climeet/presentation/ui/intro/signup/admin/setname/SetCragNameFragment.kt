package com.climus.climeet.presentation.ui.intro.signup.admin.setname

import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentSetCragNameBinding
import com.climus.climeet.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SetCragNameFragment :
    BaseFragment<FragmentSetCragNameBinding>(R.layout.fragment_set_crag_name) {

    private val viewModel: SetCragNameViewModel by viewModels()
    private val args: SetCragNameFragmentArgs by navArgs()
    private val cragId by lazy { args.cragId }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = viewModel
        viewModel.setCragId(cragId)
        setWarningText()
        initEventObserve()
    }

    private val clickableSpan = object : ClickableSpan() {
        override fun updateDrawState(tp: TextPaint) {
            tp.color = requireContext().getColor(R.color.cm_red)
            tp.typeface = Typeface.DEFAULT_BOLD
            tp.isUnderlineText = false
        }

        override fun onClick(widget: View) {
            findNavController().toSetCragNameError()
        }
    }

    private fun setWarningText() {
        val text = getString(R.string.set_crag_name_warning_text)
        val spannable = SpannableString(text)
            .apply {
                setSpan(
                    clickableSpan,
                    23,
                    25,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }

        binding.tvWarning.text = spannable
        binding.tvWarning.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun initEventObserve() {
        repeatOnStarted {
            viewModel.event.collect {
                when (it) {
                    is SetCragNameEvent.NavigateToBusinessRegistration -> findNavController().toBusinessRegistration()
                    is SetCragNameEvent.NavigateToBack -> findNavController().navigateUp()
                }
            }
        }
    }

    private fun NavController.toBusinessRegistration() {
        val action = SetCragNameFragmentDirections.actionSetCragNameFragmentToBusinessRegistrationFragment()
        navigate(action)
    }

    private fun NavController.toSetCragNameError() {
        val action = SetCragNameFragmentDirections.actionSetCragNameFragmentToSetCragErrorFragment()
        navigate(action)
    }

}