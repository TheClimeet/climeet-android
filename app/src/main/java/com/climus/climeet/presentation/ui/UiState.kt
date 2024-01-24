package com.climus.climeet.presentation.ui

sealed class InputState{
    data object Empty: InputState()
    data class Error(val msg: String): InputState()
    data class Success(val msg: String): InputState()
}