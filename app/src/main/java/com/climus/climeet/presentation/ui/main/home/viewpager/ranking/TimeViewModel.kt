package com.climus.climeet.presentation.ui.main.home.viewpager.ranking

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.climus.climeet.data.model.BaseState
import com.climus.climeet.data.model.response.BestTimeClimberSimpleResponse
import com.climus.climeet.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TimeUiState(
    val rankingList: List<BestTimeClimberSimpleResponse> = emptyList()
)

@HiltViewModel
class TimeViewModel @Inject constructor(private val repository: MainRepository): ViewModel() {
    private val _uiState = MutableStateFlow(TimeUiState())
    val uiState: StateFlow<TimeUiState> = _uiState.asStateFlow()

    fun getClimberRankingOrderTime() {
        viewModelScope.launch {
            repository.findClimberRankingOrderTime("Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxK2NsaW1iZXIiLCJpYXQiOjE3MDY4NTkzNDQsImV4cCI6MTcwNzIxOTM0NH0.XDHZlOUDY-C8Ac99JHRB5Oi3YjBf--cTpHhwSM09lpU").let {
                when(it) {
                    is BaseState.Success -> {
                        _uiState.update { state ->
                            state.copy(
                                rankingList = it.body
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
}