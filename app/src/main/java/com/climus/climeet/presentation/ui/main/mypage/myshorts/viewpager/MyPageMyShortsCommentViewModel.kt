package com.climus.climeet.presentation.ui.main.mypage.myshorts.viewpager

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.climus.climeet.data.repository.MainRepository
import com.climus.climeet.presentation.ui.main.mypage.myshorts.model.MyPageShortsCommentUiData
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

// todo: api 나요면 수정
data class ShortsCommentUiData(
    val commentList : List<MyPageShortsCommentUiData> = emptyList()
)

sealed class MyPageMyShortsCommentEvent {
    data class NavigateToShortsComment(val boardId: Long) : MyPageMyShortsCommentEvent()
}

@HiltViewModel
class MyPageMyShortsCommentViewModel @Inject constructor(val repository: MainRepository) :
    ViewModel() {

    private val _uiState = MutableStateFlow(ShortsCommentUiData())
    val uiState: StateFlow<ShortsCommentUiData> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<MyPageMyShortsCommentEvent>()
    val event: SharedFlow<MyPageMyShortsCommentEvent> = _event.asSharedFlow()

    fun getComment() {
        viewModelScope.launch {
            // todo: api 연결해 ShortsCommentUiData에 넣기

            // 더미 데이터
            val comments = listOf(
                MyPageShortsCommentUiData(
                    shortsId = 30,
                    contents = "움직임이 예술이네요",
                    profileImage = "https://climeet-production-bucket.s3.ap-northeast-2.amazonaws.com/e27898c9-d5e1-46eb-82df-f53c0ce1e4e1.jpg",
                    createdAt = "2024-06-20T12:00:00Z"
                ),
                MyPageShortsCommentUiData(
                    shortsId = 26,
                    contents = "클라이밍 몇넌 하셨어요?",
                    profileImage = null,
                    createdAt = "2024-06-20T12:00:00Z"
                ),
                MyPageShortsCommentUiData(
                    shortsId = 12,
                    contents = "다음에 같이 운동해요!",
                    profileImage = "https://climeet-production-bucket.s3.ap-northeast-2.amazonaws.com/e27898c9-d5e1-46eb-82df-f53c0ce1e4e1.jpg",
                    createdAt = "2024-06-20T12:00:00Z"
                )
            )

            _uiState.update { state ->
                state.copy(
                    commentList = comments
                )
            }
        }
    }

}