package com.climus.climeet.presentation.ui.main.mypage.policy

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentMypagePolicyBinding
import com.climus.climeet.presentation.base.BaseFragment

class MyPagePolicyFragment: BaseFragment<FragmentMypagePolicyBinding>(R.layout.fragment_mypage_policy) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initClickListener()
    }

    private fun initClickListener() {
        binding.layoutLink.setOnClickListener{
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://climbers.notion.site/Meet-884013799ca64e4283142f1729c26a7f?pvs=74"))
            startActivity(intent)
        }
    }
}