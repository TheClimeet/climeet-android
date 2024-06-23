package com.climus.climeet.presentation.ui.main.mypage.myprofile.admin.editprofile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject

sealed class AdminBackgroundEditEvent {
    data object NavigateToBack : AdminBackgroundEditEvent()
    data object NavigateToNext : AdminBackgroundEditEvent()
}

@HiltViewModel
class MyPageAdminProfileEditBackgroundViewModel @Inject constructor() : ViewModel() {

    private val _event = MutableSharedFlow<AdminBackgroundEditEvent>()
    val event: SharedFlow<AdminBackgroundEditEvent> = _event.asSharedFlow()

    var backgroundImg = ""
    val imageUpdated = MutableStateFlow(false)
    private lateinit var imageToChange : MultipartBody.Part

    init {
        imageObserve()
    }

    fun initBackground(image: String){
        backgroundImg = image
    }

    private fun imageObserve(){
        AdminEditProfileForm.backgroundUriState.onEach { uri ->
            if (uri.isNotBlank()) {
                imageUpdated.value = true
                imageToChange = AdminEditProfileForm.getBackgroundPath()
                Log.d("mypage", "배경 이미지 업데이트 : $imageToChange")
            }
        }.launchIn(viewModelScope)
    }

    fun navigateToBack() {
        viewModelScope.launch {
            imageUpdated.value = false
            _event.emit(AdminBackgroundEditEvent.NavigateToBack)
        }
    }

    fun navigateToNext() {
        viewModelScope.launch {
            _event.emit(AdminBackgroundEditEvent.NavigateToNext)
        }
    }
}