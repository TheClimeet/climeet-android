package com.climus.climeet.presentation.ui.intro.signup.admin.alarm

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentSetAdminAlarmBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.climus.climeet.presentation.ui.intro.signup.admin.AdminSignupForm

class SetAdminAlarmFragment: BaseFragment<FragmentSetAdminAlarmBinding>(R.layout.fragment_set_admin_alarm) {

    private val viewModel: SetAdminAlarmViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 데이터 바인딩 변수에 뷰모델 연결
        binding.vm = viewModel

        // AdminSignupForm의 지점명을 가져와 붙여줄 예정 (임시로 이름을 넣어둠)
        binding.tvExplainLarge.text = AdminSignupForm.name + " " + binding.tvExplainLarge.text

        initEventObserve()
    }

    private fun initEventObserve() {
        repeatOnStarted {
            viewModel.event.collect {
                when (it) {
                    is SetAdminAlarmEvent.NavigateToBack -> findNavController().navigateUp()  // 서비스 설정으로 이동
                    is SetAdminAlarmEvent.NavigateToNextAlarmOn -> navigateNext()
                    is SetAdminAlarmEvent.NavigateToNextAlarmOff -> navigateNext()
                }
            }
        }
    }

    // 완료 페이지로 이동
    private fun navigateNext(){
        val action = SetAdminAlarmFragmentDirections.actionSetAdminAlarmFragmentToSetAdminCompleteFragment()
        findNavController().navigate(action)
    }
}