package com.climus.climeet.presentation.ui.main.mypage.sendopinion

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentMypageSendopinionBinding
import com.climus.climeet.presentation.base.BaseFragment

class MyPageSendOpinionFragment: BaseFragment<FragmentMypageSendopinionBinding>(R.layout.fragment_mypage_sendopinion) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initClickListener()
    }

    private fun initClickListener() {
        binding.ibSend.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("http://pf.kakao.com/_xiCxgPG/chat"))
            startActivity(intent)
        }
    }
}