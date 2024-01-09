package com.climus.climeet.presentation.ui.intro.signup.admin.personal

import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentSetAdminPersonalInfoBinding
import com.climus.climeet.presentation.base.BaseFragment
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class SetAdminPersonalInfoFragment : BaseFragment<FragmentSetAdminPersonalInfoBinding>(R.layout.fragment_set_admin_personal_info) {

    private val viewModel: SetAdminPersonalViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 데이터 바인딩 변수에 뷰모델 연결
        binding.vm = viewModel

        binding.etPhone.addTextChangedListener(PhoneNumberFormattingTextWatcher())

        observeColorChange(viewModel.nameViewColor, binding.viewBar)
        observeColorChange(viewModel.phoneViewColor, binding.viewBar2)
        observeColorChange(viewModel.emailViewColor, binding.viewBar3)

        initEventObserve()
    }

    private fun initEventObserve() {
        repeatOnStarted {
            viewModel.event.collect {
                when (it) {
                    is SetAdminPersonalEvent.NavigateToBack -> findNavController().navigateUp()  // 아이디, 비번 설정으로 이동
                    is SetAdminPersonalEvent.NavigateToNext -> navigateNext()
                }
            }
        }
    }

    // color를 관찰하고, 변경될 때마다 EditText 아래 바 색상 업데이트
    private fun observeColorChange(flow: Flow<Int>, view: View) {
        lifecycleScope.launch {
            flow.collect { color ->
                view.setBackgroundColor(ContextCompat.getColor(requireContext(), color))
            }
        }
    }

    // 배경화면 설정으로 이동
    private fun navigateNext(){
        val action = SetAdminPersonalInfoFragmentDirections.actionSetAdminPersonalFragmentToSetAdminBackgroundFragment()
        findNavController().navigate(action)
    }
}