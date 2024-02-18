package com.climus.climeet.presentation.ui.main.global.gymprofile

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GymProfileViewModel @Inject constructor() : ViewModel() {

    var gymId = MutableLiveData<Long>()
    var gymName = MutableLiveData<String>()

}