package com.climus.climeet.presentation.ui.main.mypage.myprofile.admin.info

import androidx.lifecycle.ViewModel
import com.climus.climeet.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MyPageAdminProfileEditServiceViewModel @Inject constructor(val repository: MainRepository): ViewModel() {

}