package com.climus.climeet.presentation.ui.main.record.timer.stopwatch.selectcrag

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.climus.climeet.presentation.ui.main.record.model.RecordCragData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class CragNameUiState(
    val searchList: List<RecordCragData> = emptyList(),
    val selectedItems: List<RecordCragData> = emptyList()
)

@HiltViewModel
class CragSelectBottomViewModel @Inject constructor() : ViewModel() {

    //todo
    // - 암장 검색 API 연결하기
    // - 암장 검색 API 연결해 결과 개수 tv_crag_num에 반영하기

    private val _uiState = MutableStateFlow(CragNameUiState())
    val uiState: StateFlow<CragNameUiState> = _uiState.asStateFlow()

    val keyword = MutableStateFlow("")

    val selectedCrag = MutableLiveData<RecordCragData>()


    init {
        observeKeyword()
    }

    private fun observeKeyword() {
        keyword.onEach {

            if (it.isBlank()) {
                _uiState.update { state ->
                    state.copy(
                        searchList = emptyList()
                    )
                }
            } else {
                // todo 타이핑 할 때마다 API 호출
                _uiState.update { state ->
                    state.copy(
                        searchList = listOf(
                            RecordCragData(null, "더클라임 클라이밍 강남점", false),
                            RecordCragData(null, "더클라임 클라이밍 논현점", false),
                            RecordCragData(null, "더클라임 클라이밍 마곡점", false),
                            RecordCragData(null, "더클라임 클라이밍 홍대점", false),
                            RecordCragData(null, "더클라임 클라이밍 강남점", false),
                            RecordCragData(null, "더클라임 클라이밍 논현점", false),
                            RecordCragData(null, "더클라임 클라이밍 마곡점", false),
                            RecordCragData(null, "더클라임 클라이밍 홍대점", false),
                            RecordCragData(null, "더클라임 클라이밍 강남점", false),
                            RecordCragData(null, "더클라임 클라이밍 논현점", false),
                            RecordCragData(null, "더클라임 클라이밍 마곡점", false),
                            RecordCragData(null, "더클라임 클라이밍 홍대점", false)
                        )
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    fun deleteKeyword() {
        keyword.value = ""
        _uiState.update { state ->
            state.copy(
                searchList = emptyList()
            )
        }
    }

    // 선택된 암장 관리
    fun setSelectedItem(item: RecordCragData) {
        selectedCrag.value = item // 암장 선택 시 선택 정보 업데이트
    }
}