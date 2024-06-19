package com.climus.climeet.presentation.ui.main.mypage.myshorts.viewpager

import androidx.lifecycle.ViewModel
import com.climus.climeet.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MyPageShortsCommentViewModel @Inject constructor(val repository: MainRepository) :
    ViewModel() {


}