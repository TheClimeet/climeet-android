package com.climus.climeet.presentation.ui.main.global.gymprofile.community

import androidx.lifecycle.ViewModel
import com.climus.climeet.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GymProfileCommunityViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {


}