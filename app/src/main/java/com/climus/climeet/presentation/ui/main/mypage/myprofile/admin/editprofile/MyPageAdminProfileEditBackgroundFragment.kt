package com.climus.climeet.presentation.ui.main.mypage.myprofile.admin.editprofile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentMypageAdminProfileBackgroundEditBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.climus.climeet.presentation.ui.main.mypage.myprofile.climer.editprofile.EditClimberProfileEvent
import com.climus.climeet.presentation.ui.main.mypage.myprofile.climer.editprofile.MyPageClimberProfileEditFragmentArgs

class MyPageAdminProfileEditBackgroundFragment :
    BaseFragment<FragmentMypageAdminProfileBackgroundEditBinding>(R.layout.fragment_mypage_admin_profile_background_edit) {

    private val viewModel: MyPageAdminProfileEditBackgroundViewModel by  viewModels()

    private val args: MyPageAdminProfileEditBackgroundFragmentArgs by navArgs()
    private val gymName by lazy { args.gymName }
    private val gymProfile by lazy { args.gymProfile }
    private val background by lazy { args.gymBackground }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = viewModel

        initEventObserve()
    }

    private fun initEventObserve() {
        repeatOnStarted {
            viewModel.event.collect {
                when (it) {
                    AdminBackgroundEditEvent.NavigateToBack -> findNavController().navigateUp()
                    AdminBackgroundEditEvent.NavigateToNext -> findNavController().toEditProfileNext()
                }
            }
        }
    }

    private fun NavController.toEditProfileNext(){
        val action = MyPageAdminProfileEditBackgroundFragmentDirections.actionMyPageAdminProfileEditBackgroundFragmentToMyPageAdminProfileEditFragment(gymName, gymProfile)
        navigate(action)
    }
}