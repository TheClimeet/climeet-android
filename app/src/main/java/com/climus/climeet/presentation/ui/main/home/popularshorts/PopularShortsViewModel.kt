package com.climus.climeet.presentation.ui.main.home.popularshorts

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.climus.climeet.data.model.BaseState
import com.climus.climeet.data.model.response.ShortsListResponse
import com.climus.climeet.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PopularShortsUiState(
    val shortsList : ShortsListResponse? = null,
)

@HiltViewModel
class PopularShortsViewModel @Inject constructor(private val repository: MainRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(PopularShortsUiState())
    val uiState: StateFlow<PopularShortsUiState> = _uiState.asStateFlow()

    fun getShorts() {
        // todo API 업데이트 되면, 필터 적용해서 API CALL

        viewModelScope.launch {
            repository.getPopularShorts(0, 20, hashMapOf()).let {
                when(it) {
                    is BaseState.Success -> {
                        _uiState.update { state ->
                            state.copy(
                                shortsList = it.body
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