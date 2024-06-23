package com.climus.climeet.presentation.ui.main.mypage.myprofile.admin.info

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentMypageAdminProfileEditServiceBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.climus.climeet.presentation.ui.intro.signup.admin.model.ServiceUiData
import com.climus.climeet.presentation.ui.intro.signup.admin.service.OnServiceClickListener
import com.climus.climeet.presentation.ui.intro.signup.admin.service.ServiceRVAdapter
import com.climus.climeet.presentation.ui.main.global.gymprofile.info.GymProfileInfoViewModel
import com.climus.climeet.presentation.ui.main.mypage.myprofile.admin.MyPageAdminMyProfileFragmentArgs
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MyPageAdminProfileEditServiceFragment :
    BaseFragment<FragmentMypageAdminProfileEditServiceBinding>(R.layout.fragment_mypage_admin_profile_edit_service),
    OnServiceClickListener {

    val sharedViewModel: GymProfileInfoViewModel by activityViewModels()
    val viewModel: MyPageAdminProfileEditServiceViewModel by viewModels()

    private val args: MyPageAdminMyProfileFragmentArgs by navArgs()
    private val gymId by lazy { args.gymId }

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

        binding.vm = viewModel

        // gymServiceList 가져와 serviceList 업데이트
        updateServiceListSelectedState()

        viewModel.setInitialServices(serviceList)

        initRecyclerview()
        initEventObserve()
    }

    override fun onServiceClick(position: Int) {
        viewModel.toggleServiceSelection(position)
        serviceRVAdapter.notifyItemChanged(position)
    }

    private fun updateServiceListSelectedState() {
        val gymServiceList = sharedViewModel.uiState.value.gymServiceList ?: return
        for (service in serviceList) {
            if (gymServiceList.any { it.name.replace("_", " ") == service.title }) {
                service.isSelected = true
            }
        }
    }

    private fun initEventObserve() {
        repeatOnStarted {
            viewModel.event.collect {
                when (it) {
                    is EditAdminServiceEvent.NavigateToBack -> findNavController().navigateUp()
                    is EditAdminServiceEvent.NavigateToProfile -> findNavController().toMyPageAdminProfile()
                    is EditAdminServiceEvent.ShowToastMessage -> showToastMessage(it.msg)
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

    private fun NavController.toMyPageAdminProfile() {
        val action =
            MyPageAdminProfileEditServiceFragmentDirections.actionMyPageAdminProfileEditServiceFragmentToMyPageAdminMyProfileFragment(
                gymId
            )
        navigate(action)
    }
}