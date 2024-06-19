package com.climus.climeet.presentation.ui.main.mypage.announce

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.climus.climeet.data.model.BaseState
import com.climus.climeet.data.repository.MainRepository
import com.climus.climeet.presentation.ui.main.global.toAnnouncementUiData
import com.climus.climeet.presentation.ui.main.mypage.announce.model.AnnouncementUiData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AnnounceUiData(
    val announceList : List<AnnouncementUiData> = emptyList()
)

sealed class MyPageAnnounceEvent {
    data class NavigateToAnnounceDetail(val boardId: Long) : MyPageAnnounceEvent()
}

@HiltViewModel
class MyPageAnnounceViewModel @Inject constructor(val repository: MainRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(AnnounceUiData())
    val uiState: StateFlow<AnnounceUiData> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<MyPageAnnounceEvent>()
    val event: SharedFlow<MyPageAnnounceEvent> = _event.asSharedFlow()

    fun getAnnouncement() {
        viewModelScope.launch {
            repository.getAnnouncement().let {
                when (it) {
                    is BaseState.Success -> {
                        val announcements = it.body.map { response -> response.toAnnouncementUiData() }
                        _uiState.update { state ->
                            state.copy(
                                announceList = announcements
                            )
                        }
                    }

                    is BaseState.Error -> {
                        it.msg // 서버 에러 메시지
                        Log.d("API", it.msg)
                    }
                }
            }
        }
    }

    fun navigateToAnnounceDetail(boardId: Long) {
        viewModelScope.launch {
            _event.emit(MyPageAnnounceEvent.NavigateToAnnounceDetail(boardId))
        }
    }
}