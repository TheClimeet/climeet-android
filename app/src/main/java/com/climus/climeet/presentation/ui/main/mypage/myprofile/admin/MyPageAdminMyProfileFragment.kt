package com.climus.climeet.presentation.ui.main.mypage.myprofile.admin

import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentMypageAdminMyprofileBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.climus.climeet.presentation.ui.main.global.gymprofile.GymProfileViewModel
import com.climus.climeet.presentation.ui.main.global.gymprofile.adapter.GymTabAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MyPageAdminMyProfileFragment: BaseFragment<FragmentMypageAdminMyprofileBinding>(R.layout.fragment_mypage_admin_myprofile) {

    private val sharedViewModel: GymProfileViewModel by activityViewModels()
    private val viewModel: MyPageAdminMyProfileViewModel by activityViewModels()
    private var adapter : GymTabAdapter? = null

    private val args :MyPageAdminMyProfileFragmentArgs by navArgs()
    private val gymId by lazy { args.gymId }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.svm = sharedViewModel
        binding.vm = viewModel

        initEventObserve()
        initStateObserve()
        initCragInfo()
        initViewPager()
        initClickListener()

        sharedViewModel.getGymProfileInfo()
        viewModel.setGymId(gymId)
    }

    private fun initEventObserve(){
        repeatOnStarted {
            viewModel.event.collect{
                when(it){
                    MyPageAdminProfileEvent.NavigateToEditAdminProfile -> findNavController().toEditPage()
                }
            }
        }
    }

    private fun initStateObserve() {
        repeatOnStarted {
            viewModel.showSnackbar.collect { state ->
                if (state) {
                    showCustomSnackBar(binding.snackGuide, "클라이밍 암장 이름 변경이 요청되었어요.\n 이름이 변경되면 알려드릴게요!")
                    viewModel.setSnackBarState(false)
                }
            }
        }
    }

    private fun initCragInfo() {
        sharedViewModel.setGymId(gymId)
    }

    private fun initViewPager() {
        adapter = GymTabAdapter(this)
        binding.vpTabDetail.adapter = adapter

        val tabMenu = arrayListOf("커뮤니티", "루트", "정보")
        TabLayoutMediator(binding.tbMenu, binding.vpTabDetail) { tab, position ->
            tab.text = tabMenu[position]
        }.attach()
    }

    private fun initClickListener() {

        // 탭 indicator 색 바꾸기
        binding.tbMenu.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    val colorFilter = BlendModeColorFilter(
                        ContextCompat.getColor(requireContext(), R.color.cm_main),
                        BlendMode.SRC_ATOP
                    )
                    tab?.icon?.colorFilter = colorFilter
                } else {
                    tab?.icon?.setColorFilter(
                        ContextCompat.getColor(requireContext(), R.color.cm_main),
                        PorterDuff.Mode.SRC_IN
                    )
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    val colorFilter = BlendModeColorFilter(
                        ContextCompat.getColor(requireContext(), R.color.cm_grey5),
                        BlendMode.SRC_ATOP
                    )
                    tab?.icon?.colorFilter = colorFilter
                } else {
                    tab?.icon?.setColorFilter(
                        ContextCompat.getColor(requireContext(), R.color.cm_grey5),
                        PorterDuff.Mode.SRC_IN
                    )
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
    }

    private fun NavController.toEditPage(){
        val name = sharedViewModel.uiState.value.gymName
        val profile = sharedViewModel.uiState.value.gymProfileImageUrl
        val background = sharedViewModel.uiState.value.gymBackGroundImageUrl
        val action = MyPageAdminMyProfileFragmentDirections.actionMyPageAdminMyProfileFragmentToMyPageAdminProfileEditBackgroundFragment(name, profile, background)
        navigate(action)
    }
}