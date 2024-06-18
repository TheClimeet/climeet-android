package com.climus.climeet.presentation.ui.main.mypage.review

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentMyPageReviewBinding
import com.climus.climeet.databinding.FragmentMypagePolicyBinding
import com.climus.climeet.presentation.base.BaseFragment

class MyPageReviewFragment : BaseFragment<FragmentMyPageReviewBinding>(R.layout.fragment_my_page_review) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initClickListener()
    }

    private fun initClickListener(){
        // todo : 구글 플레이스토어 연결
        binding.btnReview.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/games?hl=ko-KR"))
            startActivity(intent)
        }
        // todo : 토스 익명 계좌 연결
        binding.btnSupport.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://toss.im/"))
            startActivity(intent)
        }
    }
}