package com.climus.climeet.presentation.ui.intro.signup.admin.service

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentSetAdminServiceBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.climus.climeet.presentation.ui.intro.IntroViewModel
import com.climus.climeet.presentation.ui.intro.signup.admin.model.ServiceUiData

class SetAdminServiceFragment : BaseFragment<FragmentSetAdminServiceBinding>(R.layout.fragment_set_admin_service),
    OnServiceClickListener {

    private val parentViewModel: IntroViewModel by activityViewModels()
    private val viewModel: SetAdminServiceViewModel by viewModels()
    private lateinit var serviceRVAdapter: ServiceRVAdapter

    private val serviceList = listOf(
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

        parentViewModel.adminSignUpProgress(6)
        binding.vm = viewModel

        // 초기 데이터를 뷰 모델에 설정
        viewModel.setInitialServices(serviceList)

        initRecyclerview()
        initEventObserve()
    }

    override fun onServiceClick(position: Int) {
        viewModel.toggleServiceSelection(position)
        serviceRVAdapter.notifyItemChanged(position)
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
        serviceRVAdapter = ServiceRVAdapter(serviceList, this)
        binding.rvService.layoutManager = GridLayoutManager(context, 2)
        binding.rvService.adapter = serviceRVAdapter
    }

    // 알림 설정으로 이동
    private fun navigateNext(){
        val action = SetAdminServiceFragmentDirections.actionSetAdminServiceFragmentToSetAdminAlarmFragment()
        findNavController().navigate(action)
    }
}