package com.climus.climeet.presentation.ui.intro.signup.admin

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentSetCragNameBinding
import com.climus.climeet.presentation.base.BaseFragment


class SetCragNameFragment: BaseFragment<FragmentSetCragNameBinding>(R.layout.fragment_set_crag_name) {

    private val viewModel: SetCragNameViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 데이터 바인딩 변수에 뷰모델 연결
        binding.vm = viewModel

        initEventObserve()
    }

    private fun initEventObserve() {
        repeatOnStarted {
            viewModel.event.collect {
                when (it) {
                    is SetCragNameEvent.NavigateToBack -> findNavController().navigateUp()  // 배경화면 설정으로 이동
                    is SetCragNameEvent.NavigateToNext -> navigateNext()
                }
            }
        }
    }

    // 알림 설정으로 이동
    private fun navigateNext(){
        val action = SetCragNameFragmentDirections.actionSetCragNameFragmentToSetAdminIdPwFragment2()
        findNavController().navigate(action)
    }
}