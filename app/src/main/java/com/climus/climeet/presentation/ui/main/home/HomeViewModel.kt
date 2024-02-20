package com.climus.climeet.presentation.ui.main.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.climus.climeet.data.model.BaseState
import com.climus.climeet.data.model.response.BannerDetailInfoResponse
import com.climus.climeet.data.model.response.BestFollowGymSimpleResponse
import com.climus.climeet.data.model.response.BestRecordGymDetailInfoResponse
import com.climus.climeet.data.model.response.BestRouteDetailInfoResponse
import com.climus.climeet.data.model.response.UserHomeGymSimpleResponse
import com.climus.climeet.data.repository.MainRepository
import com.climus.climeet.presentation.ui.main.global.selectsector.model.SelectedFilter
import com.climus.climeet.presentation.ui.main.shorts.model.ShortsThumbnailUiData
import com.climus.climeet.presentation.ui.main.shorts.model.ShortsUiData
import com.climus.climeet.presentation.ui.main.shorts.toShortsThumbnailUiData
import com.climus.climeet.presentation.ui.main.shorts.toShortsUiData
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

data class HomeUiState(
    val bannerList : List<BannerDetailInfoResponse> = emptyList(),
    val homegymList: List<UserHomeGymSimpleResponse> = emptyList(),
    val shortsList: List<ShortsUiData> = emptyList(),
    val shortsThumbnailList: List<ShortsThumbnailUiData> = emptyList(),
    val followOrderCragList: List<BestFollowGymSimpleResponse> = emptyList(),
    val recordOrderCragList: List<BestRecordGymDetailInfoResponse> = emptyList(),
    val routeList: List<BestRouteDetailInfoResponse> = emptyList(),
    val curFilter: SelectedFilter = SelectedFilter(),
    val page: Int = 0,
    val hasNext: Boolean = true
)

sealed class HomeEvent {
    data class NavigateToShortsPlayer(val shortsId: Long, val position: Int) : HomeEvent()
}

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: MainRepository): ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<HomeEvent>()
    val event: SharedFlow<HomeEvent> = _event.asSharedFlow()

    fun getBannerListBetweenDates() {
        viewModelScope.launch {
            repository.findBannerListBetweenDates().let {
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

    fun getHomeGyms() {
        viewModelScope.launch {
            repository.getHomeGyms().let {
                when(it) {
                    is BaseState.Success -> {
                        _uiState.update { state ->
                            state.copy(
                                homegymList = it.body
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

    fun getShorts() {

        // todo API 업데이트 되면, 필터 적용해서 API CALL

        val filterMap = hashMapOf<String, Long>()

        uiState.value.curFilter.let {
            if (it.cragId != -1L) {
                filterMap["gymId"] = it.cragId
            }

            if (it.sectorId != -1L) {
                filterMap["sectorId"] = it.sectorId
            }

            if (it.routeId != -1L) {
                filterMap["routeId"] = it.routeId
            }
        }


        viewModelScope.launch {
            if (uiState.value.hasNext) {
                val result = repository.getPopularShorts(uiState.value.page, 10, filterMap)

                result.let {
                    when (it) {
                        is BaseState.Success -> {

                            val shortsThumbnailUiData = it.body.result.map { data ->
                                data.toShortsThumbnailUiData(
                                    ::navigateToShortsPlayer
                                )
                            }

                            val shortsUiData = it.body.result.map { data ->
                                data.toShortsUiData()
                            }

                            _uiState.update { state ->
                                state.copy(
                                    page = uiState.value.page + 1,
                                    hasNext = it.body.hasNext,
                                    shortsThumbnailList = shortsThumbnailUiData,
                                    shortsList = shortsUiData
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

    private fun navigateToShortsPlayer(id: Long, position: Int) {
        viewModelScope.launch {
            _event.emit(HomeEvent.NavigateToShortsPlayer(id, position))
        }
    }

    fun getGymRankingOrderFollowCount() {
        viewModelScope.launch {
            repository.findGymRankingOrderFollowCount().let {
                when(it) {
                    is BaseState.Success -> {
                        _uiState.update { state ->
                            state.copy(
                                followOrderCragList = it.body
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

    fun getGymRankingListOrderSelectionCount() {
        viewModelScope.launch {
            repository.findGymRankingListOrderSelectionCount().let {
                when(it) {
                    is BaseState.Success -> {
                        _uiState.update { state ->
                            state.copy(
                               recordOrderCragList = it.body
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
            repository.findRouteRankingOrderSelectionCount().let {
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
