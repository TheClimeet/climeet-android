package com.climus.climeet.presentation.ui.intro.signup.climer.howtoknow

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.climus.climeet.presentation.ui.intro.signup.climer.ClimerSignupForm
import com.climus.climeet.presentation.ui.intro.signup.climer.model.WayItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class HowTokKnowEvent {
    data object NavigateToBack : HowTokKnowEvent()
    data object NavigateToNext : HowTokKnowEvent()
}

@HiltViewModel
class HowToKnowViewModel @Inject constructor() : ViewModel() {
    private val _event = MutableSharedFlow<HowTokKnowEvent>()
    val event: SharedFlow<HowTokKnowEvent> = _event.asSharedFlow()

    var explainText = MutableStateFlow("${ClimerSignupForm.nickName}님,\n클밋을 어떻게 알게 되었나요?")

    private val _levels = MutableLiveData<List<WayItem>>(listOf())
    val levels: LiveData<List<WayItem>> = _levels

    private var selectedPosition = -1

    val isNextButtonVisible = MutableLiveData(selectedPosition != -1)

    init {
        val initialLevels = listOf(
            WayItem("인스타그램 / 페이스북"),
            WayItem("유튜브"),
            WayItem("지인 추천"),
            WayItem("블로그 / 카페 / 커뮤니티"),
            WayItem("기타")
        )
        _levels.value = initialLevels
    }

    fun navigateToBack() {
        viewModelScope.launch {
            _event.emit(HowTokKnowEvent.NavigateToBack)
        }
    }

    fun navigateToNext() {
        ClimerSignupForm.setWay(selectedPosition)
        viewModelScope.launch {
            _event.emit(HowTokKnowEvent.NavigateToNext)
        }
        Log.d("how to know", selectedPosition.toString())
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