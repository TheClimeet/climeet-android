package com.climus.climeet.presentation.ui.intro.signup.climer.complete

import android.content.Intent
import android.graphics.drawable.Animatable
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentSetClimerCompleteBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.climus.climeet.presentation.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SetClimerCompleteFragment : BaseFragment<FragmentSetClimerCompleteBinding>(R.layout.fragment_set_climer_complete) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, onBackPressedCallback)

        binding.ivCheck.setImageResource(R.drawable.ic_check_anim)
        val drawable = binding.ivCheck.drawable
        if (drawable is Animatable) {
            (drawable as Animatable).start()
        }

        binding.btnStartClimeet.setOnClickListener {
            val intent = Intent(requireContext(), MainActivity::class.java)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            findNavController().toLogin()
        }
    }

    private fun NavController.toLogin(){
        val action = SetClimerCompleteFragmentDirections.actionSetClimerCompleteFragmentToLoginFragment()
        navigate(action)
    }

}