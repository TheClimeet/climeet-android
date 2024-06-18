package com.climus.climeet.presentation.ui

import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.climus.climeet.MainNavDirections
import com.climus.climeet.presentation.ui.main.mypage.myprofile.admin.routefinding.MyPageAdminRouteFindingFragmentDirections


fun NavController.toShortsPlayer(shortsId: Long, position: Int) {
    val action = MainNavDirections.globalActionToShortsPlayerFragment(shortsId, position)
    navigate(action)
}

fun NavController.toGymProfile(gymId: Long) {
    val action = MainNavDirections.globalActionToGymProfileFragment(gymId)
    navigate(action)
}

fun NavController.toClimerProfile(userId: Long) {
    val action = MainNavDirections.globalActionToClimerProfileFragment(userId)
    navigate(action)
}

fun NavController.toSearchProfile() {
    val action = MainNavDirections.globalActionToSearchProfileFragment()
    navigate(action)
}

fun NavController.toRouteFinding() {
    val action =
        MyPageAdminRouteFindingFragmentDirections.globalActionTomyPageAdminRouteFindingFragment()
    navigate(action)
}

