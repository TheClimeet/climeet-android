package com.climus.climeet.presentation.ui.main.mypage

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.climus.climeet.R
import com.climus.climeet.app.App
import com.climus.climeet.databinding.FragmentMypageBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.climus.climeet.presentation.ui.intro.IntroActivity
import com.climus.climeet.presentation.util.Constants
import com.climus.climeet.presentation.util.Constants.X_ACCESS_TOKEN
import com.climus.climeet.presentation.util.Constants.X_MODE
import com.climus.climeet.presentation.util.Constants.X_REFRESH_TOKEN

class MyPageFragment: BaseFragment<FragmentMypageBinding>(R.layout.fragment_mypage) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogout.setOnClickListener {
            App.sharedPreferences.edit()
                .clear()
                .apply()
            val intent = Intent(requireContext(),IntroActivity::class.java)
            startActivity(intent)
        }
    }


}