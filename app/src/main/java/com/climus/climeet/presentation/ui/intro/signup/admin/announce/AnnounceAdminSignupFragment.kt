package com.climus.climeet.presentation.ui.intro.signup.admin.announce

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentAnnounceAdminSignupBinding
import com.climus.climeet.presentation.base.BaseFragment

class AnnounceAdminSignupFragment: BaseFragment<FragmentAnnounceAdminSignupBinding>(R.layout.fragment_announce_admin_signup) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setDescriptionText()
        setBtnListener()
    }

    private fun setDescriptionText(){
        val spannable = SpannableString("암장 관리자 회원가입을 하시려면 다음 절차가 필요해요\n꼼꼼하게 확인하여 모두가 즐거운 클밋을 만들어갈게요!")
            .apply{
                setSpan(
                    ForegroundColorSpan(requireContext().getColor(R.color.cm_main)),
                    18,
                    23,
                    Spannable.SPAN_EXCLUSIVE_INCLUSIVE
                )
            }
        binding.tvDescription.text = spannable
    }

    private fun setBtnListener(){
        binding.btnNext.setOnClickListener {
            findNavController().toSearchCragName()
        }

        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun NavController.toSearchCragName(){
        val action = AnnounceAdminSignupFragmentDirections.actionAnnounceAdminSignupFragmentToSearchCragNameFragment()
        navigate(action)
    }

}