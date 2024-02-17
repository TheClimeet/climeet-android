package com.climus.climeet.presentation.ui

import androidx.navigation.NavController
import com.climus.climeet.MainNavDirections


fun NavController.toShortsPlayer(shortsId: Long, position: Int) {
    val action = MainNavDirections.globalActionToShortsPlayerFragment(shortsId, position)
    navigate(action)
}

fun NavController.toGymProfile(gymId: Long){
    val action = MainNavDirections.globalActionToGymProfileFragment(gymId)
    navigate(action)
}

fun NavController.toClimerProfile(userId: Long){
    val action = MainNavDirections.globalActionToClimerProfileFragment(userId)
    navigate(action)
}

