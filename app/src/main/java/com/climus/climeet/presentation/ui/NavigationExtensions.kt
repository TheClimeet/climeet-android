package com.climus.climeet.presentation.ui

import androidx.navigation.NavController
import com.climus.climeet.MainNavDirections


fun NavController.toShortsPlayer(shortsId: Long, position: Int) {
    val action = MainNavDirections.globalActionToShortsPlayerFragment(shortsId, position)
    navigate(action)
}
