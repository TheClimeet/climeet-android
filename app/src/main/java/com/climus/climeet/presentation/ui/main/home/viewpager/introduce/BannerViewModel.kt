package com.climus.climeet.presentation.ui.main.home.viewpager.introduce

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.climus.climeet.data.model.BaseState
import com.climus.climeet.data.model.response.BannerDetailInfoResponse
import com.climus.climeet.data.model.response.BestClearClimberSimpleResponse
import com.climus.climeet.data.repository.MainRepository
import com.climus.climeet.presentation.ui.main.home.viewpager.ranking.CompleteClimbingUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class BannerUiState(
    val bannerList: List<BannerDetailInfoResponse> = emptyList()
)

@HiltViewModel
class BannerViewModel @Inject constructor(private val repository: MainRepository): ViewModel() {
    private val _uiState = MutableStateFlow(BannerUiState())
    val uiState: StateFlow<BannerUiState> = _uiState.asStateFlow()

    fun getBannerListBetweenDates() {
        viewModelScope.launch {
            repository.findBannerListBetweenDates("Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIyNSttYW5hZ2VyIiwiaWF0IjoxNzA2NzQzMjczLCJleHAiOjE3MDcxMDMyNzN9.6IKq29hpSLSPw06TVHoN-gq3EP24MjtYlDwirrrYr3U").let {
                when(it) {
                    is BaseState.Success -> {
                        _uiState.update { state ->
                            state.copy(
                                bannerList = it.body
                            )
                        }
                    }
                    is BaseState.Error -> {
                        it.msg // 서버 에러 메시지
                        Log.d("Banner List API", it.msg)
                    }
                }
            }
        }
    }
}