package com.climus.climeet.presentation.ui.intro.signup.climer

import android.graphics.Rect
import android.util.Log
import android.view.View
import androidx.compose.runtime.MutableState
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import com.climus.climeet.presentation.ui.intro.signup.climer.model.LevelItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class SetClimerLevelEvent {

    data object NavigateToBack : SetClimerLevelEvent()

    data object NavigateToNext : SetClimerLevelEvent()

}

@HiltViewModel
class SetClimerLevelViewModel @Inject constructor() : ViewModel() {

    private val _event = MutableSharedFlow<SetClimerLevelEvent>()
    val event: SharedFlow<SetClimerLevelEvent> = _event.asSharedFlow()

    var explainText = MutableStateFlow("${ClimerSignupForm.nickName}님, 클라이밍을\n시작한지 얼마나 되셨어요?")

    private val _levels = MutableLiveData<List<LevelItem>>(listOf())
    val levels: LiveData<List<LevelItem>> = _levels

    private var selectedPosition = -1

    val isNextButtonVisible = MutableLiveData(selectedPosition != -1)

    init {
        val initialLevels = listOf(
            LevelItem("입문", "클라이밍이 처음이에요."),
            LevelItem("초급", "클라이밍을 몇 번 해봤어요."),
            LevelItem("중급", "V3 이상의 루트를 주로 타요."),
            LevelItem("고급", "V5 이상의 루트를 주로 타요."),
            LevelItem("전문가", "V8 이상의 루트를 완등할 수 있어요.")
        )
        _levels.value = initialLevels
    }

    fun navigateToBack() {
        viewModelScope.launch {
            _event.emit(SetClimerLevelEvent.NavigateToBack)
        }
    }

    fun navigateToSetNext() {
        ClimerSignupForm.setLevel(selectedPosition)
        viewModelScope.launch {
            _event.emit(SetClimerLevelEvent.NavigateToNext)
        }
    }

    fun selectLevel(position: Int) {
        // 이전 선택되었던 항목 업데이트
        if (selectedPosition != -1) {
            _levels.value?.get(selectedPosition)?.isSelected = false
        }

        // 새로 선택된 항목 업데이트
        _levels.value?.get(position)?.isSelected = true
        selectedPosition = position

        isNextButtonVisible.value = selectedPosition != -1

        // UI반영
        _levels.value = _levels.value
    }

}