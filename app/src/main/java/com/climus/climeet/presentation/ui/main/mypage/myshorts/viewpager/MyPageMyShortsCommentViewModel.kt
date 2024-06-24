package com.climus.climeet.presentation.ui.main.mypage.myshorts.viewpager

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.climus.climeet.data.model.BaseState
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
    val commentList: List<MyPageShortsCommentUiData> = emptyList(),
)

sealed class MyPageMyShortsCommentEvent {
    data class NavigateToShortsComment(val boardId: Long) : MyPageMyShortsCommentEvent()
    data class ShowToastMessage(val msg: String) : MyPageMyShortsCommentEvent()
}

@HiltViewModel
class MyPageMyShortsCommentViewModel @Inject constructor(val repository: MainRepository) :
    ViewModel() {

    private val _uiState = MutableStateFlow(ShortsCommentUiData())
    val uiState: StateFlow<ShortsCommentUiData> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<MyPageMyShortsCommentEvent>()
    val event: SharedFlow<MyPageMyShortsCommentEvent> = _event.asSharedFlow()

    var currentPage = 0
    private var hasNextPage = true

    fun getComment(page: Int) {
        if (!hasNextPage) return

        viewModelScope.launch {
            repository.getMyShortsComments(page, 15).let {
                when (it) {
                    is BaseState.Success -> {
                        val comments = it.body.result.map { comment ->
                            MyPageShortsCommentUiData(
                                shortsId = comment.commentId,
                                content = comment.content,
                                profileImage = comment.profileImageUrl,
                                createdAt = comment.createdDate
                            )
                        }
                        _uiState.update { currentUiState ->
                            ShortsCommentUiData(
                                commentList = currentUiState.commentList + comments
                            )
                        }
                        currentPage = it.body.page
                        hasNextPage = it.body.hasNext
                    }

                    is BaseState.Error -> {
                        _event.emit(MyPageMyShortsCommentEvent.ShowToastMessage(it.msg))
                    }
                }
            }
        }
    }
}