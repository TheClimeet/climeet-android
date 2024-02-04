package com.climus.climeet.presentation.ui.main.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.climus.climeet.data.model.BaseState
import com.climus.climeet.data.model.response.BannerDetailInfoResponse
import com.climus.climeet.data.model.response.BestFollowGymSimpleResponse
import com.climus.climeet.data.model.response.BestRouteSimpleResponse
import com.climus.climeet.data.model.response.ShortsSimpleResponse
import com.climus.climeet.data.repository.MainRepository
import com.climus.climeet.presentation.ui.main.home.model.HomeGym
import com.climus.climeet.presentation.ui.main.home.model.PopularCrag
import com.climus.climeet.presentation.ui.main.home.model.PopularShorts
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val bannerList : List<BannerDetailInfoResponse> = emptyList(),
    val homegymList: List<HomeGym> = emptyList(),
    val shortsList: List<ShortsSimpleResponse> = emptyList(),
    val cragList: List<BestFollowGymSimpleResponse> = emptyList(),
    val routeList: List<BestRouteSimpleResponse> = emptyList()
)

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: MainRepository): ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    fun getPopularShorts() {
        viewModelScope.launch {
            repository.findPopularShorts("Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNzA2NDI0OTY5LCJleHAiOjE3MjE5NzY5Njl9.BzA3Cj_KPcLuKsusBDFaoVlz5ltnrwsZHo_lCcfHBk-nNqG643v40GwnrZIYDRVK2H0A6SvXMBurYiklIAmGqg", 0, 20).let {
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

    fun getGymRankingOrderFollowCount() {
        viewModelScope.launch {
            repository.findGymRankingOrderFollowCount("Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNzA2NDI0OTY5LCJleHAiOjE3MjE5NzY5Njl9.BzA3Cj_KPcLuKsusBDFaoVlz5ltnrwsZHo_lCcfHBk-nNqG643v40GwnrZIYDRVK2H0A6SvXMBurYiklIAmGqg").let {
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

    fun getRouteRankingOrderSelectionCount() {
        viewModelScope.launch {
            repository.findRouteRankingOrderSelectionCount("Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNzA2NDI0OTY5LCJleHAiOjE3MjE5NzY5Njl9.BzA3Cj_KPcLuKsusBDFaoVlz5ltnrwsZHo_lCcfHBk-nNqG643v40GwnrZIYDRVK2H0A6SvXMBurYiklIAmGqg").let {
                when(it) {
                    is BaseState.Success -> {
                        _uiState.update { state ->
                            state.copy(
                                routeList = it.body
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
