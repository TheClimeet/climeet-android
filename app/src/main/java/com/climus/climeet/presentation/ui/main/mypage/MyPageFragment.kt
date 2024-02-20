package com.climus.climeet.presentation.ui.main.mypage

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.climus.climeet.R
import com.climus.climeet.app.App
import com.climus.climeet.app.di.NetworkModule
import com.climus.climeet.databinding.FragmentMypageBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.climus.climeet.presentation.ui.intro.IntroActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyPageFragment: BaseFragment<FragmentMypageBinding>(R.layout.fragment_mypage) {

    private val viewModel: MyPageViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = viewModel
        initEventObserve()
        modeSwitchListener()

        viewModel.getProfileInfo()
    }

    private fun initEventObserve(){
        repeatOnStarted {
            viewModel.event.collect{
                when(it){
                    is MyPageEvent.NavigateToAccount -> findNavController().toAccount()
                    is MyPageEvent.NavigateToAlarm -> findNavController().toAlarm()
                    is MyPageEvent.NavigateToAnnounce -> findNavController().toAnnounce()
                    is MyPageEvent.NavigateToCheerUp -> findNavController().toCheerUp()
                    is MyPageEvent.NavigateToFollow -> findNavController().toFollow()
                    is MyPageEvent.NavigateToAdminMyProfile -> findNavController().toAdminMyProfile()
                    is MyPageEvent.NavigateToClimerMyProfile -> findNavController().toClimerMyProfile()
                    is MyPageEvent.NavigateToMyShorts-> findNavController().toMyShorts()
                    is MyPageEvent.NavigateToPolicy -> findNavController().toPolicy()
                    is MyPageEvent.NavigateToSendOpinion -> findNavController().toSendOpinion()
                    is MyPageEvent.Logout -> {
                        App.sharedPreferences.edit()
                            .clear()
                            .apply()
                        val intent = Intent(requireContext(),IntroActivity::class.java)
                        startActivity(intent)
                    }
                    is MyPageEvent.NavigateToReview -> findNavController().toReview()
                }
            }
        }
    }

    private fun modeSwitchListener(){

        if(NetworkModule.isProd){
            binding.switchmode.isChecked = true
            binding.tvMode.text = "PROD MODE"
        } else {
            binding.switchmode.isChecked = false
            binding.tvMode.text = "DEV MODE"
        }

        binding.switchmode.setOnCheckedChangeListener { buttonView, isChecked ->

            if(isChecked){
                binding.tvMode.text = "PROD MODE"
                NetworkModule.changeVersion(true)
            } else {
                binding.tvMode.text = "DEV MODE"
                NetworkModule.changeVersion(false)
            }

        }
    }


    private fun NavController.toAccount(){
        val action = MyPageFragmentDirections.actionMyPageFragmentToMyPageAccountFragment()
        navigate(action)
    }

    private fun NavController.toAlarm(){
        val action = MyPageFragmentDirections.actionMyPageFragmentToMyPageAlarmFragment()
        navigate(action)
    }

    private fun NavController.toAnnounce(){
        val action = MyPageFragmentDirections.actionMyPageFragmentToMyPageAnnounceFragment()
        navigate(action)
    }

    private fun NavController.toCheerUp(){
        val action = MyPageFragmentDirections.actionMyPageFragmentToMyPageCheerUpFragment()
        navigate(action)
    }

    private fun NavController.toFollow(){
        val action = MyPageFragmentDirections.actionMyPageFragmentToMyPageFollowFragment()
        navigate(action)
    }

    private fun NavController.toAdminMyProfile(){
        val action = MyPageFragmentDirections.actionMyPageFragmentToMyPageAdminMyProfileFragment()
        navigate(action)
    }

    private fun NavController.toClimerMyProfile(){
        val action = MyPageFragmentDirections.actionMyPageFragmentToMyPageClimerMyProfileFragment()
        navigate(action)
    }

    private fun NavController.toMyShorts(){
        val action = MyPageFragmentDirections.actionMyPageFragmentToMyPageMyShortsFragment()
        navigate(action)
    }

    private fun NavController.toPolicy(){
        val action = MyPageFragmentDirections.actionMyPageFragmentToMyPagePolicyFragment()
        navigate(action)
    }

    private fun NavController.toSendOpinion(){
        val action = MyPageFragmentDirections.actionMyPageFragmentToMyPageSendOpinionFragment()
        navigate(action)
    }

    private fun NavController.toReview(){
        val action = MyPageFragmentDirections.actionMyPageFragmentToMyPageReviewFragment()
        navigate(action)
    }


}