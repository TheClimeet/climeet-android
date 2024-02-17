package com.climus.climeet.presentation.ui.main.record.timer

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class TimerMainViewModel @Inject constructor() : ViewModel() {
    private val _moveToTimerFragmentEvent = MutableLiveData<Unit>()
    val moveToTimerFragmentEvent: LiveData<Unit> get() = _moveToTimerFragmentEvent

    // TimerFragment로 이동하는 이벤트 발생
    fun moveToStopwatch() {
        _moveToTimerFragmentEvent.value = Unit
    }
}