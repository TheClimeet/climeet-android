package com.climus.climeet.presentation.ui.main.home.viewpager.ranking

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.climus.climeet.data.model.BaseState
import com.climus.climeet.data.model.response.BestClearClimberSimpleResponse
import com.climus.climeet.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CompleteClimbingUiState(
    val rankingList: List<BestClearClimberSimpleResponse> = emptyList()
)

@HiltViewModel
class CompleteClimbingViewModel @Inject constructor(private val repository: MainRepository): ViewModel() {

    private val _uiState = MutableStateFlow(CompleteClimbingUiState())
    val uiState: StateFlow<CompleteClimbingUiState> = _uiState.asStateFlow()
    // MutableLiveData를 LiveData로 변경
//    private val _rankingList = MutableLiveData<List<BestClearClimberResponse>?>()
//    val rankingList: LiveData<List<BestClearClimberResponse>?> get() = _rankingList

    fun getClimberRankingOrderClearCount() {
        viewModelScope.launch {
            repository.findClimberRankingOrderClearCount("Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNzA2NDI0OTY5LCJleHAiOjE3MjE5NzY5Njl9.BzA3Cj_KPcLuKsusBDFaoVlz5ltnrwsZHo_lCcfHBk-nNqG643v40GwnrZIYDRVK2H0A6SvXMBurYiklIAmGqg").let {
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
