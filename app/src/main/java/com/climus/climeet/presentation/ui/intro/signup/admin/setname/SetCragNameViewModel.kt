package com.climus.climeet.presentation.ui.intro.signup.admin.setname

import androidx.lifecycle.ViewModel
import com.climus.climeet.presentation.ui.intro.signup.admin.model.CragInfoUiData
import com.climus.climeet.presentation.util.Constants.TEST_IMG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SetCragNameViewModel @Inject constructor(): ViewModel(){

    private val _uiCragInfo = MutableStateFlow(CragInfoUiData())
    val uiCragInfo: StateFlow<CragInfoUiData> = _uiCragInfo.asStateFlow()

    private var cragId: Long ?= null

    fun setCragId(id: Long){
        cragId = id
        getCragInfo()
    }

    private fun getCragInfo(){
        // todo 서버통신

        _uiCragInfo.update { state ->
            state.copy(
                imgUrl = TEST_IMG,
                name = "클라이머스 클라이밍",
                state = false
            )
        }
    }

}