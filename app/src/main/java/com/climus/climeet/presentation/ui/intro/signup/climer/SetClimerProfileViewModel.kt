package com.climus.climeet.presentation.ui.intro.signup.climer

import android.net.Uri
import android.util.Log
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import com.climus.climeet.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class SetClimerProfileEvent {

    data object NavigateToBack : SetClimerProfileEvent()

    data object ImageClick : SetClimerProfileEvent()

}

@HiltViewModel
class SetClimerProfileViewModel @Inject constructor() : ViewModel()  {

    private val _event = MutableSharedFlow<SetClimerProfileEvent>()
    val event: SharedFlow<SetClimerProfileEvent> = _event.asSharedFlow()

    val nick = MutableStateFlow(ClimerSignupForm.nickName)

    // 이미지 URI를 저장하는 LiveData
    val imageUri = MutableLiveData<Uri?>()

    fun navigateToBack(){
        viewModelScope.launch {
            _event.emit(SetClimerProfileEvent.NavigateToBack)
        }
    }

    fun ImageClick(){
        viewModelScope.launch {
            _event.emit(SetClimerProfileEvent.ImageClick)
        }
    }

    fun setImageUri(imageUri: Uri?) {
        this.imageUri.value = imageUri
    }

}