package com.climus.climeet.presentation.ui.intro.signup.climer.setprofile

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.climus.climeet.data.repository.GlobalRepository
import com.climus.climeet.presentation.ui.intro.signup.climer.ClimerSignupForm
import com.climus.climeet.presentation.util.Constants.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

sealed class SetClimerProfileEvent {
    data object NavigateToBack : SetClimerProfileEvent()
    data object NavigateToSetLevel : SetClimerProfileEvent()
}

@HiltViewModel
class SetClimerProfileViewModel @Inject constructor(
    private val repository: GlobalRepository
) : ViewModel() {

    private val _event = MutableSharedFlow<SetClimerProfileEvent>()
    val event: SharedFlow<SetClimerProfileEvent> = _event.asSharedFlow()

    val nick = MutableStateFlow(ClimerSignupForm.nickName)

    // 이미지 URI를 저장하는 LiveData
    val imageUri = MutableLiveData<Uri?>()
    var imageUriString = ""

    val isNextButtonVisible = imageUri.map { it != null }

    fun navigateToBack() {
        viewModelScope.launch {
            _event.emit(SetClimerProfileEvent.NavigateToBack)
        }
    }

    fun setImageUri(context: Context, imageUri: Uri?) {
        Log.d(TAG, "uri = $imageUri")
        this.imageUri.value = imageUri
        if (imageUri != null) {
            val inputStream = context.contentResolver.openInputStream(imageUri)
            val requestBody = inputStream?.readBytes()?.toRequestBody("image/*".toMediaTypeOrNull())
            if (requestBody != null) {
                uploadImage(requestBody)
            }
        }
    }
    fun uploadImage(requestBody: RequestBody) {
        viewModelScope.launch {
            try {
                val response = repository.uploadImage(requestBody)
                val responseBody = response.body()
                if (response.body()!!.isSuccess) {
                    imageUriString =  responseBody!!.result.imgUrl
                    ClimerSignupForm.setImageUri(imageUriString)
                } else {
                    Log.d(TAG, "실패 ${response.body()!!.code}")
                }

            }catch(e : Exception){
                Log.e(TAG, "이미지 에러 : $e")
            }

        }
    }

    fun navigateToSetLevel() {
        viewModelScope.launch {
            _event.emit(SetClimerProfileEvent.NavigateToSetLevel)
        }
    }

}