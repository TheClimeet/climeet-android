package com.climus.climeet.presentation.ui.main.home.popularcrags.viewpager

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.climus.climeet.data.model.BaseState
import com.climus.climeet.data.model.response.BannerDetailInfoResponse
import com.climus.climeet.data.model.response.BestFollowGymSimpleResponse
import com.climus.climeet.data.model.response.BestRouteDetailInfoResponse
import com.climus.climeet.data.model.response.ShortsSimpleResponse
import com.climus.climeet.data.repository.MainRepository
import com.climus.climeet.presentation.ui.main.home.HomeUiState
import com.climus.climeet.presentation.ui.main.home.model.HomeGym
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class FollowerOrderUiState(
    val cragList: List<BestFollowGymSimpleResponse> = emptyList()
)

@HiltViewModel
class FollowerOrderViewModel @Inject constructor(private val repository: MainRepository): ViewModel() {

    private val _uiState = MutableStateFlow(FollowerOrderUiState())
    val uiState: StateFlow<FollowerOrderUiState> = _uiState.asStateFlow()

    fun getGymRankingOrderFollowCount() {
        viewModelScope.launch {
            repository.findGymRankingOrderFollowCount().let {
                when(it) {
                    is BaseState.Success -> {
                        _uiState.update { state ->
                            state.copy(
                                cragList = it.body
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