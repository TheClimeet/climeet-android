package com.climus.climeet.presentation.ui.intro.signup.admin.service

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentSetAdminServiceBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.climus.climeet.presentation.ui.intro.signup.admin.model.ServiceUiData

class SetAdminServiceFragment : BaseFragment<FragmentSetAdminServiceBinding>(R.layout.fragment_set_admin_service) {

    private val viewModel: SetAdminServiceViewModel by viewModels()

    // todo : API 호출해 서비스 리스트 저장하기

    // 임시로 데이터 생성
    val tempData = listOf(
        ServiceUiData(1, "샤워 시설"),
        ServiceUiData(2, "샤워 용품"),
        ServiceUiData(3, "수건 제공"),
        ServiceUiData(4, "간이 세면대"),
        ServiceUiData(5, "초크 대여"),
        ServiceUiData(6, "암벽화 대여"),
        ServiceUiData(7, "삼각대 대여"),
        ServiceUiData(8, "운동복 대여"),
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 데이터 바인딩 변수에 뷰모델 연결
        binding.vm = viewModel

        // 초기 데이터를 뷰 모델에 설정
        // todo : tempData 대신 API에서 받아온 데이터로 뷰 모델 설정
        viewModel.setInitialServices(tempData)

        initRecyclerview()

        initEventObserve()
    }

    private fun initEventObserve() {
        repeatOnStarted {
            viewModel.event.collect {
                when (it) {
                    is SetAdminServiceEvent.NavigateToBack -> findNavController().navigateUp()  // 배경화면 설정으로 이동
                    is SetAdminServiceEvent.NavigateToNext -> navigateNext()
                }
            }
        }
    }

    // 서비스 RecyclerView
    private fun initRecyclerview() {
        // todo : tempData 대신 API에서 받아온 값 넘겨주기
        val serviceRVAdapter = ServiceRVAdapter(viewModel, tempData)
        binding.rvService.layoutManager = GridLayoutManager(context, 2)
        binding.rvService.adapter = serviceRVAdapter
    }

    // 알림 설정으로 이동
    private fun navigateNext(){
        val action = SetAdminServiceFragmentDirections.actionSetAdminServiceFragmentToSetAdminAlarmFragment()
        findNavController().navigate(action)
    }
}