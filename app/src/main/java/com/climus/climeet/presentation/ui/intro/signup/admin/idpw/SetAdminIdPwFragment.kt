package com.climus.climeet.presentation.ui.intro.signup.admin.idpw

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentSetAdminIdPwBinding
import com.climus.climeet.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SetAdminIdPwFragment :
    BaseFragment<FragmentSetAdminIdPwBinding>(R.layout.fragment_set_admin_id_pw) {

    private val viewModel: SetAdminIdPwViewModel by viewModels()

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
                    is SetAdminIdPwEvent.NavigateToBack -> findNavController().navigateUp() // 사업자 등록증 설정으로 이동
                    is SetAdminIdPwEvent.NavigateToNext -> navigateNext()
                    is SetAdminIdPwEvent.ShowToastMessage -> showToastMessage(it.msg)
                }
            }
        }
    }

    // 개인정보 설정으로 이동
    private fun navigateNext() {
        val action =
            SetAdminIdPwFragmentDirections.actionSetAdminIdPwFragmentToSetAdminPersonalFragment()
        findNavController().navigate(action)
    }
}