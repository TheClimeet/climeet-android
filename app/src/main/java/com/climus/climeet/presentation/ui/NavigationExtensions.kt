package com.climus.climeet.presentation.ui

import androidx.navigation.NavController
import com.climus.climeet.MainNavDirections


fun NavController.toShortsPlayer(shortsId: Long, position: Int) {
    val action = MainNavDirections.globalActionToShortsPlayerFragment(shortsId, position)
    navigate(action)
}

fun NavController.toGymProfile(){
    val action = MainNavDirections.globalActionToGymProfileFragment()
    navigate(action)
}

fun NavController.toClimerProfile(){
    val action = MainNavDirections.globalActionToClimerProfileFragment()
    navigate(action)
}

